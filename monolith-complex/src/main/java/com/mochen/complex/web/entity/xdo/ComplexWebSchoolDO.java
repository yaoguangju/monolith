package com.mochen.complex.web.entity.xdo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 区校
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-10
 */
@Data
@Accessors(chain = true)
@TableName("complex_web_school")
public class ComplexWebSchoolDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    @TableField("o_id")
    private Long oId;

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
