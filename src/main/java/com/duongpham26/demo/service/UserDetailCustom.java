package com.duongpham26.demo.service;

import java.util.Collections;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Component("userDetailsService")
public class UserDetailCustom implements UserDetailsService {

   private UserService userService;

   public UserDetailCustom(UserService userService) {
      this.userService = userService;
   }

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {    
      com.duongpham26.demo.entity.User user = this.userService.handleGetUserByUserName(username);
      if(user == null) {
         throw new UsernameNotFoundException("Username/password không hợp lệ"); 
      }
      return new User(
         user.getEmail(), 
         user.getPassword(), 
         Collections.singleton(new SimpleGrantedAuthority("USER_ROLE"))
      );
   }
}
