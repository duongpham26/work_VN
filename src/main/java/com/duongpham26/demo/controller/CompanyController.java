package com.duongpham26.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.duongpham26.demo.entity.Company;
import com.duongpham26.demo.service.CompanyService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createNewCompany(@Valid @RequestBody Company newCompany) {

        Company company = this.companyService.handleCreateCompany(newCompany);
        return ResponseEntity.status(HttpStatus.CREATED).body(company);

    }

}
