package com.mochen.edudata.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mochen.edudata.data.entity.xdo.BaseStudentDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-18
 */
@Mapper
public interface BaseStudentMapper extends BaseMapper<BaseStudentDO> {

}
