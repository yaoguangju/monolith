package com.mochen.web.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mochen.core.common.xbo.Result;
import com.mochen.log.annotation.SysLog;
import com.mochen.redis.common.manager.RedisManager;
import com.mochen.web.config.CustomizeConfig;
import com.mochen.web.entity.vo.SchoolStudentVO;
import com.mochen.web.entity.xdo.ComplexWebStudentDO;
import com.mochen.web.mapper.ComplexWebStudentMapper;
import com.mochen.web.service.IComplexWebStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

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

    @Resource
    private RedisManager redisManager;

    @Autowired
    private ComplexWebStudentMapper complexWebStudentMapper;

    @SysLog("学生列表")
    @PostMapping("/getStudentList")
    public Result getStudentList(){
        SchoolStudentVO schoolStudentVO = complexWebStudentService.getStudentList();
        return Result.success(schoolStudentVO);
    }

    @SysLog("学校列表")
    @GetMapping("/getSchoolList")
    public Result getSchoolList(){
        Set<Integer> complexWebStudentDOSet = complexWebStudentService.getSchoolList();
        return Result.success(complexWebStudentDOSet);
    }

    @SysLog("学校列表")
    @GetMapping("/getLogEx")
    public Result getLogEx() throws Exception {
        throw new Exception("1111");
    }

    @PostMapping("/getJwtSecret")
    public Result getJwtSecret(){
        return Result.success(customizeConfig.getJwtSecret());
    }

    @GetMapping("/testRedis")
    public Result testRedis(){
        redisManager.setCacheObject("小明","已消费");
        return Result.success();
    }

    @PostMapping("/getStudent")
    public Result getStudent(@RequestParam String schoolId,
                             @RequestParam String year){
        List<ComplexWebStudentDO> complexWebStudentDOList = complexWebStudentMapper.selectList(new QueryWrapper<ComplexWebStudentDO>()
                .lambda()
                .eq(ComplexWebStudentDO::getSchoolId,schoolId)
                .eq(ComplexWebStudentDO::getYear,year));
        return Result.success(complexWebStudentDOList);
    }

}

