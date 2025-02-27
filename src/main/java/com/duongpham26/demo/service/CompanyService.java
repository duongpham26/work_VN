package com.duongpham26.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.duongpham26.demo.entity.Company;
import com.duongpham26.demo.entity.User;
import com.duongpham26.demo.entity.dto.response.ResResultPaginationDTO;
import com.duongpham26.demo.repository.CompanyRepository;
import com.duongpham26.demo.repository.UserRepository;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public Company handleCreateCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public ResResultPaginationDTO handleGetAllCompany(Specification<Company> spec, Pageable pageable) {
        Page<Company> page = this.companyRepository.findAll(spec, pageable);

        ResResultPaginationDTO.Meta meta = new ResResultPaginationDTO.Meta();
        meta.setPage(page.getNumber() + 1);
        meta.setPageSize(page.getSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());

        ResResultPaginationDTO resultPaginationDTO = new ResResultPaginationDTO();
        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(page.getContent());

        return resultPaginationDTO;
    }

    public void handleDeleteOneById(long id) {
        Optional<Company> companyOptional = this.companyRepository.findById(id);
        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            // fetch user belong to company
            List<User> users = this.userRepository.findByCompany(company);
            this.userRepository.deleteAll(users);
        }

        this.companyRepository.deleteById(id);
    }

    public Company handleUpdateCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public Optional<Company> findById(long id) {
        return this.companyRepository.findById(id);
    }
}
