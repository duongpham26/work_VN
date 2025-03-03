package com.duongpham26.demo.entity.dto.response;

import com.duongpham26.demo.entity.Role;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ResLoginDTO {

   @JsonProperty("access_token")
   private String accessToken;

   private UserLogin userLogin;

   @Getter
   @Setter
   @NoArgsConstructor
   @AllArgsConstructor
   public static class UserLogin {
      private long id;
      private String email;
      private String name;
      private Role role;
   }

   @Getter
   @Setter
   @NoArgsConstructor
   @AllArgsConstructor
   public static class UserGetAccount {
      private UserLogin user;
   }

   @Getter
   @Setter
   @NoArgsConstructor
   @AllArgsConstructor
   public static class UserInSideToken {
      private long id;
      private String email;
      private String name;
   }

}
