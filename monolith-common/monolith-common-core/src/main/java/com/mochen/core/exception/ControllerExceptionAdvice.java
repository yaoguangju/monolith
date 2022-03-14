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

    @ExceptionHandler(value =CommonException.class)
    @ResponseBody
    public Result commonExceptionHandler(CommonException e){
        return new Result(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(value =BindException.class)
    @ResponseBody
    public Result commonBindExceptionHandler(BindException e){
        BindingResult result = e.getBindingResult();
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> fieldErrors = result.getFieldErrors();
            fieldErrors.forEach(error -> {
                errorMsg.append(error.getDefaultMessage()).append("!");
            });
            return new Result(ExceptionEnum.PARAM_ERROR.getCode(),String.valueOf(errorMsg));
    }

    @ExceptionHandler(value =MethodArgumentNotValidException.class)
    @ResponseBody
    public Result commonMethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e){
        BindingResult result = e.getBindingResult();
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> fieldErrors = result.getFieldErrors();
            fieldErrors.forEach(error -> {
                errorMsg.append(error.getDefaultMessage()).append("!");
            });
            return new Result(ExceptionEnum.PARAM_ERROR.getCode(),String.valueOf(errorMsg));
    }

    @ExceptionHandler(value =ConstraintViolationException.class)
    @ResponseBody
    public Result commonConstraintViolationExceptionHandler(ConstraintViolationException e){
        return new Result(ExceptionEnum.PARAM_ERROR.getCode(),String.valueOf(e.getMessage()));
    }


}
