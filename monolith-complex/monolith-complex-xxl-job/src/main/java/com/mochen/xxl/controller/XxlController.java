package com.mochen.xxl.controller;

import com.mochen.core.common.xbo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/xxl")
public class XxlController {

    @GetMapping("/test")
    public Result test(){
        return Result.success("访问成功");
    }
}
