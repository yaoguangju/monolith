package com.mochen.swagger.controller;

import com.mochen.swagger.entity.UserDO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "这个注解写在controller类上，用于表明这个类表明什么")
public class HelloController {

    @ApiOperation("这个注解写在接口方法上，用于表明这个方法表明什么")
    @GetMapping("/test1")
    public String test1(@RequestParam @ApiParam("用户姓名") String name){
        return name;
    }

    @ApiOperation("这个注解写在接口方法上，用于表明这个方法表明什么")
    @PostMapping("/test2")
    public UserDO test2(@RequestBody UserDO userDO){
        return userDO;
    }
}
