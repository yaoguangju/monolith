package com.mochen.complex.thread.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.mochen.complex.thread.entity.xdo.ComplexConcurrentDO;
import com.mochen.complex.thread.mapper.ComplexConcurrentMapper;
import com.mochen.complex.thread.service.IComplexConcurrentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

    @Override
    @Async("asyncServiceExecutor")
    public void setStudent() {
        ComplexConcurrentDO complexConcurrentDO = new ComplexConcurrentDO();
        complexConcurrentDO.setName("小明");
        complexConcurrentDO.setAge((long) RandomUtil.randomInt(10, 99));
        complexConcurrentMapper.insert(complexConcurrentDO);

    }
}
