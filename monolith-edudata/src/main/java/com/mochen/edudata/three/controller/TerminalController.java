package com.mochen.edudata.three.controller;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.mochen.core.common.xbo.Result;
import com.mochen.edudata.common.config.CustomizeConfig;
import com.mochen.edudata.common.datasource.DynamicDatasourceManager;
import com.mochen.edudata.common.utils.Sm4Utils;
import com.mochen.edudata.major.entity.xdo.StudentDO;
import com.mochen.edudata.three.service.ITerminalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/terminal")
public class TerminalController {

    @Resource
    private Sm4Utils sm4Utils;

    @Resource
    private DynamicDatasourceManager dynamicDatasourceManager;

    @Resource
    private ITerminalService terminalService;

    @GetMapping("/getStudentInfo")
    public Result getStudentInfo(@RequestParam("analysisNoHex") String analysisNoHex){
        String analysisNo = sm4Utils.getDecryptStr(analysisNoHex);

        DynamicDataSourceContextHolder.push(dynamicDatasourceManager.getDataSource(analysisNo));
        StudentDO studentDO = terminalService.getStudentInfo(analysisNo);
        return Result.success(studentDO);
    }

}
