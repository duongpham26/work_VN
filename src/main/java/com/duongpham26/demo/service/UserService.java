package com.duongpham26.demo.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.duongpham26.demo.entity.User;
import com.duongpham26.demo.entity.dto.Meta;
import com.duongpham26.demo.entity.dto.ResultPaginationDTO;
import com.duongpham26.demo.entity.dto.response.CreateUserDTO;
import com.duongpham26.demo.repository.UserRepository;
import com.duongpham26.demo.util.error.IdInvalidException;

@Service
public class UserService {
   private PasswordEncoder passwordEncoder;

   private UserRepository userRepository;

   public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
      this.userRepository = userRepository;
      this.passwordEncoder = passwordEncoder;
   }

   public CreateUserDTO handleCreateUser(User user) throws IdInvalidException {
      boolean isEmailExist = this.userRepository.existsByEmail(user.getEmail());
      if (isEmailExist) {
         throw new IdInvalidException("Email " + user.getEmail() + " đã tồn tại");
      }
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      User createdUser = this.userRepository.save(user);
      CreateUserDTO createUserDTO = convertToCreateUserDTO(createdUser);
      return createUserDTO;
   }

   public CreateUserDTO convertToCreateUserDTO(User user) {
      CreateUserDTO createUserDTO = new CreateUserDTO();
      createUserDTO.setId(user.getId());
      createUserDTO.setEmail(user.getEmail());
      createUserDTO.setName(user.getName());
      createUserDTO.setAge(user.getAge());
      createUserDTO.setCreatedAt(user.getCreatedAt());
      createUserDTO.setGender(user.getGender());
      createUserDTO.setAddress(user.getAddress());
      return createUserDTO;
   }

   public void handleDeleteUser(long id) {
      this.userRepository.deleteById(id);
   }

   public User handleGetUser(long id) {
      Optional<User> userOptional = this.userRepository.findById(id);
      if (userOptional.isPresent()) {
         return userOptional.get();
      } else {
         return null;
      }
   }

   public ResultPaginationDTO handleGetAllUser(Specification<User> spec, Pageable pageable) {
      Page<User> pageUser = this.userRepository.findAll(spec, pageable);

      ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
      Meta meta = new Meta();

      meta.setPage(pageUser.getNumber() + 1); // +1 vì spring tự trừ đi 1 khi cấu hình start index = 1
      meta.setPageSize(pageUser.getSize());
      meta.setTotal(pageUser.getTotalElements());
      meta.setPages(pageUser.getTotalPages());

      resultPaginationDTO.setMeta(meta);
      resultPaginationDTO.setResult(pageUser.getContent());

      return resultPaginationDTO;
   }

   public User handleUpdateUser(User user) {
      User currentUser = this.handleGetUser(user.getId());
      if (currentUser != null) {
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
