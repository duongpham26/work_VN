package com.duongpham26.demo.entity;

import java.time.Instant;
import java.util.Optional;

import com.duongpham26.demo.util.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "companies")
@Setter
@Getter // date => tự động generate toString,...
public class Company {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private long id;

   @NotEmpty(message = "name không được để trống")
   private String name;

   @Column(columnDefinition = "MEDIUMTEXT")
   private String description;

   private String address;

   private String logo;

   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Bangkok")
   private Instant createdAt; // sử dụng life cycle của spring để cập nhậtnhật

   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Bangkok")
   private Instant updatedAt;

   private String createdBy;

   private String updatedBy;

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
