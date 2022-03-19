package com.mochen.edudata.data.service;

import com.mochen.edudata.data.entity.xdo.BaseStudentDO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-18
 */
public interface IBaseStudentService extends IService<BaseStudentDO> {

    void saveRedisDate();
}
