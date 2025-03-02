package com.duongpham26.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duongpham26.demo.entity.Resume;
import com.duongpham26.demo.entity.dto.response.ResResultPaginationDTO;
import com.duongpham26.demo.entity.dto.response.resume.ResCreateResumeDTO;
import com.duongpham26.demo.entity.dto.response.resume.ResFetchResumeDTO;
import com.duongpham26.demo.entity.dto.response.resume.ResUpdateResumeDTO;
import com.duongpham26.demo.service.ResumeService;
import com.duongpham26.demo.util.annotation.ApiMessage;
import com.duongpham26.demo.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import io.micrometer.core.ipc.http.HttpSender.Response;
import jakarta.validation.Valid;

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

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/resumes")
    @ApiMessage("Create resume")
    public ResponseEntity<ResCreateResumeDTO> createResume(@Valid @RequestBody Resume resume)
            throws IdInvalidException {
        // check id exist with job and user
        boolean isIdExist = this.resumeService.checkResumeExistByUseAndJob(resume);
        if (!isIdExist) {
            throw new IdInvalidException("User id / Job id not exist");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.createResume(resume));

    }

    @PutMapping("/resumes")
    @ApiMessage("Update resume")
    public ResponseEntity<ResUpdateResumeDTO> updateResume(@Valid @RequestBody Resume resume)
            throws IdInvalidException {
        // check id exist with job and user
        Optional<Resume> resumeOptional = this.resumeService.getResumeById(resume.getId());
        if (!resumeOptional.isPresent()) {
            throw new IdInvalidException("Resume id=" + resume.getId() + " not exist");
        }

        Resume reqResume = resumeOptional.get();
        reqResume.setStatus(resume.getStatus());

        return ResponseEntity.status(HttpStatus.OK).body(this.resumeService.updateResume(reqResume));

    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("Get resume by id")
    public ResponseEntity<ResFetchResumeDTO> fetchById(@PathVariable Long id) throws IdInvalidException {
        Optional<Resume> resume = this.resumeService.getResumeById(id);

        if (!resume.isPresent()) {
            throw new IdInvalidException("Resume with id " + id + " not found");
        }

        return ResponseEntity.ok().body(this.resumeService.getResume(resume.get()));
    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("Delete resume by id")
    public ResponseEntity<Response> deleteById(@PathVariable Long id) throws IdInvalidException {
        Optional<Resume> resume = this.resumeService.getResumeById(id);

        if (!resume.isPresent()) {
            throw new IdInvalidException("Resume with id " + id + " not found");
        }

        this.resumeService.deleteResumeById(id);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/resumes")
    @ApiMessage("Get all resumes with pagination")
    public ResponseEntity<ResResultPaginationDTO> getAllJobs(@Filter Specification<Resume> specification,
            Pageable pageable) {

        return ResponseEntity.ok().body(this.resumeService.fetchAllResume(specification, pageable));

    }
}
