package com.duongpham26.demo.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // hoat dong tai runtime
@Target(ElementType.METHOD) // su dungj cho method
public @interface ApiMessage {
    String value();
}
