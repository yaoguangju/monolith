package com.mochen.lock.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mochen.lock.entity.dto.ComplexLockDTO;
import com.mochen.lock.entity.xdo.ComplexLockDO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-10
 */
public interface IComplexLockService extends IService<ComplexLockDO> {

    void testLock(ComplexLockDTO complexLockDTO);

    void testLock1(ComplexLockDTO complexLockDTO);
}
