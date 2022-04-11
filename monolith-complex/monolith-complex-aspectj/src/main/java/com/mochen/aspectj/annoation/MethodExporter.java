package com.mochen.aspectj.annoation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 注解作用在方法上
@Target(ElementType.METHOD)
// @Retention的作用是定义他所注解保留多久，RUNTIME运行时
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodExporter {
}
