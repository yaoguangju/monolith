package com.mochen.rocketmq.listen;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mochen.rocketmq.entity.xdo.ComplexRocketmqStockDO;
import com.mochen.rocketmq.mapper.ComplexRocketmqStockMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
@RocketMQMessageListener(topic = "transaction-rocketmq", consumerGroup = "Con_Transaction_Group")
public class TransactionMessageListen implements RocketMQListener<MessageExt> {

    @Resource
    private ComplexRocketmqStockMapper complexRocketmqStockMapper;

    @Override
    public void onMessage(MessageExt messageExt) {
        String msg = new String(messageExt.getBody());
        JSONObject bodyObject = JSONObject.parseObject(msg);
        log.info("接收方获取到的消息,{}",bodyObject);
        // 扣减库存
        ComplexRocketmqStockDO complexRocketmqStockDO = complexRocketmqStockMapper.selectOne(new QueryWrapper<ComplexRocketmqStockDO>()
                .lambda()
                .eq(ComplexRocketmqStockDO::getCommodityId,bodyObject.get("commodityId")));
        complexRocketmqStockDO.setCommodityStock(complexRocketmqStockDO.getCommodityStock() - Long.parseLong(bodyObject.get("commodityAmount").toString()));
        complexRocketmqStockMapper.updateById(complexRocketmqStockDO);
    }
}
