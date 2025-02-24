package com.duongpham26.demo.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

   private Instant createdAt; // sử dụng life cycle của spring để cập nhậtnhật

   private Instant updatedAt;

   private String createdBy;

   private String updatedBy;
}
