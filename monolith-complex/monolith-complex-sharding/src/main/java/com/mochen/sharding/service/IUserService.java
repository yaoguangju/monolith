package com.mochen.sharding.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mochen.sharding.entity.xdo.UserDO;

import java.util.List;

/**
 * <p>
 * 学生 服务类
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-29
 */
public interface IUserService extends IService<UserDO> {

    List<UserDO> getStudentListByPage(String current, String limit,String name);
}
