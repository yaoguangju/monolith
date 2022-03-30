package com.mochen.sharding.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mochen.core.common.xbo.Result;
import com.mochen.sharding.entity.xdo.UserDO;
import com.mochen.sharding.service.IUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 学生 前端控制器
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-29
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @GetMapping("/getStudentList")
    public Result getStudentList(){
        List<UserDO> list = userService.list();
        return Result.success(list);
    }

    @GetMapping("/getStudentListByName")
    public Result getStudentListByName(){
        List<UserDO> list = userService.list(new QueryWrapper<UserDO>().like("name","姚"));
        return Result.success(list);
    }

    @GetMapping("/getStudentListByYear")
    public Result getStudentListByYear(){
        List<UserDO> list = userService.list(new QueryWrapper<UserDO>().eq("year","2019").like("name","姚"));
        return Result.success(list);
    }

    @GetMapping("/getStudentListByAnalysisNo")
    public Result getStudentListByAnalysisNo(){
        List<UserDO> list = userService.list(new QueryWrapper<UserDO>().eq("analysis_no","JNN20191201011250").eq("year","2019"));
        return Result.success(list);
    }

    @GetMapping("/setStudent")
    public Result setStudent(){
        UserDO userDO = new UserDO();
        userDO.setName("测试添加数据2019");
        userDO.setYear(2019L);
        userService.save(userDO);
        return Result.success();
    }


}

