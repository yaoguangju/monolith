package com.mochen.complex.rocketmq.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mochen.complex.rocketmq.entity.xdo.ComplexRocketmqOrderDO;
import com.mochen.complex.rocketmq.entity.xdo.ComplexRocketmqStockDO;
import com.mochen.complex.rocketmq.mapper.ComplexRocketmqOrderMapper;
import com.mochen.complex.rocketmq.mapper.ComplexRocketmqStockMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;

@Slf4j
@Service
public class TransactionListenerImpl  implements TransactionListener {



    @Resource
    private ComplexRocketmqOrderMapper complexRocketmqOrderMapper;

    @Override
    //执行本地事务
    @Transactional(rollbackFor = Exception.class)
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        System.out.println("预提交消息成功：" + message);
        if(StringUtils.contains("TAGA",message.getTags())){
            log.info("准备提交下订单的本地事务");
            try {
                JSONObject bodyObject = JSONObject.parseObject(new String(message.getBody()));
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
                int i = 1 / 0;
                return LocalTransactionState.COMMIT_MESSAGE;
            }catch (Exception e){
                // 手动控制回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.info("执行本地事务失败,{}",e.getMessage());
                return LocalTransactionState.ROLLBACK_MESSAGE;
            }
        }
        return LocalTransactionState.UNKNOW;
    }

    @Override
    //检查本地事务
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        log.info("执行消息回查,{}",messageExt.getTags());
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
