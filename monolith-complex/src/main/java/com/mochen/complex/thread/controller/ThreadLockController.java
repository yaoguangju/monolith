package com.mochen.complex.thread.controller;


import com.mochen.complex.thread.entity.dto.ThreadLockDTO;
import com.mochen.complex.thread.service.IThreadLockService;
import com.mochen.core.common.xbo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-02
 */
@RestController
@RequestMapping("/thread-lock")
public class ThreadLockController {

    @Resource
    private IThreadLockService threadLockService;

    @PostMapping("/testLock")
    public Result testLock(@RequestBody ThreadLockDTO threadLockDTO){
        threadLockService.testLock(threadLockDTO);
        return Result.success();
    }

}

