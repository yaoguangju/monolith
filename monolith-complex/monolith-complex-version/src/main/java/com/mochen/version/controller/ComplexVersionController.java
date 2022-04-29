package com.mochen.version.controller;


import com.mochen.core.common.xbo.Result;
import com.mochen.version.entity.xdo.ComplexVersionDO;
import com.mochen.version.mapper.ComplexVersionMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 姚广举
 * @since 2022-04-21
 */
@RestController
@RequestMapping("/complex_version")
public class ComplexVersionController {

    @Resource
    private ComplexVersionMapper complexVersionMapper;

    @GetMapping("/addAge")
    public Result addAge(){

        int i = 0;
        while (i == 0){
            ComplexVersionDO complexVersionDO = complexVersionMapper.selectById(1);
            complexVersionDO.setAge(complexVersionDO.getAge() + 1);
            i = complexVersionMapper.updateById(complexVersionDO);
        }
//        synchronized (this){
//            ComplexVersionDO complexVersionDO = complexVersionMapper.selectById(1);
//            complexVersionDO.setAge(complexVersionDO.getAge() + 1);
//            complexVersionMapper.updateById(complexVersionDO);
//        }
        return Result.success();
    }

}

