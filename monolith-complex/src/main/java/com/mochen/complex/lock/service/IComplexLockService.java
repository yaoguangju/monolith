package com.mochen.complex.lock.service;

import com.mochen.complex.lock.entity.dto.ComplexLockDTO;
import com.mochen.complex.lock.entity.xdo.ComplexLockDO;
import com.baomidou.mybatisplus.extension.service.IService;

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
