package com.duongpham26.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duongpham26.demo.entity.User;
import com.duongpham26.demo.entity.dto.request.ReqLoginDTO;
import com.duongpham26.demo.entity.dto.response.ResLoginDTO;
import com.duongpham26.demo.service.UserService;
import com.duongpham26.demo.util.SecurityUtil;
import com.duongpham26.demo.util.annotation.ApiMessage;
import com.duongpham26.demo.util.error.IdInvalidException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

   private final AuthenticationManagerBuilder authenticationManagerBuilder;

   private final SecurityUtil securityUtil;

   private final UserService userService;

   @Value("${duongpham26.jwt.refresh-token-validity-in-seconds}")
   private long refreshTokenExpiration;

   public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil,
         UserService userService) {
      this.authenticationManagerBuilder = authenticationManagerBuilder;
      this.securityUtil = securityUtil;
      this.userService = userService;
   }

   @PostMapping("/auth/login")
   @ApiMessage("Login")
   public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO loginDTO) {

      // nap thông tin vào security
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginDTO.getUsername(),
            loginDTO.getPassword());

      // xác thực người dùng -> viết hàm loadUserByUserName để lấy thông tin người
      // dùng lưu ở đâu dể xác thức
      Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
      SecurityContextHolder.getContext().setAuthentication(authentication);

      ResLoginDTO resLoginDTO = new ResLoginDTO(); // cần trả ra đối tượng, nếu trả ra String thì bị lỗi không thể cast
                                                   // String - > Object
      User currentUserFromDB = this.userService.handleGetUserByUserName(loginDTO.getUsername());

      if (currentUserFromDB != null) {
         ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
               currentUserFromDB.getId(),
               currentUserFromDB.getEmail(),
               currentUserFromDB.getName());

         resLoginDTO.setUserLogin(userLogin);
      }

      // tao access token
      String access_token = this.securityUtil.createAccessToken(loginDTO.getUsername(), resLoginDTO.getUserLogin());
      resLoginDTO.setAccessToken(access_token);

      // create refreshToken
      String refreshToken = this.securityUtil.createRefreshToken(loginDTO.getUsername(), resLoginDTO);
      this.userService.updateUserToken(refreshToken, loginDTO.getUsername());

      // set cookie
      ResponseCookie responseCookie = ResponseCookie.from("refresh_token", refreshToken)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(this.refreshTokenExpiration)
            .build();
      return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(resLoginDTO);
   }

   @GetMapping("/auth/account")
   @ApiMessage("Get User Information")
   public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {
      String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
      User currentUserFromDB = this.userService.handleGetUserByUserName(email);
      ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
      ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount();
      if (currentUserFromDB != null) {
         userLogin.setId(currentUserFromDB.getId());
         userLogin.setEmail(currentUserFromDB.getEmail());
         userLogin.setName(currentUserFromDB.getName());
         userGetAccount.setUser(userLogin);
      }
      return ResponseEntity.ok().body(userGetAccount);
   }

   @GetMapping("auth/refresh")
   @ApiMessage(value = "Get user by refresh token")
   public ResponseEntity<ResLoginDTO> getRefreshToken(
         @CookieValue(name = "refresh_token", defaultValue = "abc") String refreshToken)
         throws IdInvalidException {

      if (refreshToken.equals("abc")) {
         throw new IdInvalidException("Bạn không truyền lên Refresh Token");
      }

      // check valid token
      Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refreshToken);
      String email = decodedToken.getSubject();

      // check user and refresh token
      User currentUserFromDB = this.userService.getUserByRefreshTokenAndEmail(refreshToken, email);
      if (currentUserFromDB == null) {
         throw new IdInvalidException("Refresh Token không hợp lệ");
      }

      ResLoginDTO resLoginDTO = new ResLoginDTO(); // cần trả ra đối tượng, nếu trả ra String thì bị lỗi không thể cast
                                                   // String - > Object

      if (currentUserFromDB != null) {
         ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
               currentUserFromDB.getId(),
               currentUserFromDB.getEmail(),
               currentUserFromDB.getName());

         resLoginDTO.setUserLogin(userLogin);
      }

      // tao access token
      String access_token = this.securityUtil.createAccessToken(email, resLoginDTO.getUserLogin());
      resLoginDTO.setAccessToken(access_token);

      // create refreshToken
      String newRefreshToken = this.securityUtil.createRefreshToken(email, resLoginDTO);
      this.userService.updateUserToken(newRefreshToken, email);

      // set cookie
      ResponseCookie responseCookie = ResponseCookie.from("refresh_token", newRefreshToken)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(this.refreshTokenExpiration)
            .build();

      return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(resLoginDTO);

   }

   @GetMapping("/auth/logout")
   @ApiMessage("Logout")
   public ResponseEntity<Void> logout() throws IdInvalidException {
      String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";

      if (email == null) {
         throw new IdInvalidException("Bạn không truyền lên Refresh Token");
      }

      this.userService.updateUserToken(null, email);

      // remove cookie refresh token
      ResponseCookie responseCookie = ResponseCookie.from("refresh_token", "")
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(0)
            .build();
      return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString())
            .body(null);
   }
}
