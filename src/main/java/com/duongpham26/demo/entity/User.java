package com.duongpham26.demo.entity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.duongpham26.demo.util.SecurityUtil;
import com.duongpham26.demo.util.constant.GenderEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;

   private String name;

   @NotBlank(message = "email không được để trống")
   private String email;

   @NotBlank(message = "password không được để trống")
   private String password;
   private int age;

   @Enumerated(EnumType.STRING) // lưu enum vào database kiểu string
   private GenderEnum gender;

   private String address;

   @Column(columnDefinition = "MEDIUMTEXT")
   private String refreshToken;

   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Bangkok")
   private Instant createdAt;

   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Bangkok")
   private Instant updatedAt;

   private String createdBy;

   private String updatedBy;

   @ManyToOne
   @JoinColumn(name = "company_id")
   private Company company;

   @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
   @JsonIgnore
   private List<Resume> resumes;

   @PrePersist // callback method
   public void handleBeforeCreate() {
      Optional<String> userInfo = SecurityUtil.getCurrentUserLogin();
      this.createdBy = userInfo.isPresent() ? userInfo.get() : "";
      this.createdAt = Instant.now(); // lưu theo chuẩn ISO đã cấu hình
      // Lỗi mối giới mặc định lưu là múi giờ 0; => chỉnh thành múi giờ 7 (VN)
   }

   @PreUpdate
   public void handleBeforeUpdate() {
      Optional<String> userInfo = SecurityUtil.getCurrentUserLogin();
      this.updatedBy = userInfo.isPresent() ? userInfo.get() : "";
      this.updatedAt = Instant.now();
   }
}
