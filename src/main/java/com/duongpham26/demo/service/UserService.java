package com.duongpham26.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.duongpham26.demo.entity.User;
import com.duongpham26.demo.repository.UserRepository;

@Service
public class UserService {
   private PasswordEncoder passwordEncoder;

   private UserRepository userRepository;

   public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
      this.userRepository = userRepository;
      this.passwordEncoder = passwordEncoder;
   }

   public User handleCreateUser(User user) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      return this.userRepository.save(user);
   }

   public void handleDeleteUser(long id) {
      this.userRepository.deleteById(id);
   }  

   public User handleGetUser(long id) {
      Optional<User> userOptional = this.userRepository.findById(id);
      if(userOptional.isPresent()) {
         return userOptional.get();
      } else {
         return null;
      }
   }

   public List<User> handleGetAllUser() {
      return this.userRepository.findAll();
   }

   public User handleUpdateUser(User user) {
      User currentUser = this.handleGetUser(user.getId());
      if(currentUser != null) {
         currentUser.setEmail(user.getEmail());
         currentUser.setName(user.getName());
         currentUser.setPassword(user.getPassword());
        currentUser = this.userRepository.save(currentUser);
      }
      return currentUser;
   }

   public User handleGetUserByUserName(String username) {
      return this.userRepository.findByEmail(username);
   }
}
