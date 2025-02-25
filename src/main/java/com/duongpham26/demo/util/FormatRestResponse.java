package com.duongpham26.demo.util;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.duongpham26.demo.entity.RestResponse;
import com.duongpham26.demo.util.annotation.ApiMessage;

import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice // @RestControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {

   @Override
   public boolean supports(MethodParameter returnType, Class converterType) {
      // TODO Auto-generated method stub
      return true;
   }

   @Override
   @Nullable
   public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType,
         Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

      HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
      int status = servletResponse.getStatus();

      RestResponse<Object> res = new RestResponse<Object>();
      res.setStatusCode(status);

      if (body instanceof String) { // nếu body là String thì trả về không làm gì cả (RestResponse cannot be cast to
                                    // class String)
         return body;
      }
      if (status >= 400) {
         return body;
      } else {
         res.setData(body);

         ApiMessage apiMessage = returnType.getMethodAnnotation(ApiMessage.class); // lay gia tri annotation
         res.setMessage(apiMessage != null ? apiMessage.value() : "CALL API SUCCESS"); // chỉ lấy khi annotation có giá
                                                                                       // trị
      }
      return res;
   }

}
