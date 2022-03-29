package com.mochen.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mochen.web.entity.vo.SchoolStudentVO;
import com.mochen.web.entity.xdo.ComplexWebStudentDO;
import com.mochen.web.mapper.ComplexWebStudentMapper;
import com.mochen.web.service.IComplexWebStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 学生 服务实现类
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-10
 */
@Service
public class ComplexWebStudentServiceImpl extends ServiceImpl<ComplexWebStudentMapper, ComplexWebStudentDO> implements IComplexWebStudentService {

    @Resource
    private ComplexWebStudentMapper complexWebStudentMapper;

    @Override
    public SchoolStudentVO getStudentList() {
        SchoolStudentVO schoolStudentVO = new SchoolStudentVO();
        List<ComplexWebStudentDO> complexWebStudentDOList = complexWebStudentMapper.selectList(new QueryWrapper<ComplexWebStudentDO>()
                .eq("school_id", 1487306021557899264L));
        List<SchoolStudentVO.Student> studentList = new ArrayList<>();
        complexWebStudentDOList.forEach(ComplexWebStudentDO -> {
            SchoolStudentVO.Student student = new SchoolStudentVO.Student();
            student.setYear(ComplexWebStudentDO.getYear());
            student.setName(ComplexWebStudentDO.getName());
            studentList.add(student);
        });
        schoolStudentVO.setId(1L)
                .setName("山东省实验中学")
                .setStudentList(studentList);
        return schoolStudentVO;
    }
}
