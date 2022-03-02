package com.mochen.complex.string.controller;


import com.mochen.core.common.xbo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/string")
public class StringController {

    @GetMapping("/test")
    public Result test(@RequestParam("str") String str){
        int a = 123;
        Integer b = 123;
        Integer c = a/0;
        Map<String, Object> map = new HashMap<>();
        map.put("name",str);
        return Result.success(map);
    }

}
