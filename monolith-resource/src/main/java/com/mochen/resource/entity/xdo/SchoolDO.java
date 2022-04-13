package com.mochen.resource.entity.xdo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 区校
 * </p>
 *
 * @author 姚广举
 * @since 2022-04-02
 */
@Data
@Accessors(chain = true)
@TableName("school")
public class SchoolDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    /**
     * 校名
     */
    @TableField("name")
    private String name;

    /**
     * 县区
     */
    @TableField("district_id")
    private Long districtId;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;


}
