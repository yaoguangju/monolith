package com.mochen.sharding.major.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mochen.sharding.major.entity.xdo.UserDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 学生 Mapper 接口
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-12
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

}
