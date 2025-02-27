package com.duongpham26.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.duongpham26.demo.entity.Company;
import com.duongpham26.demo.entity.User;
import com.duongpham26.demo.entity.dto.response.ResCreateUserDTO;
import com.duongpham26.demo.entity.dto.response.ResResultPaginationDTO;
import com.duongpham26.demo.entity.dto.response.ResUpdateUserDTO;
import com.duongpham26.demo.entity.dto.response.ResUserDTO;
import com.duongpham26.demo.repository.UserRepository;
import com.duongpham26.demo.util.error.IdInvalidException;

@Service
public class UserService {
   private PasswordEncoder passwordEncoder;

   private UserRepository userRepository;

   private CompanyService companyService;

   public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CompanyService companyService) {
      this.userRepository = userRepository;
      this.passwordEncoder = passwordEncoder;
      this.companyService = companyService;
   }

   public ResCreateUserDTO handleCreateUser(User user) throws IdInvalidException {
      boolean isEmailExist = this.userRepository.existsByEmail(user.getEmail());
      if (isEmailExist) {
         throw new IdInvalidException("Email " + user.getEmail() + " đã tồn tại");
      }

      // check company
      if (user.getCompany() != null) {
         Optional<Company> companyOptional = this.companyService.findById(user.getCompany().getId());
         user.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);
      }

      user.setPassword(passwordEncoder.encode(user.getPassword()));
      User createdUser = this.userRepository.save(user);
      ResCreateUserDTO createUserDTO = convertToCreateUserDTO(createdUser);
      return createUserDTO;
   }

   public ResCreateUserDTO convertToCreateUserDTO(User user) {
      ResCreateUserDTO createUserDTO = new ResCreateUserDTO();
      ResCreateUserDTO.CompanyUser companyUser = new ResCreateUserDTO.CompanyUser();
      createUserDTO.setId(user.getId());
      createUserDTO.setEmail(user.getEmail());
      createUserDTO.setName(user.getName());
      createUserDTO.setAge(user.getAge());
      createUserDTO.setCreatedAt(user.getCreatedAt());
      createUserDTO.setGender(user.getGender());
      createUserDTO.setAddress(user.getAddress());

      if (user.getCompany() != null) {
         companyUser.setId(user.getCompany().getId());
         companyUser.setName(user.getCompany().getName());
         createUserDTO.setCompany(companyUser);
      }

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
      ResUserDTO.CompanyUser companyUser = new ResUserDTO.CompanyUser();
      resUserDTO.setId(user.getId());
      resUserDTO.setEmail(user.getEmail());
      resUserDTO.setName(user.getName());
      resUserDTO.setAge(user.getAge());
      resUserDTO.setCreatedAt(user.getCreatedAt());
      resUserDTO.setGender(user.getGender());
      resUserDTO.setAddress(user.getAddress());

      if (user.getCompany() != null) {
         companyUser.setId(user.getCompany().getId());
         companyUser.setName(user.getCompany().getName());
         resUserDTO.setCompany(companyUser);
      }

      return resUserDTO;
   }

   public ResResultPaginationDTO handleGetAllUser(Specification<User> spec, Pageable pageable) {
      Page<User> pageUser = this.userRepository.findAll(spec, pageable);

      ResResultPaginationDTO resultPaginationDTO = new ResResultPaginationDTO();
      ResResultPaginationDTO.Meta meta = new ResResultPaginationDTO.Meta();

      meta.setPage(pageUser.getNumber() + 1); // +1 vì spring tự trừ đi 1 khi cấu hình start index = 1
      meta.setPageSize(pageUser.getSize());
      meta.setTotal(pageUser.getTotalElements());
      meta.setPages(pageUser.getTotalPages());

      resultPaginationDTO.setMeta(meta);

      List<ResUserDTO> listUser = pageUser.getContent().stream().map(user -> {
         ResUserDTO resUserDTO = new ResUserDTO();
         resUserDTO.setId(user.getId());
         resUserDTO.setName(user.getName());
         resUserDTO.setEmail(user.getEmail());
         resUserDTO.setGender(user.getGender());
         resUserDTO.setAddress(user.getAddress());
         resUserDTO.setAge(user.getAge());
         resUserDTO.setCreatedAt(user.getCreatedAt());
         resUserDTO.setUpdatedAt(user.getUpdatedAt());
         if (user.getCompany() != null) {
            ResUserDTO.CompanyUser companyUser = new ResUserDTO.CompanyUser();
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            resUserDTO.setCompany(companyUser);
         }
         return resUserDTO;
      }).collect(Collectors.toList());
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

      // check company
      if (user.getCompany() != null) {
         Optional<Company> companyOptional = this.companyService.findById(user.getCompany().getId());
         currentUser.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);
      }
      currentUser = this.userRepository.save(currentUser);

      return convertResUpdateUserDTO(currentUser);
   }

   public User handleGetUserByUserName(String username) {
      return this.userRepository.findByEmail(username);
   }

   public ResUpdateUserDTO convertResUpdateUserDTO(User user) {
      ResUpdateUserDTO resUpdateUserDTO = new ResUpdateUserDTO();
      ResUpdateUserDTO.CompanyUser companyUser = new ResUpdateUserDTO.CompanyUser();

      resUpdateUserDTO.setName(user.getName());
      resUpdateUserDTO.setAddress(user.getAddress());
      resUpdateUserDTO.setGender(user.getGender());
      resUpdateUserDTO.setAge(user.getAge());
      resUpdateUserDTO.setId(user.getId());
      resUpdateUserDTO.setUpdatedAt(user.getUpdatedAt());

      if (user.getCompany() != null) {
         companyUser.setId(user.getCompany().getId());
         companyUser.setName(user.getCompany().getName());
         resUpdateUserDTO.setCompany(companyUser);
      }

      return resUpdateUserDTO;
   }

   public void updateUserToken(String token, String email) {
      User currentUser = this.userRepository.findByEmail(email);

      if (currentUser != null) {
         currentUser.setRefreshToken(token);
         this.userRepository.save(currentUser);
      }
   }

   public User getUserByRefreshTokenAndEmail(String token, String email) {
      return this.userRepository.findByRefreshTokenAndEmail(token, email);
   }
}
