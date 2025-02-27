package com.duongpham26.demo.entity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.duongpham26.demo.util.SecurityUtil;
import com.duongpham26.demo.util.constant.LevelEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "jobs")
@Getter
@Setter
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String location;
    private String salary;
    private String quantity;
    private LevelEnum level;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    private Instant startDate;
    private Instant endDate;
    private boolean active;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @JoinColumn(name = "company_id")
    @ManyToOne
    private Company company;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinTable(name = "job_skills", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skills;

    @PrePersist // callback method
    public void handleBeforeCreate() {
        Optional<String> userInfo = SecurityUtil.getCurrentUserLogin();
        this.createdBy = userInfo.isPresent() ? userInfo.get() : "";
        this.createdAt = Instant.now(); // lưu theo chuẩn ISO đã cấu hình
        // Lỗi mối giới mặc định lưu là múi giờ 0; => chỉnh thành múi giờ 7 (VN)
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        Optional<String> userInfo = SecurityUtil.getCurrentUserLogin();
        this.updatedBy = userInfo.isPresent() ? userInfo.get() : "";
        this.updatedAt = Instant.now();
    }
}
