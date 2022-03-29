package com.mochen.quartz.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
public class JobDTO {

    private String jobName;
    private String groupName;
    private String jobClass;
    private String cronExpression;
    private Param param;

    @Data
    @Accessors(chain = true)
    public static class Param{
        private String name;
    }
}
