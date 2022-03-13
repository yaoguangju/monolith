package com.mochen.sharding.controller;


import cn.hutool.jwt.JWT;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.mochen.core.common.xbo.Result;
import com.mochen.sharding.entity.xdo.StudentDO;
import com.mochen.sharding.service.IStudentService;
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
 * @since 2022-03-11
 */
@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    private IStudentService studentService;

    @GetMapping("/getStudentList")
    public Result getStudentList(){

        List<StudentDO> studentDOList = studentService.getStudentList();
        return Result.success(studentDOList);
    }

    @GetMapping("/getToken")
    public Result getToken(){
        String jwtSecret = "1231321";
        JWTSigner signer = JWTSignerUtil.hs256(jwtSecret.getBytes());

        String token = JWT.create()
                .setPayload("uid", "1464947870011945039")
                .setPayload("analysis_no", "JN20200101010101")
                .setPayload("year", 2020)
                .setPayload("expire_time", System.currentTimeMillis() + 60 * 60 * 1000L)
                .sign(signer);
        return Result.success(token);
    }

}

