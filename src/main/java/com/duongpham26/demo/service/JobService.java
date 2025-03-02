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
import com.duongpham26.demo.entity.Skill;
import com.duongpham26.demo.entity.dto.response.ResResultPaginationDTO;
import com.duongpham26.demo.entity.dto.response.job.ResCreateJobDTO;
import com.duongpham26.demo.entity.dto.response.job.ResUpdateJobDTO;
import com.duongpham26.demo.repository.CompanyRepository;
import com.duongpham26.demo.repository.JobRepository;
import com.duongpham26.demo.repository.SkillRepository;

@Service
public class JobService {
    private final JobRepository jobRepository;

    private final SkillRepository skillRepository;

    private final CompanyRepository companyRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository,
            CompanyRepository companyRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.companyRepository = companyRepository;
    }

    public ResCreateJobDTO createJob(Job job) {
        // check skills
        if (job.getSkills() != null) {
            List<Long> reqSkills = job.getSkills().stream().map(skill -> skill.getId()).collect(Collectors.toList());
            List<Skill> skills = this.skillRepository.findByIdIn(reqSkills);
            job.setSkills(skills);
        }

        // check company
        if (job.getCompany() != null) {
            Optional<Company> companyOptional = this.companyRepository.findById(job.getCompany().getId());
            if (companyOptional.isPresent()) {
                job.setCompany(companyOptional.get());
            }
        }

        // create job
        Job currentJob = this.jobRepository.save(job);

        // convert response
        ResCreateJobDTO resJobCreateDTO = new ResCreateJobDTO();
        resJobCreateDTO.setName(currentJob.getName());
        resJobCreateDTO.setLocation(currentJob.getLocation());
        resJobCreateDTO.setSalary(currentJob.getSalary());
        resJobCreateDTO.setQuantity(currentJob.getQuantity());
        resJobCreateDTO.setDescription(currentJob.getDescription());
        resJobCreateDTO.setLevel(currentJob.getLevel());
        resJobCreateDTO.setStartDate(currentJob.getStartDate());
        resJobCreateDTO.setEndDate(currentJob.getEndDate());
        resJobCreateDTO.setActive(currentJob.isActive());
        resJobCreateDTO.setCreatedAt(currentJob.getCreatedAt());
        resJobCreateDTO.setCreateBy(currentJob.getCreatedBy());

        if (currentJob.getSkills() != null) {
            List<String> skills = currentJob.getSkills().stream().map(skill -> skill.getName())
                    .collect(Collectors.toList());
            resJobCreateDTO.setSkills(skills);
        }

        return resJobCreateDTO;
    }

    public ResUpdateJobDTO updateJob(Job job, Job jobUpdate) {

        // check skills
        if (job.getSkills() != null) {
            List<Long> reqSkills = job.getSkills().stream().map(skill -> skill.getId()).collect(Collectors.toList());
            List<Skill> skills = this.skillRepository.findByIdIn(reqSkills);
            jobUpdate.setSkills(skills);
        }

        // check company
        if (job.getCompany() != null) {
            Optional<Company> companyOptional = this.companyRepository.findById(job.getCompany().getId());
            if (companyOptional.isPresent()) {
                jobUpdate.setCompany(companyOptional.get());
            }
        }

        // update correct info
        jobUpdate.setName(job.getName());
        jobUpdate.setLocation(job.getLocation());
        jobUpdate.setSalary(job.getSalary());
        jobUpdate.setQuantity(job.getQuantity());
        jobUpdate.setLevel(job.getLevel());
        jobUpdate.setStartDate(job.getStartDate());
        jobUpdate.setEndDate(job.getEndDate());
        jobUpdate.setActive(job.isActive());

        // create job
        Job currentJob = this.jobRepository.save(jobUpdate);

        // convert response
        ResUpdateJobDTO resUpdateJobDTO = new ResUpdateJobDTO();
        resUpdateJobDTO.setId(currentJob.getId());
        resUpdateJobDTO.setName(currentJob.getName());
        resUpdateJobDTO.setLocation(currentJob.getLocation());
        resUpdateJobDTO.setSalary(currentJob.getSalary());
        resUpdateJobDTO.setQuantity(currentJob.getQuantity());
        resUpdateJobDTO.setDescription(currentJob.getDescription());
        resUpdateJobDTO.setLevel(currentJob.getLevel());
        resUpdateJobDTO.setStartDate(currentJob.getStartDate());
        resUpdateJobDTO.setEndDate(currentJob.getEndDate());
        resUpdateJobDTO.setActive(currentJob.isActive());
        resUpdateJobDTO.setUpdatedAt(currentJob.getUpdatedAt());
        resUpdateJobDTO.setUpdateBy(currentJob.getUpdatedBy());

        if (currentJob.getSkills() != null) {
            List<String> skills = currentJob.getSkills().stream().map(skill -> skill.getName())
                    .collect(Collectors.toList());
            resUpdateJobDTO.setSkills(skills);
        }

        return resUpdateJobDTO;
    }

    public Optional<Job> getJobById(long id) {
        return this.jobRepository.findById(id);
    }

    public void deleteJob(long id) {
        this.jobRepository.deleteById(id);
    }

    public ResResultPaginationDTO fetchAll(Specification<Job> specification, Pageable pageable) {
        Page<Job> page = this.jobRepository.findAll(specification, pageable);

        ResResultPaginationDTO resResultPaginationDTO = new ResResultPaginationDTO();
        ResResultPaginationDTO.Meta meta = new ResResultPaginationDTO.Meta();

        meta.setPage(page.getNumber() + 1); // +1 vì spring tự trừ đi 1 khi cấu hình start index = 1
        meta.setPageSize(page.getSize());
        meta.setTotal(page.getTotalElements());
        meta.setPages(page.getTotalPages());

        resResultPaginationDTO.setMeta(meta);

        List<Job> listJob = page.getContent();
        resResultPaginationDTO.setResult(listJob);

        return resResultPaginationDTO;

    }
}
