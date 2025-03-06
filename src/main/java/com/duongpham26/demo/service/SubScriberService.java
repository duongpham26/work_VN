package com.duongpham26.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.duongpham26.demo.entity.Subscriber;
import com.duongpham26.demo.entity.Company;
import com.duongpham26.demo.entity.Skill;
import com.duongpham26.demo.repository.SubscriberRepository;
import com.duongpham26.demo.repository.SkillRepository;

@Service
public class SubScriberService {

    private final SkillRepository skillRepository;

    private final SubscriberRepository subscriberRepository;

    public SubScriberService(SkillRepository skillRepository, SubscriberRepository subscriberRepository) {
        this.skillRepository = skillRepository;
        this.subscriberRepository = subscriberRepository;
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
}