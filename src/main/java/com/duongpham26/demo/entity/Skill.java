package com.duongpham26.demo.entity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.duongpham26.demo.util.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "skills")
@Getter
@Setter
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name is required")
    private String name;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @ManyToMany(mappedBy = "skills", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Job> jobs;

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
