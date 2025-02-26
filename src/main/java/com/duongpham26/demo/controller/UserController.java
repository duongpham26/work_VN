package com.duongpham26.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.duongpham26.demo.entity.User;
import com.duongpham26.demo.entity.dto.response.ResCreateUserDTO;
import com.duongpham26.demo.entity.dto.response.ResResultPaginationDTO;
import com.duongpham26.demo.entity.dto.response.ResUpdateUserDTO;
import com.duongpham26.demo.entity.dto.response.ResUserDTO;
import com.duongpham26.demo.service.UserService;
import com.duongpham26.demo.util.annotation.ApiMessage;
import com.duongpham26.demo.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class UserController {

   private final UserService userService;

   public UserController(UserService userService) {
      this.userService = userService;
   }

   @PostMapping("/user/create")
   @ApiMessage("Create a new user")
   public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User newUser) throws IdInvalidException {
      ResCreateUserDTO user = this.userService.handleCreateUser(newUser);
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
   @ApiMessage(value = "Fetch user by id")
   public ResponseEntity<ResUserDTO> getUser(@PathVariable("id") long id) throws IdInvalidException {
      ResUserDTO user = this.userService.handleGetUser(id);
      return ResponseEntity.status(HttpStatus.OK).body(user);
   }

   @GetMapping("/user/get-all-user")
   @ApiMessage(value = "Fetch all users")
   public ResponseEntity<ResResultPaginationDTO> getAllUsers(
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
   @ApiMessage(value = "update user")
   public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User userUpdate) throws IdInvalidException {
      return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleUpdateUser(userUpdate));
   }
}
