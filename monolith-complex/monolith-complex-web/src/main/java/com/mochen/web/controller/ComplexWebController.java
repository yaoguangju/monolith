package com.mochen.web.controller;



import com.mochen.web.config.CustomizeConfig;
import com.mochen.web.entity.vo.SchoolStudentVO;
import com.mochen.web.service.IComplexWebStudentService;
import com.mochen.core.common.xbo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 学生 前端控制器
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-10
 */
@RestController
@RequestMapping("/complex-web")
public class ComplexWebController {

    @Resource
    private IComplexWebStudentService complexWebStudentService;

    @Resource
    private CustomizeConfig customizeConfig;

    @PostMapping("/getStudentList")
    public Result getStudentList(){
        SchoolStudentVO schoolStudentVO = complexWebStudentService.getStudentList();
        return Result.success(schoolStudentVO);
    }

    @PostMapping("/getJwtSecret")
    public Result getJwtSecret(){
        return Result.success(customizeConfig.getJwtSecret());
    }

}

