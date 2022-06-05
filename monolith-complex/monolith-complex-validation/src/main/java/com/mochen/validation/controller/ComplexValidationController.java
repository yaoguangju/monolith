package com.mochen.validation.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mochen.core.common.xbo.Result;
import com.mochen.validation.entity.dto.ComplexValidationDTO;
import com.mochen.validation.entity.xdo.ComplexValidationDO;
import com.mochen.validation.mapper.ComplexValidationMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

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
@Validated // 单个参数或者Path参数校验
public class ComplexValidationController {

    @Resource
    private ComplexValidationMapper complexValidationMapper;

    /**
     * springboot-2.3后需要单独引入spring-boot-starter-validation组件
     */
    @GetMapping("/validationDTO")
    public Result validationDTO(@RequestBody @Valid ComplexValidationDTO complexValidationDTO){

        return Result.success(complexValidationDTO);
    }

    @GetMapping("/validationPathVariable/year/{year}")
    public Result validationPathVariable(@PathVariable @Min(2018) Integer year){
        List<ComplexValidationDO> studentList = complexValidationMapper.selectList(new QueryWrapper<ComplexValidationDO>()
                .eq("year", year));
        return Result.success(studentList);
    }

    @GetMapping("/validationRequestParam")
    public Result validationRequestParam(@RequestParam @Min(2018) Integer year){
        List<ComplexValidationDO> studentList = complexValidationMapper.selectList(new QueryWrapper<ComplexValidationDO>()
                .eq("year", year));
        return Result.success(studentList);
    }
}

