package com.duongpham26.demo.entity.dto.response.job;

import java.time.Instant;
import java.util.List;

import com.duongpham26.demo.util.constant.LevelEnum;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResUpdateJobDTO {
    private long id;
    private String name;
    private String location;
    private String salary;
    private int quantity;
    private String description;
    private LevelEnum level;
    private Instant startDate;
    private Instant endDate;
    private boolean active;

    private List<String> skills;

    private Instant updatedAt;
    private String updateBy;

}
