package com.mochen.complex.web.controller;


import com.itextpdf.text.pdf.PdfReader;
import com.mochen.complex.web.entity.vo.SchoolStudentVO;
import com.mochen.complex.web.service.IComplexWebStudentService;
import com.mochen.core.common.xbo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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

    @PostMapping("/getStudentList")
    public Result getStudentList(){
        SchoolStudentVO schoolStudentVO = complexWebStudentService.getStudentList();
        return Result.success(schoolStudentVO);
    }


}

