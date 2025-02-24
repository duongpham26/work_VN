package com.duongpham26.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.duongpham26.demo.entity.User;
import com.duongpham26.demo.entity.dto.ResultPaginationDTO;
import com.duongpham26.demo.service.UserService;
import com.duongpham26.demo.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class UserController {

   private final UserService userService;

   public UserController(UserService userService) {
      this.userService = userService;
   }

   @PostMapping("/user/create")
   public ResponseEntity<User> createNewUser(@RequestBody User newUser) {
      User user = this.userService.handleCreateUser(newUser);
      return ResponseEntity.status(HttpStatus.CREATED).body(user);
   }

   @DeleteMapping("/user/delete/{id}")
   public ResponseEntity<String> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
      if (id < 0 || id > 1500) {
         throw new IdInvalidException("Id khong lon hon 1500");
      }
      this.userService.handleDeleteUser(id);
      return ResponseEntity.ok("Delete Success " + id);
   }

   @GetMapping("/user/get-user/{id}")
   public ResponseEntity<User> getUser(@PathVariable("id") long id) {
      User user = this.userService.handleGetUser(id);
      return ResponseEntity.status(HttpStatus.OK).body(user);
   }

   @GetMapping("/user/get-all-user")
   public ResponseEntity<ResultPaginationDTO> getAllUsers(
         @Filter Specification<User> spec,
         Pageable pageable) {

      // String stringCurrent = currentOptional.isPresent() ? currentOptional.get() :
      // "";
      // String stringPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get()
      // : "";

      // Pageable pageable = PageRequest.of(Integer.parseInt(stringCurrent) - 1,
      // Integer.parseInt(stringPageSize));

      return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleGetAllUser(spec, pageable));
   }

   @PutMapping("/user")
   public ResponseEntity<User> updateUser(@RequestBody User userUpdate) {
      return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleUpdateUser(userUpdate));
   }
}
