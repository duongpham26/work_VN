package com.duongpham26.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.duongpham26.demo.entity.dto.LoginDTO;
import com.duongpham26.demo.entity.dto.RestLoginDTO;
import com.duongpham26.demo.util.SecurityUtil;

import jakarta.validation.Valid;

@RestController
public class AuthController {

   private final AuthenticationManagerBuilder authenticationManagerBuilder;

   private final SecurityUtil securityUtil;

   public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil) {
      this.authenticationManagerBuilder = authenticationManagerBuilder;
      this.securityUtil = securityUtil;
   }

   @PostMapping("/login")
   public ResponseEntity<RestLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {

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

      RestLoginDTO restLoginDTO = new RestLoginDTO();
      restLoginDTO.setAccessToken(access_token);
      return ResponseEntity.ok().body(restLoginDTO);
   }
}
