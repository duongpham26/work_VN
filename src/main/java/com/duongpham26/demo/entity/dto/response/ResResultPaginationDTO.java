package com.duongpham26.demo.entity.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResResultPaginationDTO {
    private Meta meta;
    private Object result;

    @Getter
    @Setter
    public static class Meta {
        private int page;
        private int pageSize;
        private int pages;
        private long total;
    }
}
