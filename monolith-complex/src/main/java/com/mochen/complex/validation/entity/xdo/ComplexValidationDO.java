package com.mochen.complex.validation.entity.xdo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 绑定学生
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-03
 */
@Data
@Accessors(chain = true)
@TableName("complex_validation")
public class ComplexValidationDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 学校id
     */
    @TableField("school_id")
    private Long schoolId;

    /**
     * 身份证号
     */
    @TableField("id_card")
    private String idCard;

    /**
     * 成绩分析账号
     */
    @TableField("analysis_no")
    private String analysisNo;

    /**
     * 年级
     */
    @TableField("year")
    private String year;

    /**
     * 余额
     */
    @TableField("balance")
    private BigDecimal balance;

    /**
     * 是否认证  0=未认证，1=已认证
     */
    @TableField("is_certificated")
    private Integer isCertificated;

    /**
     * 是否为补全的身份证号  0=不是，1=是
     */
    @TableField("is_completion")
    private Integer isCompletion;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;


}
