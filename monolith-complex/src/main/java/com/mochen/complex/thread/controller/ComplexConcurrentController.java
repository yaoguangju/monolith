package com.mochen.complex.thread.controller;


import com.mochen.complex.thread.service.IComplexConcurrentService;
import com.mochen.core.common.xbo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-10
 */
@RestController
@RequestMapping("/complex-concurrent")
public class ComplexConcurrentController {

//    @Qualifier("taskExecutor")
//    @Autowired
//    private Executor executor;

    @Resource
    private IComplexConcurrentService complexConcurrentService;

    @PostMapping("/setStudent")
    public Result setStudent(){
        for (int i = 0; i < 10000; i++) {
            complexConcurrentService.setStudent();
        }
        return Result.success();
    }

//    @PostMapping("/testCountDownLatch")
//    public Result testCountDownLatch(){
//        executor.execute(() -> {
//            System.out.println("111111111111111111");
//            Thread t = Thread.currentThread();
//            System.out.println(t.getName());
//        });
//        return Result.success();
//    }
}

