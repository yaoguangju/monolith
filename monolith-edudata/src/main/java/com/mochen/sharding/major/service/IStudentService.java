package com.mochen.sharding.major.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mochen.sharding.major.entity.xdo.StudentDO;

import java.util.List;

/**
 * <p>
 * 学生 服务类
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-11
 */
public interface IStudentService extends IService<StudentDO> {

    List<StudentDO> getStudentList();
}
