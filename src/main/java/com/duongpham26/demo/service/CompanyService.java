package com.duongpham26.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.duongpham26.demo.entity.Company;
import com.duongpham26.demo.entity.dto.Meta;
import com.duongpham26.demo.entity.dto.ResultPaginationDTO;
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

    public ResultPaginationDTO handleGetAllCompany(Specification<Company> spec, Pageable pageable) {
        Page<Company> page = this.companyRepository.findAll(spec, pageable);

        Meta meta = new Meta();
        meta.setPage(page.getNumber() + 1);
        meta.setPageSize(page.getSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());

        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(page.getContent());

        return resultPaginationDTO;
    }

    public void handleDeleteOneById(long id) {
        this.companyRepository.deleteById(id);
    }

    public Company handleUpdateCompany(Company company) {
        return this.companyRepository.save(company);
    }
}
