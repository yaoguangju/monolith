package com.mochen.validation.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class ComplexValidationDTO {

    @NotNull(message = "学校id不能为空")
    private Long schoolId;
    @NotBlank(message = "学年不能为空")
    private String year;
}
