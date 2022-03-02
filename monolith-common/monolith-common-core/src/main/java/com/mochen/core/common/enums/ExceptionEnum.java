package com.mochen.core.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionEnum {

    // 通用异常
    PARAM_ERROR(40000,"参数错误异常"),
    UNIFIED_EXCEPTION(50000,"统一异常"),
    OPEN_FEIGN_ERROR(50001, "openFeign调用失败"),
    SYSTEM_EXCEPTION(99999, "通用异常");

    /**
     * 错误码
     */
    private final int code;

    /**
     * 提示信息
     */
    private final String msg;
}
