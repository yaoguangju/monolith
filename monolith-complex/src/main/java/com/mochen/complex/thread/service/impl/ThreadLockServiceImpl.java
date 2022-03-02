package com.mochen.complex.thread.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mochen.complex.thread.entity.dto.ThreadLockDTO;
import com.mochen.complex.thread.entity.xdo.ThreadLockDO;
import com.mochen.complex.thread.mapper.ThreadLockMapper;
import com.mochen.complex.thread.service.IThreadLockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-02
 */
@Service
public class ThreadLockServiceImpl extends ServiceImpl<ThreadLockMapper, ThreadLockDO> implements IThreadLockService {

    @Resource
    private ThreadLockMapper threadLockMapper;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public void testLock(ThreadLockDTO threadLockDTO) {
        RLock rLock = redissonClient.getLock("sendNoticeLock");
        try {
            if (rLock.tryLock(30, 30, TimeUnit.SECONDS)) {
                List<ThreadLockDO> threadLockDOList = threadLockMapper.selectList(new QueryWrapper<ThreadLockDO>().lambda()
                        .eq(ThreadLockDO::getName, threadLockDTO.getName()));
                if (threadLockDOList.size() == 0) {
                    ThreadLockDO threadLockDOSave = new ThreadLockDO();
                    BeanUtils.copyProperties(threadLockDTO, threadLockDOSave);
                    threadLockMapper.insert(threadLockDOSave);
                } else {
                    ThreadLockDO threadLockDO = threadLockDOList.get(0);
                    BeanUtils.copyProperties(threadLockDTO, threadLockDO);
                    threadLockMapper.updateById(threadLockDO);
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
    public void testLock1(ThreadLockDTO threadLockDTO) {
        List<ThreadLockDO> threadLockDOList = threadLockMapper.selectList(new QueryWrapper<ThreadLockDO>().lambda()
                .eq(ThreadLockDO::getName, threadLockDTO.getName()));
        if (threadLockDOList.size() == 0) {
            ThreadLockDO threadLockDOSave = new ThreadLockDO();
            BeanUtils.copyProperties(threadLockDTO, threadLockDOSave);
            threadLockMapper.insert(threadLockDOSave);
        } else {
            ThreadLockDO threadLockDO = threadLockDOList.get(0);
            BeanUtils.copyProperties(threadLockDTO, threadLockDO);
            threadLockMapper.updateById(threadLockDO);
        }

    }
}
