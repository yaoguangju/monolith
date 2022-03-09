package com.mochen.complex.rocketmq.common.config;



import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mochen.complex.rocketmq.entity.xdo.ComplexRocketmqStockDO;
import com.mochen.complex.rocketmq.mapper.ComplexRocketmqStockMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


@Component
@Slf4j
public class SpringInitRunner implements ApplicationRunner {

    @Resource
    private ComplexRocketmqStockMapper complexRocketmqStockMapper;


    @Override
    public void run(ApplicationArguments args) throws Exception {

        // 实例化消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("affair");
        // 设置NameServer的地址
        consumer.setNamesrvAddr("192.168.164.128:9876");
        // 指定从第一条开始消费
//        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 订阅Topics
        consumer.subscribe("TopicTest2", "TAGA");
        // 注册消息监听者
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messages, ConsumeConcurrentlyContext context) {
                messages.forEach(msg ->{

                    String bodyString = new String(msg.getBody());
                    JSONObject bodyObject = JSONObject.parseObject(bodyString);
                    log.info("接收方获取到的消息,{}",bodyObject);
                    // 扣减库存
                    ComplexRocketmqStockDO complexRocketmqStockDO = complexRocketmqStockMapper.selectOne(new QueryWrapper<ComplexRocketmqStockDO>()
                            .lambda()
                            .eq(ComplexRocketmqStockDO::getCommodityId,bodyObject.get("commodityId")));
                    complexRocketmqStockDO.setCommodityStock(complexRocketmqStockDO.getCommodityStock() - Long.parseLong(bodyObject.get("commodityAmount").toString()));
                    complexRocketmqStockMapper.updateById(complexRocketmqStockDO);
                });
                log.info("接收方消息处理成功");
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        // 启动消费者
        consumer.start();
        System.out.printf("Consumer Started.%n");

    }

}
