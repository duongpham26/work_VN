package com.duongpham26.demo.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.duongpham26.demo.entity.Skill;
import com.duongpham26.demo.repository.SkillRepository;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public boolean isNameExist(String name) {
        return this.skillRepository.existsByName(name);
    }

    public Skill createSkill(Skill skill) {
        return this.skillRepository.save(skill);
    }

    public Skill updateSkill(Skill skill) {
        return this.skillRepository.save(skill);
    }

    public Skill fetchSkillById(long id) {
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        if (skillOptional.isPresent()) {
            return skillOptional.get();
        }
        return null;
    }

    public void deleteSkill(long id) {
        // delete job inside table job_skill
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        Skill skill = skillOptional.get();
        // skill.getJobs().clear();
        skill.getJobs().forEach(job -> job.getSkills().remove(skill));

        // delete subscriber
        skill.getSubscribers().forEach(sub -> sub.getSkills().remove(skill));

        // delete skill
        this.skillRepository.delete(skill);
    }
}
