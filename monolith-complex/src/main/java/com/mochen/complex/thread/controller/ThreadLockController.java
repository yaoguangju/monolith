package com.mochen.complex.thread.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mochen.complex.thread.entity.dto.ThreadLockDTO;
import com.mochen.complex.thread.entity.xdo.ThreadLockDO;
import com.mochen.complex.thread.service.IThreadLockService;
import com.mochen.core.common.xbo.Result;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private RedissonClient redissonClient;

    @Resource
    private IThreadLockService threadLockService;

    /**
     * 事务和锁同时存在的情况下，需要将锁的粒度调整的比事务大
     * 错误情况，事务里面有锁，锁释放了，但是事务还没有提交，数据库里面没有数据
     */
    @PostMapping("/testLock")
    public Result testLock(@RequestBody ThreadLockDTO threadLockDTO){
        threadLockService.testLock(threadLockDTO);
        return Result.success();
    }
    @PostMapping("/testLock1")
    public Result testLock1(@RequestBody ThreadLockDTO threadLockDTO){

        RLock rLock = redissonClient.getLock("sendNoticeLock");
        try {
            if (rLock.tryLock(30, 30, TimeUnit.SECONDS)) {
                threadLockService.testLock1(threadLockDTO);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally{
            //释放锁
            if (rLock.isLocked()) {
                rLock.unlock();
            }
        }
        return Result.success();
    }

}

