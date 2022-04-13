package com.mochen.resource.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mochen.resource.entity.xdo.UserDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 学生 Mapper 接口
 * </p>
 *
 * @author 姚广举
 * @since 2022-04-08
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

}
