package com.mochen.sharding.major.entity.xdo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 学生
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-11
 */
@Data
@Accessors(chain = true)
@TableName("student")
public class StudentDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    @TableField("school_id")
    private Long schoolId;

    /**
     * 成绩分析账号
     */
    @TableField("analysis_no")
    private String analysisNo;

    /**
     * 身份证号
     */
    @TableField("id_card")
    private String idCard;

    /**
     * 年级
     */
    @TableField("year")
    private Long year;

    /**
     * 性别 1=男 2=女
     */
    @TableField("gender")
    private Integer gender;

    /**
     * 学籍号
     */
    @TableField("student_code")
    private String studentCode;

    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 选择科目
     */
    @TableField("select_subject")
    private String selectSubject;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 维护时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;


}
