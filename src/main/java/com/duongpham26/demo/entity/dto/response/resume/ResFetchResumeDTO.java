package com.duongpham26.demo.entity.dto.response.resume;

import java.time.Instant;

import com.duongpham26.demo.util.constant.ResumeStatusEnum;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ResFetchResumeDTO {
    private long id;
    private String name;
    private String url;

    @Enumerated(EnumType.STRING)
    private ResumeStatusEnum status;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    private String companyName;
    private UserResume user;
    private JobResume job;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserResume {
        private long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JobResume {
        private long id;
        private String name;
    }
}
