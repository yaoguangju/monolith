package com.mochen.rocketmq.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mochen.rocketmq.entity.xdo.ComplexRocketmqOrderDO;
import com.mochen.rocketmq.mapper.ComplexRocketmqOrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@Slf4j
@RocketMQTransactionListener
public class TransactionListenerImpl  implements RocketMQLocalTransactionListener {


    @Resource
    private ComplexRocketmqOrderMapper complexRocketmqOrderMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        System.out.println("预提交消息成功：" + message);

        log.info("准备提交下订单的本地事务");
        try {
            JSONObject bodyObject = JSONObject.parseObject(new String((byte[]) message.getPayload(), StandardCharsets.UTF_8));
            log.info("查看消息体,{}",bodyObject);
            // 下订单
            ComplexRocketmqOrderDO complexRocketmqOrderDO = new ComplexRocketmqOrderDO();
            complexRocketmqOrderDO.setCommodityId(Long.valueOf(bodyObject.get("commodityId").toString()));
            complexRocketmqOrderDO.setCommodityAmount(Long.valueOf(bodyObject.get("commodityAmount").toString()));
            complexRocketmqOrderDO.setMoney(100L);
            log.info("查看DO,{}",complexRocketmqOrderDO);
            int insert = complexRocketmqOrderMapper.insert(complexRocketmqOrderDO);
            log.info("insert,{}",insert);
            log.info("提交本地事务");
//            int i = 1 / 0;
            return RocketMQLocalTransactionState.COMMIT;
        }catch (Exception e){
            // 手动控制回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.info("执行本地事务失败,{}",e.getMessage());
            return RocketMQLocalTransactionState.ROLLBACK;
        }

    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        return RocketMQLocalTransactionState.COMMIT;
    }
}
