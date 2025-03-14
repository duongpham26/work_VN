package com.duongpham26.demo.entity.dto.response;

import java.time.Instant;

import com.duongpham26.demo.util.constant.GenderEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResUserDTO {
    private long id;
    private String name;
    private String email;
    private GenderEnum gender;
    private String address;
    private int age;
    private CompanyUser company;
    private RoleUser role;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Bangkok")
    private Instant createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Bangkok")
    private Instant updatedAt;

    @Getter
    @Setter
    public static class CompanyUser {
        private long id;
        private String name;
    }

    @Getter
    @Setter
    public static class RoleUser {
        private long id;
        private String name;
    }
}
