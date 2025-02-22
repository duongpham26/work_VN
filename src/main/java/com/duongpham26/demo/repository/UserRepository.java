package com.duongpham26.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duongpham26.demo.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{

   User findByEmail(String email);
} 
