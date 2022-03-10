package com.mochen.complex.web.service;

import com.mochen.complex.web.entity.vo.SchoolStudentVO;
import com.mochen.complex.web.entity.xdo.ComplexWebStudentDO;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
