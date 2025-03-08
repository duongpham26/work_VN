package com.duongpham26.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.duongpham26.demo.entity.Subscriber;
import com.duongpham26.demo.entity.Job;
import com.duongpham26.demo.entity.Skill;
import com.duongpham26.demo.repository.SubscriberRepository;
import com.duongpham26.demo.repository.JobRepository;
import com.duongpham26.demo.repository.SkillRepository;

@Service
public class SubScriberService {

    private final SkillRepository skillRepository;

    private final SubscriberRepository subscriberRepository;

    private final JobRepository jobRepository;

    private final EmailService emailService;

    public SubScriberService(SkillRepository skillRepository, SubscriberRepository subscriberRepository,
            JobRepository jobRepository, EmailService emailService) {
        this.skillRepository = skillRepository;
        this.subscriberRepository = subscriberRepository;
        this.jobRepository = jobRepository;
        this.emailService = emailService;
    }

    public Subscriber createSubscriber(Subscriber subscriber) {
        // check skills
        if (subscriber.getSkills() != null) {
            List<Long> reqSkills = subscriber.getSkills().stream().map(skill -> skill.getId())
                    .collect(Collectors.toList());
            List<Skill> skills = this.skillRepository.findByIdIn(reqSkills);
            subscriber.setSkills(skills);
        }

        // create subscriber
        Subscriber currentSubscriber = this.subscriberRepository.save(subscriber);

        return currentSubscriber;
    }

    public Subscriber updateSubscriber(Subscriber reqSubscriber, Subscriber subscriberDB) {

        // check skills
        if (reqSubscriber.getSkills() != null) {
            List<Long> reqSkills = reqSubscriber.getSkills().stream().map(skill -> skill.getId())
                    .collect(Collectors.toList());
            List<Skill> skills = this.skillRepository.findByIdIn(reqSkills);
            subscriberDB.setSkills(skills);
        }

        // create subscriber
        Subscriber currentSubscriber = this.subscriberRepository.save(subscriberDB);
        return currentSubscriber;
    }

    public void deleteSubscriber(long id) {
        this.subscriberRepository.deleteById(id);
    }

    public Optional<Subscriber> getSubscriberById(long id) {
        return this.subscriberRepository.findById(id);
    }

    // public ResResultPaginationDTO fetchAll(Specification<Subscriber>
    // specification, Pageable pageable) {
    // Page<Subscriber> page = this.subscriberRepository.findAll(specification,
    // pageable);

    // ResResultPaginationDTO resResultPaginationDTO = new ResResultPaginationDTO();
    // ResResultPaginationDTO.Meta meta = new ResResultPaginationDTO.Meta();

    // meta.setPage(page.getNumber() + 1); // +1 vì spring tự trừ đi 1 khi cấu hình
    // start index = 1
    // meta.setPageSize(page.getSize());
    // meta.setTotal(page.getTotalElements());
    // meta.setPages(page.getTotalPages());

    // resResultPaginationDTO.setMeta(meta);

    // List<Subscriber> listSubscriber = page.getContent();
    // resResultPaginationDTO.setResult(listSubscriber);

    // return resResultPaginationDTO;

    // }
    // }

    public void sendSubscribersEmail() {
        List<Subscriber> listSubs = this.subscriberRepository.findAll();
        if (listSubs != null && listSubs.size() > 0) {
            for (Subscriber sub : listSubs) {
                List<Skill> listSkills = sub.getSkills();
                if (listSkills != null && listSkills.size() > 0) {
                    List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
                    if (listJobs != null && listJobs.size() > 0) {

                        List<ResEmailJob> resEmailJobs = listJobs.stream().map(job -> this.convertJobToSendEmail(job))
                                .collect(Collectors.toList());
                        this.emailService.sendEmailFromTemplateSync(sub.getEmail(), "Jobs", "job", sub.getName(),
                                resEmailJobs);
                    }
                }
            }
        }
    }

    public ResEmailJob convertJobToSendEmail(Job job) {
        ResEmailJob resEmailJob = new ResEmailJob();
        resEmailJob.setName(job.getName());
        resEmailJob.setSalary(job.getSalary());
        resEmailJob.setCompany(new ResEmailJob.CompanyEmail(job.getCompany().getName()));
        List<Skill> skills = job.getSkills();
        List<ResEmailJob.SkillEmail> skillEmails = skills.stream()
                .map(skill -> new ResEmailJob.SkillEmail(skill.getName())).collect(Collectors.toList());
        resEmailJob.setSkills(skillEmails);
        return resEmailJob;
    }

    public Subscriber findByEmail(String email) {
        return this.subscriberRepository.findByEmail(email);
    }
}