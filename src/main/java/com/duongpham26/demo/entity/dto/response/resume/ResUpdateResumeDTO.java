package com.duongpham26.demo.entity.dto.response.resume;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResUpdateResumeDTO {
    private String updatedBy;
    private Instant updatedAt;
}
