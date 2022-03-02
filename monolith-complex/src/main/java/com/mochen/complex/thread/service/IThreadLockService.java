package com.mochen.complex.thread.service;

import com.mochen.complex.thread.entity.dto.ThreadLockDTO;
import com.mochen.complex.thread.entity.xdo.ThreadLockDO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-02
 */
public interface IThreadLockService extends IService<ThreadLockDO> {

    void testLock(ThreadLockDTO threadLockDTO);

    void testLock1(ThreadLockDTO threadLockDTO);
}
