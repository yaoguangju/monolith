package com.mochen.core.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 结果枚举
 *
 * @author 姚广举
 * @since 2020-11-11
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {

    SUCCESS(20000, "success"),
    ERROR(40000, "error");

    private final int code;
    private final String msg;
}