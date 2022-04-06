package com.mochen.web.service;

import com.mochen.web.entity.vo.SchoolStudentVO;
import com.mochen.web.entity.xdo.ComplexWebStudentDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * <p>
 * 学生 服务类
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-10
 */
public interface IComplexWebStudentService extends IService<ComplexWebStudentDO> {

    SchoolStudentVO getStudentList();

    Set<Integer> getSchoolList();
}
