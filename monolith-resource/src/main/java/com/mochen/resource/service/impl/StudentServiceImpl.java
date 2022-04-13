package com.mochen.resource.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mochen.resource.entity.xdo.StudentDO;
import com.mochen.resource.mapper.StudentMapper;
import com.mochen.resource.service.IStudentService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学生 服务实现类
 * </p>
 *
 * @author 姚广举
 * @since 2022-04-02
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, StudentDO> implements IStudentService {

}
