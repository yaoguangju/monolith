package com.mochen.rocketmq.controller;

import com.alibaba.fastjson.JSON;
import com.mochen.rocketmq.entity.xdo.ComplexRocketmqOrderDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/rocketmq")
@Slf4j
public class RocketMqController {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送普通消息（上面的发送同步消息，delayLevel的值就为0，因为不延时）
     */
    @GetMapping("/send")
    public void send(){
        rocketMQTemplate.send("topic1:tag1", MessageBuilder.withPayload("普通消息").build());
    }

    /**
     * 发送延时消息（上面的发送同步消息，delayLevel的值就为0，因为不延时）
     * 在start版本中 延时消息一共分为18个等级分别为：1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
     */
    @GetMapping("/sendDelayMsg")
    public void sendDelayMsg() {
        rocketMQTemplate.syncSend("topic1:tag1",
                MessageBuilder.withPayload("延时消息").build(),
                10000,
                2);
    }


    /**
     * 发送同步消息（阻塞当前线程，等待broker响应发送结果，这样不太容易丢失消息）
     * （msgBody也可以是对象，sendResult为返回的发送结果）
     */
    @GetMapping("/sendMsg")
    public void sendMsg() {
        SendResult sendResult = rocketMQTemplate.syncSend("topic1:tag1", MessageBuilder.withPayload("同步消息").build());
        log.info("【sendMsg】sendResult={}", JSON.toJSONString(sendResult));
    }

    /**
     * 发送异步消息（通过线程池执行发送到broker的消息任务，执行完后回调：在SendCallback中可处理相关成功失败时的逻辑）
     * （适合对响应时间敏感的业务场景）
     */
    @GetMapping("/sendAsyncMsg")
    public void sendAsyncMsg() {
        rocketMQTemplate.asyncSend("topic1:tag1", MessageBuilder.withPayload("异步消息").build(), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                // 处理消息发送成功逻辑
                System.out.println("消息发送成功了");
            }
            @Override
            public void onException(Throwable throwable) {
                // 处理消息发送异常逻辑
                System.out.println("消息发送失败了");
            }
        });
    }

    /**
     * 发送单向消息（只负责发送消息，不等待应答，不关心发送结果，如日志）
     */
    @GetMapping("/sendOneWayMsg")
    public void sendOneWayMsg() {
        rocketMQTemplate.sendOneWay("topic1:tag1", MessageBuilder.withPayload("单向消息").build());
    }

    /**
     * 发送事务消息
     */
    @GetMapping("/sendTransactionMessage")
    public void sendTransactionMessage(){
        // 设置下订单的消息
        ComplexRocketmqOrderDO complexRocketmqOrderDO = new ComplexRocketmqOrderDO();
        complexRocketmqOrderDO.setCommodityId(1L);
        complexRocketmqOrderDO.setCommodityAmount(10L);
        // 将消息转换为消息头
        Message<String> msg = MessageBuilder.withPayload(JSON.toJSONString(complexRocketmqOrderDO)).build();
        rocketMQTemplate.sendMessageInTransaction("transaction-rocketmq",msg,null);
    }

}
