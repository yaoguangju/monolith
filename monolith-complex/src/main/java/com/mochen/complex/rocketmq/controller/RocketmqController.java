package com.mochen.complex.rocketmq.controller;


import com.alibaba.fastjson.JSON;
import com.mochen.complex.rocketmq.entity.xdo.ComplexRocketmqOrderDO;
import com.mochen.complex.rocketmq.service.impl.TransactionListenerImpl;
import com.mochen.core.common.xbo.Result;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

@RestController
@RequestMapping("rocketmq")
@Slf4j
public class RocketmqController {

    @Resource
    private TransactionListenerImpl transactionListener;

    @GetMapping("/sendTransaction")
    public Result sendTransaction() throws MQClientException {
        TransactionMQProducer producer = new TransactionMQProducer("affair");
        producer.setNamesrvAddr("192.168.164.128:9876");
        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("client-transaction-msg-check-thread");
                return thread;
            }
        });
        producer.setExecutorService(executorService);

        producer.setTransactionListener(transactionListener);
        // 启动Producer实例
        producer.start();

        // 设置下订单的消息
        ComplexRocketmqOrderDO complexRocketmqOrderDO = new ComplexRocketmqOrderDO();
        complexRocketmqOrderDO.setCommodityId(1L);
        complexRocketmqOrderDO.setCommodityAmount(10L);
        // 将消息转换为消息头
        byte[] body = JSON.toJSONString(complexRocketmqOrderDO).getBytes(StandardCharsets.UTF_8);

        Message msg = new Message("TopicTest2", "TAGA", body);
        SendResult sendResult = producer.sendMessageInTransaction(msg, null);
        log.info("消息结果为{},",sendResult.getSendStatus());
        // 事务消息要注释掉
//        producer.shutdown();
        return Result.success();
    }
}
