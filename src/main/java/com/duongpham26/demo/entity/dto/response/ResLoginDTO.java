package com.duongpham26.demo.entity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ResLoginDTO {

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
   }

}
