package com.mochen.lock.controller;


import com.mochen.core.common.xbo.Result;
import com.mochen.lock.entity.dto.ComplexLockDTO;
import com.mochen.lock.service.IComplexLockService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-10
 */
@RestController
@RequestMapping("/complex-lock")
public class ComplexLockController {

    @Resource
    private IComplexLockService complexLockService;

    @Resource
    private RedissonClient redissonClient;

    /**
     * 事务和锁同时存在的情况下，需要将锁的粒度调整的比事务大
     * 错误情况，事务里面有锁，锁释放了，但是事务还没有提交，数据库里面没有数据
     */
    @PostMapping("/testLock1")
    public Result testLock1(@RequestBody ComplexLockDTO complexLockDTO){
        complexLockService.testLock(complexLockDTO);
        return Result.success();
    }
    @PostMapping("/testLock2")
    public Result testLock2(@RequestBody ComplexLockDTO complexLockDTO){

        RLock rLock = redissonClient.getLock("sendNoticeLock");
        try {
            if (rLock.tryLock(30, 30, TimeUnit.SECONDS)) {
                complexLockService.testLock1(complexLockDTO);
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

