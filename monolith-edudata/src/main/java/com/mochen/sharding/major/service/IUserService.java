package com.mochen.sharding.major.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mochen.core.exception.CommonException;
import com.mochen.sharding.major.entity.dto.LoginDTO;
import com.mochen.sharding.major.entity.vo.LoginVO;
import com.mochen.sharding.major.entity.xdo.UserDO;

/**
 * <p>
 * 学生 服务类
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-12
 */
public interface IUserService extends IService<UserDO> {

    LoginVO login(LoginDTO loginDTO) throws CommonException;

    void logout();

    void createPassword();
}
