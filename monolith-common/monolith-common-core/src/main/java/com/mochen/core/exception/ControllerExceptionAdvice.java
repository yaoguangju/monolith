package com.mochen.core.exception;


import com.mochen.core.common.enums.ExceptionEnum;
import com.mochen.core.common.xbo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;
import java.util.List;

@Slf4j
@ControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(value =Exception.class)
    @ResponseBody
    public Result exceptionHandler(Exception e){
        // 自定义异常
        if(e instanceof CommonException){
            CommonException commonException = (CommonException) e;
            return new Result(commonException.getCode(),commonException.getMessage());
        }
        // 参数校验异常
        else if(e instanceof BindException){
            BindException bindException = (BindException) e;
            BindingResult result = bindException.getBindingResult();
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> fieldErrors = result.getFieldErrors();
            fieldErrors.forEach(error -> {
                errorMsg.append(error.getDefaultMessage()).append("!");
            });
            return new Result(ExceptionEnum.PARAM_ERROR.getCode(),String.valueOf(errorMsg));
        }
        // 参数校验异常
        else if(e instanceof MethodArgumentNotValidException){
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
            BindingResult result = methodArgumentNotValidException.getBindingResult();
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> fieldErrors = result.getFieldErrors();
            fieldErrors.forEach(error -> {
                errorMsg.append(error.getDefaultMessage()).append("!");
            });
            return new Result(ExceptionEnum.PARAM_ERROR.getCode(),String.valueOf(errorMsg));
        }
        // 参数校验异常
        else if(e instanceof ConstraintViolationException){
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) e;
            return new Result(ExceptionEnum.PARAM_ERROR.getCode(),String.valueOf(constraintViolationException.getMessage()));
        }
        // 其他异常
        else {
            return new Result(ExceptionEnum.SYSTEM_EXCEPTION.getCode(),String.valueOf(e.getMessage()));
        }
    }



}
