package com.duongpham26.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duongpham26.demo.entity.User;
import com.duongpham26.demo.entity.dto.LoginDTO;
import com.duongpham26.demo.entity.dto.response.ResLoginDTO;
import com.duongpham26.demo.service.UserService;
import com.duongpham26.demo.util.SecurityUtil;
import com.duongpham26.demo.util.annotation.ApiMessage;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

   private final AuthenticationManagerBuilder authenticationManagerBuilder;

   private final SecurityUtil securityUtil;

   private final UserService userService;

   public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil,
         UserService userService) {
      this.authenticationManagerBuilder = authenticationManagerBuilder;
      this.securityUtil = securityUtil;
      this.userService = userService;
   }

   @PostMapping("/login")
   @ApiMessage("Login successfully")
   public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {

      // nap thông tin vào security
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginDTO.getUsername(),
            loginDTO.getPassword());

      // xác thực người dùng -> viết hàm loadUserByUserName để lấy thông tin người
      // dùng lưu ở đâu dể xác thức
      Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

      // tao access token
      String access_token = this.securityUtil.createToken(authentication);
      SecurityContextHolder.getContext().setAuthentication(authentication);

      ResLoginDTO restLoginDTO = new ResLoginDTO(); // cần trả ra đối tượng, nếu trả ra String thì bị lỗi không thể cast
                                                    // String - > Object
      User currentUserFromDB = this.userService.handleGetUserByUserName(loginDTO.getUsername());

      if (currentUserFromDB != null) {
         ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
               currentUserFromDB.getId(),
               currentUserFromDB.getEmail(),
               currentUserFromDB.getName());

         restLoginDTO.setUserLogin(userLogin);
      }
      restLoginDTO.setAccessToken(access_token);

      return ResponseEntity.ok().body(restLoginDTO);
   }
}
