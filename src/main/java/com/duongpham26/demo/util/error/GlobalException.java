package com.duongpham26.demo.util.error;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.duongpham26.demo.entity.RestResponse;

// @ControllerAdvice
// @ResponseBody
@RestControllerAdvice // = @ControllerAdvice + @ResponseBody // AOP
public class GlobalException {
   @ExceptionHandler(value = {
         IdInvalidException.class,
         UsernameNotFoundException.class,
         BadCredentialsException.class // exception cho security filter
   })
   public ResponseEntity<RestResponse<Object>> handleInException(Exception ex) {
      RestResponse<Object> res = new RestResponse<Object>();
      res.setStatusCode(HttpStatus.BAD_REQUEST.value());
      res.setError(ex.getMessage());
      res.setMessage("Exception occurs");
      return ResponseEntity.badRequest().body(res);
   }

   @ExceptionHandler(value = MethodArgumentNotValidException.class) // @valid exception
   public ResponseEntity<RestResponse<Object>> validationError(MethodArgumentNotValidException ex) {
      BindingResult result = ex.getBindingResult();
      final List<FieldError> fieldError = result.getFieldErrors();
      List<String> errors = fieldError.stream().map(f -> f.getDefaultMessage()).collect(Collectors.toList());

      RestResponse<Object> res = new RestResponse<>();
      res.setStatusCode(HttpStatus.BAD_REQUEST.value());
      res.setError(ex.getBody().getDetail());
      res.setMessage(errors.size() > 1 ? errors : errors.get(0));

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
   }

   @ExceptionHandler(value = { NoResourceFoundException.class })
   public ResponseEntity<RestResponse<Object>> handleNotFoundException(Exception ex) {
      RestResponse<Object> res = new RestResponse<Object>();
      res.setStatusCode(HttpStatus.NOT_FOUND.value());
      res.setError(ex.getMessage());
      res.setMessage("404 Not Found.");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
   }

   @ExceptionHandler(value = { StorageException.class })
   public ResponseEntity<RestResponse<Object>> handleFileUploadException(Exception ex) {
      RestResponse<Object> res = new RestResponse<Object>();
      res.setStatusCode(HttpStatus.BAD_REQUEST.value());
      res.setError(ex.getMessage());
      res.setMessage("Exception upload file...");
      return ResponseEntity.badRequest().body(res);
   }

   @ExceptionHandler(value = { PermissionException.class })
   public ResponseEntity<RestResponse<Object>> handleFilePermissionException(Exception ex) {
      RestResponse<Object> res = new RestResponse<Object>();
      res.setStatusCode(HttpStatus.FORBIDDEN.value());
      res.setError(ex.getMessage());
      res.setMessage("FORBIDDEN");
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
   }

   // all exception not define
   @ExceptionHandler(value = { Exception.class })
   public ResponseEntity<RestResponse<Object>> handleAllException(Exception ex) {
      RestResponse<Object> res = new RestResponse<Object>();
      res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
      res.setError(ex.getMessage());
      res.setMessage("INTERNAL SERVICE ERROR");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
   }
}
