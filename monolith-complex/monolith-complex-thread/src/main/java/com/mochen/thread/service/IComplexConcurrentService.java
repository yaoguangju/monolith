package com.mochen.thread.service;

import com.mochen.thread.entity.xdo.ComplexConcurrentDO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-10
 */
public interface IComplexConcurrentService extends IService<ComplexConcurrentDO> {

    void setStudent();
}
