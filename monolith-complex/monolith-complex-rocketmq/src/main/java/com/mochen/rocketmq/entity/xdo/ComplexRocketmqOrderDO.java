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
@TableName("complex_rocketmq_order")
public class ComplexRocketmqOrderDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品名称
     */
    @TableField("commodity_id")
    private Long commodityId;

    /**
     * 订单金额
     */
    @TableField("money")
    private Long money;

    /**
     * 商品数量
     */
    @TableField("commodity_amount")
    private Long commodityAmount;


}
