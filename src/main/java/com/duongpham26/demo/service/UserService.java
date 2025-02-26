package com.duongpham26.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.duongpham26.demo.entity.User;
import com.duongpham26.demo.entity.dto.Meta;
import com.duongpham26.demo.entity.dto.ResultPaginationDTO;
import com.duongpham26.demo.entity.dto.response.CreateUserDTO;
import com.duongpham26.demo.entity.dto.response.ResUpdateUserDTO;
import com.duongpham26.demo.entity.dto.response.ResUserDTO;
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

   public ResUserDTO handleGetUser(long id) throws IdInvalidException {
      Optional<User> userOptional = this.userRepository.findById(id);
      if (!userOptional.isPresent()) {
         throw new IdInvalidException("user với " + id + " không tồn tại.");
      }
      User user = userOptional.get();
      ResUserDTO resUserDTO = convertToResUserDTO(user);
      return resUserDTO;
   }

   public ResUserDTO convertToResUserDTO(User user) {
      ResUserDTO resUserDTO = new ResUserDTO();
      resUserDTO.setId(user.getId());
      resUserDTO.setEmail(user.getEmail());
      resUserDTO.setName(user.getName());
      resUserDTO.setAge(user.getAge());
      resUserDTO.setCreatedAt(user.getCreatedAt());
      resUserDTO.setGender(user.getGender());
      resUserDTO.setAddress(user.getAddress());
      return resUserDTO;
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

      List<ResUserDTO> listUser = pageUser.getContent().stream().map(user -> new ResUserDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getGender(),
            user.getAddress(),
            user.getAge(),
            user.getCreatedAt(),
            user.getUpdatedAt())).collect(Collectors.toList());
      resultPaginationDTO.setResult(listUser);

      return resultPaginationDTO;
   }

   public ResUpdateUserDTO handleUpdateUser(User user) throws IdInvalidException {
      Optional<User> currentUserOptional = this.userRepository.findById(user.getId());
      if (!currentUserOptional.isPresent()) {
         throw new IdInvalidException("User với id = " + user.getId() + " không tồn tại.");
      }
      User currentUser = currentUserOptional.get();
      currentUser.setName(user.getName());
      currentUser.setAge(user.getAge());
      currentUser.setAddress(user.getAddress());
      currentUser.setGender(user.getGender());
      currentUser = this.userRepository.save(currentUser);

      return convertResUpdateUserDTO(currentUser);
   }

   public User handleGetUserByUserName(String username) {
      return this.userRepository.findByEmail(username);
   }

   public ResUpdateUserDTO convertResUpdateUserDTO(User user) {
      ResUpdateUserDTO resUpdateUserDTO = new ResUpdateUserDTO();
      resUpdateUserDTO.setName(user.getName());
      resUpdateUserDTO.setAddress(user.getAddress());
      resUpdateUserDTO.setGender(user.getGender());
      resUpdateUserDTO.setAge(user.getAge());
      resUpdateUserDTO.setId(user.getId());
      resUpdateUserDTO.setUpdatedAt(user.getUpdatedAt());
      return resUpdateUserDTO;
   }

   public void updateUserToken(String token, String email) {
      User currentUser = this.userRepository.findByEmail(email);

      if (currentUser != null) {
         currentUser.setRefreshToken(token);
         this.userRepository.save(currentUser);
      }
   }
}
