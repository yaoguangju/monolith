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
@TableName("subject")
public class SubjectDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("school")
    private String school;

    @TableField("school_code")
    private String schoolCode;

    @TableField("subject_id")
    private String subjectId;

    @TableField("level")
    private String level;

    @TableField("profession_class")
    private String professionClass;

    @TableField("scope")
    private String scope;

    @TableField("profession")
    private String profession;


}
