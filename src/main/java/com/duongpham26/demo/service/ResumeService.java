package com.duongpham26.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.duongpham26.demo.entity.Company;
import com.duongpham26.demo.entity.Job;
import com.duongpham26.demo.entity.Resume;
import com.duongpham26.demo.entity.User;
import com.duongpham26.demo.entity.dto.response.ResResultPaginationDTO;
import com.duongpham26.demo.entity.dto.response.resume.ResCreateResumeDTO;
import com.duongpham26.demo.entity.dto.response.resume.ResFetchResumeDTO;
import com.duongpham26.demo.entity.dto.response.resume.ResUpdateResumeDTO;
import com.duongpham26.demo.repository.JobRepository;
import com.duongpham26.demo.repository.ResumeRepository;
import com.duongpham26.demo.repository.UserRepository;
import com.duongpham26.demo.util.SecurityUtil;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;

@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;

    private final JobRepository jobRepository;

    private final UserRepository userRepository;

    private final FilterParser filterParser;

    // private final FilterBuilder filterBuilder;

    private final FilterSpecificationConverter filterSpecificationConverter;

    public ResumeService(ResumeRepository resumeRepository, JobRepository jobRepository,
            UserRepository userRepository, FilterParser filterParser,
            FilterSpecificationConverter filterSpecificationConverter,
            FilterBuilder filterBuilder) {
        this.resumeRepository = resumeRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.filterParser = filterParser;
        this.filterSpecificationConverter = filterSpecificationConverter;
        // this.filterBuilder = filterBuilder;
    }

    public boolean checkResumeExistByUseAndJob(Resume resume) {
        if (resume.getUser() == null) {
            return false;
        }
        Optional<User> user = this.userRepository.findById(resume.getUser().getId());
        if (!user.isPresent()) {
            return false;
        }

        if (resume.getJob() == null) {
            return false;
        }
        Optional<Job> job = this.jobRepository.findById(resume.getJob().getId());
        if (!job.isPresent()) {
            return false;
        }

        return true;

    }

    public ResCreateResumeDTO createResume(Resume resume) {
        Resume newResume = this.resumeRepository.save(resume);
        ResCreateResumeDTO resCreateResumeDTO = new ResCreateResumeDTO();
        resCreateResumeDTO.setId(newResume.getId());
        resCreateResumeDTO.setCreatedAt(newResume.getCreatedAt());
        resCreateResumeDTO.setCreatedBy(newResume.getCreatedBy());
        return resCreateResumeDTO;
    }

    public Optional<Resume> getResumeById(Long id) {
        return this.resumeRepository.findById(id);
    }

    public ResUpdateResumeDTO updateResume(Resume resume) {

        Resume newResume = this.resumeRepository.save(resume);
        ResUpdateResumeDTO resUpdateResumeDTO = new ResUpdateResumeDTO();
        resUpdateResumeDTO.setUpdatedAt(newResume.getUpdatedAt());
        resUpdateResumeDTO.setUpdatedBy(newResume.getUpdatedBy());

        return resUpdateResumeDTO;
    }

    public void deleteResumeById(Long id) {
        this.resumeRepository.deleteById(id);
    }

    public ResFetchResumeDTO getResume(Resume resume) {
        ResFetchResumeDTO resFetchResumeDTO = new ResFetchResumeDTO();
        resFetchResumeDTO.setId(resume.getId());
        resFetchResumeDTO.setName(resume.getName());
        resFetchResumeDTO.setUrl(resume.getUrl());
        resFetchResumeDTO.setStatus(resume.getStatus());
        resFetchResumeDTO.setCreatedAt(resume.getCreatedAt());
        resFetchResumeDTO.setCreatedBy(resume.getCreatedBy());
        resFetchResumeDTO.setUpdatedAt(resume.getUpdatedAt());
        resFetchResumeDTO.setUpdatedBy(resume.getUpdatedBy());

        if (resume.getJob() != null) {
            resFetchResumeDTO.setCompanyName(resume.getJob().getCompany().getName());
        }

        resFetchResumeDTO
                .setUser(new ResFetchResumeDTO.UserResume(resume.getUser().getId(), resume.getUser().getName()));
        resFetchResumeDTO.setJob(new ResFetchResumeDTO.JobResume(resume.getJob().getId(), resume.getJob().getName()));

        return resFetchResumeDTO;
    }

    public ResResultPaginationDTO fetchAllResume(Specification<Resume> specification, Pageable pageable) {

        List<Long> arrJobIds = null;

        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        // HR
        User currentUser = this.userRepository.findByEmail(email);

        if (currentUser != null) {
            Company userCompany = currentUser.getCompany();
            if (userCompany != null) {
                List<Job> companyJobs = userCompany.getJobs();
                if (companyJobs != null && companyJobs.size() > 0) {
                    arrJobIds = companyJobs.stream().map(companyJob -> companyJob.getId()).collect(Collectors.toList());
                }
            }
        }

        // filter
        // @SuppressWarnings("null")
        // Specification<Resume> jobInSpec = filterSpecificationConverter
        // .convert(filterBuilder.field("job").in(filterBuilder.input(arrJobIds)).get());
        Specification<Resume> jobInSpec = ResumeSpecificationService.getJobWithListIds(arrJobIds);

        Specification<Resume> finalSpec = jobInSpec.and(specification);
        Page<Resume> page = this.resumeRepository.findAll(finalSpec, pageable);

        ResResultPaginationDTO resResultPaginationDTO = new ResResultPaginationDTO();
        ResResultPaginationDTO.Meta meta = new ResResultPaginationDTO.Meta();

        meta.setPage(page.getNumber() + 1); // +1 vì spring tự trừ đi 1 khi cấu hình start index = 1
        meta.setPageSize(page.getSize());
        meta.setTotal(page.getTotalElements());
        meta.setPages(page.getTotalPages());
        resResultPaginationDTO.setMeta(meta);

        // remove sensitive data
        List<ResFetchResumeDTO> listResumes = page.getContent().stream().map(item -> this.getResume(item))
                .collect(Collectors.toList());
        resResultPaginationDTO.setResult(listResumes);

        return resResultPaginationDTO;
    }

    public ResResultPaginationDTO fetchResumesByUser(Pageable pageable) {
        // query builder
        Optional<String> userInfo = SecurityUtil.getCurrentUserLogin();
        String email = userInfo.isPresent() ? userInfo.get() : "";
        FilterNode node = filterParser.parse("email='" + email + "'");
        FilterSpecification<Resume> specification = filterSpecificationConverter.convert(node);
        Page<Resume> page = this.resumeRepository.findAll(specification, pageable);

        ResResultPaginationDTO resResultPaginationDTO = new ResResultPaginationDTO();
        ResResultPaginationDTO.Meta meta = new ResResultPaginationDTO.Meta();

        meta.setPage(page.getNumber() + 1); // +1 vì spring tự trừ đi 1 khi cấu hình start index = 1
        meta.setPageSize(page.getSize());
        meta.setTotal(page.getTotalElements());
        meta.setPages(page.getTotalPages());
        resResultPaginationDTO.setMeta(meta);

        // remove sensitive data
        List<ResFetchResumeDTO> listResumes = page.getContent().stream().map(item -> this.getResume(item))
                .collect(Collectors.toList());

        resResultPaginationDTO.setResult(listResumes);

        return resResultPaginationDTO;
    }
}
