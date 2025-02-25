package com.duongpham26.demo.entity.dto.response;

import java.time.Instant;

import com.duongpham26.demo.util.annotation.GenderEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateUserDTO {
    private long id;
    private String email;
    private String name;
    private int age;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Bangkok")
    private Instant createdAt;

    private GenderEnum gender;
    private String address;
}
