package com.mochen.edudata.major.controller;


import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.mochen.core.common.xbo.Result;
import com.mochen.edudata.common.datasource.DatasourceManager;
import com.mochen.edudata.major.entity.dto.TeacherDTO;
import com.mochen.edudata.major.entity.xdo.StudentDO;
import com.mochen.edudata.major.service.IStudentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 学生 前端控制器
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-11
 */
@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    private IStudentService studentService;

    @GetMapping("/getStudentListByStudentRole")
    @PreAuthorize("hasAuthority('student')")
    public Result getStudentListByStudentRole(){

        List<StudentDO> studentDOList = studentService.getStudentList();
        return Result.success(studentDOList);
    }

    @PostMapping("/getStudentListByTeacherRole")
    @PreAuthorize("hasAuthority('teacher')")
    public Result getStudentListByTeacherRole(@RequestBody TeacherDTO teacherDTO){
        // 切换数据源
        DynamicDataSourceContextHolder.push(DatasourceManager.getDatasource(teacherDTO.getYear()));
        List<StudentDO> studentDOList = studentService.getStudentList();
        return Result.success(studentDOList);
    }

}

