package com.mochen.complex.validation.controller;


import com.mochen.complex.validation.entity.dto.ComplexValidationDTO;
import com.mochen.core.common.xbo.Result;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 绑定学生 前端控制器
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-03
 */
@RestController
@RequestMapping("/complex-validation")
public class ComplexValidationController {

    /**
     * springboot-2.3后需要单独引入spring-boot-starter-validation组件
     * @param complexValidationDTO
     * @return
     */
    @PostMapping("/getStudentList")
    public Result getStudentList(@RequestBody @Valid ComplexValidationDTO complexValidationDTO){

        return Result.success(complexValidationDTO);
    }
}

