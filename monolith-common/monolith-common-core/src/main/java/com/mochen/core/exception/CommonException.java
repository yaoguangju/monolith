package com.mochen.core.exception;

import com.mochen.core.common.enums.ExceptionEnum;
import lombok.Getter;

/**
 * 通用异常处理类
 *
 * @author 姚广举
 * @since 2020-12-21
 */
@Getter
public class CommonException extends RuntimeException {
    private final Integer code;
    private String msg;

    public CommonException() {
        super(ExceptionEnum.UNIFIED_EXCEPTION.getMsg());
        this.code = ExceptionEnum.UNIFIED_EXCEPTION.getCode();
        this.msg = ExceptionEnum.UNIFIED_EXCEPTION.getMsg();
    }

    public CommonException(String msg) {
        super(msg);
        this.code = ExceptionEnum.UNIFIED_EXCEPTION.getCode();
    }

    public CommonException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public CommonException(ExceptionEnum type) {
        super(type.getMsg());
        this.code = type.getCode();
    }
}
