package com.mochen.edudata.data.controller;


import com.mochen.core.common.xbo.Result;
import com.mochen.edudata.data.service.IBaseStudentScoreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-18
 */
@RestController
@RequestMapping("/base_student_score")
public class BaseStudentScoreController {

    @Resource
    private IBaseStudentScoreService baseStudentScoreService;

    @GetMapping("/cacheStudentScore")
    public Result cacheStudentScore(){
        baseStudentScoreService.cacheStudentScore();
        return Result.success();
    }
}

