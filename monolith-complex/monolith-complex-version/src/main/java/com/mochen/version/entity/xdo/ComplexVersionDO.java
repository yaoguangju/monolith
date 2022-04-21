package com.mochen.version.entity.xdo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
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
 * @since 2022-04-21
 */
@Data
@Accessors(chain = true)
@TableName("complex_version")
public class ComplexVersionDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 年龄
     */
    @TableField("age")
    private Long age;

    /**
     * 版本号
     */
    @Version
    private Integer version;


}
