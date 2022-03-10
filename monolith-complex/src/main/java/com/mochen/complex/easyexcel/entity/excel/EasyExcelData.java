package com.mochen.complex.easyexcel.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class EasyExcelData implements Serializable {

    @ExcelProperty(index = 0)
    private String analysisNo;
    @ExcelProperty(index = 1)
    private String name;
    @ExcelProperty(index = 2)
    private String school;
    @ExcelProperty(index = 3)
    private Long classId;
    @ExcelProperty(index = 4)
    private BigDecimal score;
    @ExcelProperty(index = 5)
    private Long classRank;
    @ExcelProperty(index = 6)
    private Long schoolRank;
    @ExcelProperty(index = 7)
    private Long totalRank;

}
