package com.mochen.complex.lock.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mochen.complex.lock.entity.dto.ComplexLockDTO;
import com.mochen.complex.lock.entity.xdo.ComplexLockDO;
import com.mochen.complex.lock.mapper.ComplexLockMapper;
import com.mochen.complex.lock.service.IComplexLockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-10
 */
@Service
public class ComplexLockServiceImpl extends ServiceImpl<ComplexLockMapper, ComplexLockDO> implements IComplexLockService {

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private ComplexLockMapper complexLockMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void testLock(ComplexLockDTO complexLockDTO) {
        RLock rLock = redissonClient.getLock("sendNoticeLock");
        try {
            if (rLock.tryLock(30, 30, TimeUnit.SECONDS)) {
                List<ComplexLockDO> threadLockDOList = complexLockMapper.selectList(new QueryWrapper<ComplexLockDO>().lambda()
                        .eq(ComplexLockDO::getName, complexLockDTO.getName()));
                if (threadLockDOList.size() == 0) {
                    ComplexLockDO threadLockDOSave = new ComplexLockDO();
                    BeanUtils.copyProperties(complexLockDTO, threadLockDOSave);
                    complexLockMapper.insert(threadLockDOSave);
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            //释放锁
            if (rLock.isLocked()) {
                rLock.unlock();
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void testLock1(ComplexLockDTO complexLockDTO) {
        List<ComplexLockDO> threadLockDOList = complexLockMapper.selectList(new QueryWrapper<ComplexLockDO>().lambda()
                .eq(ComplexLockDO::getName, complexLockDTO.getName()));
        if (threadLockDOList.size() == 0) {
            ComplexLockDO threadLockDOSave = new ComplexLockDO();
            BeanUtils.copyProperties(complexLockDTO, threadLockDOSave);
            complexLockMapper.insert(threadLockDOSave);
        }
    }
}
