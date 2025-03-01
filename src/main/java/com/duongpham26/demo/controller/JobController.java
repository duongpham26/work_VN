package com.duongpham26.demo.controller;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duongpham26.demo.entity.Job;
import com.duongpham26.demo.entity.dto.response.ResResultPaginationDTO;
import com.duongpham26.demo.entity.dto.response.job.ResCreateJobDTO;
import com.duongpham26.demo.entity.dto.response.job.ResUpdateJobDTO;
import com.duongpham26.demo.service.JobService;
import com.duongpham26.demo.util.annotation.ApiMessage;
import com.duongpham26.demo.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/job")
    @ApiMessage("Create a job")
    public ResponseEntity<ResCreateJobDTO> addJob(@Valid @RequestBody Job job) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.createJob(job));
    }

    @PutMapping("/job")
    @ApiMessage("Update a job")
    public ResponseEntity<ResUpdateJobDTO> updateJob(@Valid @RequestBody Job job) throws IdInvalidException {

        Optional<Job> jobOptional = this.jobService.getJobById(job.getId());

        if (!jobOptional.isPresent()) {
            throw new IdInvalidException("Job with id " + job.getId() + " not found");
        }
        return ResponseEntity.ok().body(this.jobService.updateJob(job));
    }

    @DeleteMapping("/job/{id}")
    @ApiMessage("Delete a job by id")
    public ResponseEntity<Void> deleteJob(@PathVariable long id) throws IdInvalidException {
        Optional<Job> currentJobOptional = this.jobService.getJobById(id);
        if (!currentJobOptional.isPresent()) {
            throw new IdInvalidException("Job with " + id + "not found");
        }
        this.jobService.deleteJob(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/job/{id}")
    @ApiMessage("Get a job by id")
    public ResponseEntity<Job> getAJob(@PathVariable long id) throws IdInvalidException {
        Optional<Job> currentJobOptional = this.jobService.getJobById(id);
        if (!currentJobOptional.isPresent()) {
            throw new IdInvalidException("Job with " + id + "not found");
        }
        return ResponseEntity.ok(currentJobOptional.get());
    }

    @GetMapping("/jobs")
    @ApiMessage("Get all jobs with pagination")
    public ResponseEntity<ResResultPaginationDTO> getAllJobs(@Filter Specification<Job> specification,
            Pageable pageable) {
        return ResponseEntity.ok().body(this.jobService.fetchAll(specification, pageable));
    }

}
