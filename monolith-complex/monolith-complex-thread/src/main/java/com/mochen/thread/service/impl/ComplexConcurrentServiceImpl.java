package com.mochen.thread.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mochen.thread.entity.xdo.ComplexConcurrentDO;
import com.mochen.thread.mapper.ComplexConcurrentMapper;
import com.mochen.thread.service.IComplexConcurrentService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-10
 */
@Service
public class ComplexConcurrentServiceImpl extends ServiceImpl<ComplexConcurrentMapper, ComplexConcurrentDO> implements IComplexConcurrentService {

    @Resource
    private ComplexConcurrentMapper complexConcurrentMapper;

    @Resource
    @Qualifier("asyncServiceExecutor")
    private Executor executor;

    @Override
//    @Async("asyncServiceExecutor")
    public void setStudent() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Thread t = Thread.currentThread();
                System.out.println(t.getName());
                ComplexConcurrentDO complexConcurrentDO = new ComplexConcurrentDO();
                complexConcurrentDO.setName("小明");
                complexConcurrentDO.setAge((long) RandomUtil.randomInt(10, 99));
                complexConcurrentMapper.insert(complexConcurrentDO);
            }
        });
    }
}
