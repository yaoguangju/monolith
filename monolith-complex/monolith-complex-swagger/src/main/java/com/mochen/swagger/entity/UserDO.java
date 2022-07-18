package com.mochen.swagger.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户中心实体类")
public class UserDO {

    @ApiModelProperty(value = "用户名")
    private String name;
    @ApiModelProperty(value = "年龄")
    private Integer age;
}
