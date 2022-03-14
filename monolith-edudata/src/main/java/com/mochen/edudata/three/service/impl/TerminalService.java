package com.mochen.edudata.three.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mochen.edudata.major.entity.xdo.StudentDO;
import com.mochen.edudata.major.mapper.StudentMapper;
import com.mochen.edudata.three.service.ITerminalService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TerminalService implements ITerminalService {

    @Resource
    private StudentMapper studentMapper;

    @Override
    public StudentDO getStudentInfo(String analysisNo) {
        return studentMapper.selectOne(new QueryWrapper<StudentDO>().eq("analysis_no", analysisNo));
    }
}
