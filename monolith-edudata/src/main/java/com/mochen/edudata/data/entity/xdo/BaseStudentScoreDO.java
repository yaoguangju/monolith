package com.mochen.edudata.data.entity.xdo;

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
 * @since 2022-03-18
 */
@Data
@Accessors(chain = true)
@TableName("base_student_score")
public class BaseStudentScoreDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 学生id
     */
    @TableField("student_id")
    private Long studentId;

    @TableField("exam_id")
    private Long examId;

    /**
     * 客观题成绩
     */
    @TableField("objective_score")
    private BigDecimal objectiveScore;

    /**
     * 主观题成绩
     */
    @TableField("subjective_score")
    private BigDecimal subjectiveScore;

    /**
     * 成绩
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

    /**
     * 科目id
     */
    @TableField("subject_id")
    private Integer subjectId;


}
