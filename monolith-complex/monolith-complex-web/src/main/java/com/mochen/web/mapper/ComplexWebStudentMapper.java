package com.mochen.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mochen.web.entity.xdo.ComplexWebStudentDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

/**
 * <p>
 * 学生 Mapper 接口
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-10
 */
@Mapper
public interface ComplexWebStudentMapper extends BaseMapper<ComplexWebStudentDO> {

    Set<Integer> getSchoolList();
}
