package com.duongpham26.demo.service;

import org.springframework.stereotype.Service;

import com.duongpham26.demo.entity.Company;
import com.duongpham26.demo.repository.CompanyRepository;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleCreateCompany(Company company) {
        return this.companyRepository.save(company);
    }
}
