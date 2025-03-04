package com.duongpham26.demo.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.duongpham26.demo.entity.Resume;

public class ResumerSpecificationService {
    public static Specification<Resume> getJobWithListIds(List<Long> listJobIds) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("job").get("id")).value(listJobIds);
    }
}
