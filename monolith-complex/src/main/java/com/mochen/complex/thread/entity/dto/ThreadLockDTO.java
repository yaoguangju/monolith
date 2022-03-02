package com.mochen.complex.thread.entity.dto;

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
 * @since 2022-03-02
 */
@Data
public class ThreadLockDTO {

    private String name;

    private Long age;

    private Integer status;

}
