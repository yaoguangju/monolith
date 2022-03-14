package com.mochen.edudata.three.controller;

import com.mochen.core.common.xbo.Result;
import com.mochen.edudata.major.entity.xdo.StudentDO;
import com.mochen.edudata.three.service.ITerminalService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/terminal")
public class TerminalController {

    @Resource
    private ITerminalService terminalService;

    @GetMapping("/getStudentInfo")
    public Result getStudentInfo(@RequestParam("analysisNo") String analysisNo){
        StudentDO studentDO = terminalService.getStudentInfo(analysisNo);
        return Result.success(studentDO);
    }
}
