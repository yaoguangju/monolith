package com.mochen.edudata.data.controller;


import com.mochen.core.common.xbo.Result;
import com.mochen.edudata.data.service.IBaseStudentScoreService;
import com.mochen.edudata.data.service.IBaseStudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

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

    @Resource
    private IBaseStudentService baseStudentService;



    // 缓存学生信息
    @GetMapping("/saveRedisDate")
    public void saveRedisDate() {
        baseStudentService.saveRedisDate();
    }

    @GetMapping("/cacheStudentScore")
    public Result cacheStudentScore(){
        baseStudentScoreService.cacheStudentScore();
        return Result.success();
    }
}

