package com.mochen.complex.easyexcel.controller;


import com.mochen.complex.easyexcel.service.IComplexEasyexcelService;
import com.mochen.core.common.xbo.Result;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-10
 */
@RestController
@RequestMapping("/complex-easyexcel")
public class ComplexEasyExcelController {

    @Resource
    private IComplexEasyexcelService complexEasyexcelService;

    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) throws IOException {
        complexEasyexcelService.upload(file);
        return Result.success();
    }


    @GetMapping("/download")
    public void download(HttpServletResponse response) throws IOException {
        complexEasyexcelService.download(response);
    }
}

