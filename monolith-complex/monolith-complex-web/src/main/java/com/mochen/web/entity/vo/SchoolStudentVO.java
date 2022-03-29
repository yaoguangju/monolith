package com.mochen.web.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class SchoolStudentVO {

    public Long id;
    public String name;
    public List<Student> studentList;

    @Data
    @Accessors(chain = true)
    public static class Student {
        public String name;
        public Long year;
    }
}
