package com.duongpham26.demo.entity.dto.response;

import java.time.Instant;

import com.duongpham26.demo.util.constant.GenderEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResUpdateUserDTO {
    private long id;
    private String name;
    private String address;
    private GenderEnum gender;
    private int age;
    private CompanyUser company;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Bangkok")
    private Instant updatedAt;

    @Getter
    @Setter
    public static class CompanyUser {
        private long id;
        private String name;
    }
}
