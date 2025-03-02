package com.duongpham26.demo.entity;

import java.time.Instant;
import java.util.Optional;

import com.duongpham26.demo.util.SecurityUtil;
import com.duongpham26.demo.util.constant.ResumeStatusEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "resumes")
@Getter
@Setter
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Url not be must blank (cv upload failed)")
    private String url;

    @Enumerated(EnumType.STRING)
    private ResumeStatusEnum status;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @JoinColumn(name = "job_id")
    @ManyToOne
    private Job job;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

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
