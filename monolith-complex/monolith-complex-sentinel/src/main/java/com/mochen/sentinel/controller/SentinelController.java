package com.mochen.sentinel.controller;

import com.mochen.core.common.xbo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sentinel")
public class SentinelController {

    @GetMapping("/testSentinel")
    public Result testSentinel(){

        return Result.success();
    }
}
