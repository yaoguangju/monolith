package com.mochen.sharding.annotation;


import com.mochen.sharding.common.contanst.DataSourceConstants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DS {

    /**
     * 数据源名称
     * @return
     */
    String value() default DataSourceConstants.DS_KEY_2019;
}
