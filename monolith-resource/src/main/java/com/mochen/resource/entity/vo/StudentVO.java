package com.mochen.resource.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class StudentVO {

    private String studentId;
    private String name;
    private String school;
    private String schoolId;
    private String district;
    private String districtId;
    private String analysisNo;
    private String idCard;
    private String year;
    private String gender;
    private String studentCode;
    private String avatar;
    private String createTime;
    private String updateTime;
    private List<String> suggestion;
}
