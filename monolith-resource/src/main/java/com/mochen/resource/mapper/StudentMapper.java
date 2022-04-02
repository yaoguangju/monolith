package com.mochen.resource.mapper;

import com.mochen.resource.entity.vo.StudentVO;
import com.mochen.resource.entity.xdo.StudentDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 学生 Mapper 接口
 * </p>
 *
 * @author 姚广举
 * @since 2022-04-02
 */
@Mapper
public interface StudentMapper extends BaseMapper<StudentDO> {


    List<StudentVO> getStudentInfo(Integer page, Integer limit);

    List<StudentVO> getStudentByMySQL();
}
