package com.mochen.complex.easyexcel.entity.xdo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-10
 */
@Data
@Accessors(chain = true)
@TableName("complex_easyexcel")
public class ComplexEasyexcelDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 成绩分析账号
     */
    @TableField("analysis_no")
    private String analysisNo;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 学校
     */
    @TableField("school")
    private String school;

    /**
     * 班级id
     */
    @TableField("class_id")
    private Long classId;

    /**
     * 分数
     */
    @TableField("score")
    private BigDecimal score;

    /**
     * 班级排名
     */
    @TableField("class_rank")
    private Long classRank;

    /**
     * 学校排名
     */
    @TableField("school_rank")
    private Long schoolRank;

    /**
     * 总排名
     */
    @TableField("total_rank")
    private Long totalRank;


}
