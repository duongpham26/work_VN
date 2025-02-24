package com.duongpham26.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.duongpham26.demo.entity.Company;
import com.duongpham26.demo.service.CompanyService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getAllCompany() {
        List<Company> companies = this.companyService.handleGetAllCompany();
        return ResponseEntity.status(HttpStatus.OK).body(companies);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<String> deleteOneCompanyById(@PathVariable("id") long id) {
        this.companyService.handleDeleteOneById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete company with id = " + id + " successfully");
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company updateCompany) {
        Company updatedCompany = this.companyService.handleUpdateCompany(updateCompany);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCompany);
    }

}
