package com.mochen.edudata.data.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class EasyExcelData implements Serializable {

    @ExcelProperty(index = 0)
    private String analysisNo;
    @ExcelProperty(index = 6)
    private BigDecimal objectiveScore;
    @ExcelProperty(index = 7)
    private BigDecimal subjectiveScore;
    @ExcelProperty(index = 8)
    private BigDecimal score;
    @ExcelProperty(index = 9)
    private Long classRank;
    @ExcelProperty(index = 10)
    private Long schoolRank;
    @ExcelProperty(index = 11)
    private Long totalRank;

}
