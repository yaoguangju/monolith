package com.mochen.jsoup.entity.xdo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 姚广举
 * @since 2022-05-13
 */
@Data
@Accessors(chain = true)
@TableName("school")
public class SchoolDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("school_id")
    private String schoolId;

    @TableField("city")
    private String city;

    @TableField("code")
    private String code;

    @TableField("school")
    private String school;

    @TableField("official_website")
    private String officialWebsite;

    @TableField("dm")
    private String dm;

    @TableField("mc")
    private String mc;

    @TableField("yzm")
    private String yzm;

    @TableField("nf")
    private String nf;


}
