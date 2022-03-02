package com.mochen.core.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用返回枚举类
 *
 * @author 刘雪
 * @since 2021/12/28 13:55:21
 */
@Getter
@AllArgsConstructor
public enum CommonEnum {

    NO(0, "否"),

    YES(1, "是");

    private final Integer code;

    private final String msg;
}
