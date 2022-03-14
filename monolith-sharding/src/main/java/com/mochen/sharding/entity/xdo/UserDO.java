package com.mochen.sharding.entity.xdo;

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
 * @since 2022-03-12
 */
@Data
@Accessors(chain = true)
@TableName("user")
public class UserDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    /**
     * 成绩分析账号
     */
    @TableField("analysis_no")
    private String analysisNo;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 角色
     */
    @TableField("role")
    private String role;

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
     * 年级
     */
    @TableField("year")
    private Long year;

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
