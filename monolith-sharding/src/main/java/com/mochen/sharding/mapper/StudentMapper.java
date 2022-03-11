package com.mochen.sharding.mapper;

import com.mochen.sharding.entity.xdo.StudentDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 学生 Mapper 接口
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-11
 */
@Mapper
public interface StudentMapper extends BaseMapper<StudentDO> {

}
