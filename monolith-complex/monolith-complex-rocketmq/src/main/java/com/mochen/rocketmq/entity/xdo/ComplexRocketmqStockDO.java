package com.mochen.rocketmq.entity.xdo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 学生
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-09
 */
@Data
@Accessors(chain = true)
@TableName("complex_rocketmq_stock")
public class ComplexRocketmqStockDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品id
     */
    @TableField("commodity_id")
    private Long commodityId;

    /**
     * 商品库存
     */
    @TableField("commodity_stock")
    private Long commodityStock;


}
