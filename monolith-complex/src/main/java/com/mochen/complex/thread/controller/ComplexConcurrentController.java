package com.mochen.complex.thread.controller;


import com.mochen.complex.thread.service.IComplexConcurrentService;
import com.mochen.core.common.xbo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;


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

//    @Resource
//    private ThreadPoolExecutor threadPoolExecutor;

    @Resource
    private IComplexConcurrentService complexConcurrentService;

    @PostMapping("/setStudent")
    public Result setStudent(){
        for (int i = 0; i < 10000; i++) {
            complexConcurrentService.setStudent();
        }
        return Result.success();
    }

    @PostMapping("/testCountDownLatch")
    public Result testCountDownLatch(){
        CountDownLatch countDownLatch = new CountDownLatch(9);

        return Result.success();
    }
}

