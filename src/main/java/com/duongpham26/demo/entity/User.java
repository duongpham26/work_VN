package com.duongpham26.demo.entity;

import java.time.Instant;

import com.duongpham26.demo.util.annotation.GenderEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private long id;

   private String name;
   private String email;
   private String password;
   private int age;

   @Enumerated(EnumType.STRING) // lưu enum vào database kiểu string
   private GenderEnum gender;
   private String address;
   private String refreshToken;
   private Instant createdAt;
   private Instant updatedAt;
   private String createdBy;
   private String updatedBy;
}
