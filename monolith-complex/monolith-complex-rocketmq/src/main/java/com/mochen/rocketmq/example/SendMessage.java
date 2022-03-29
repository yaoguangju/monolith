package com.mochen.rocketmq.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.CountDownLatch2;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class SendMessage {

    public static void main(String[] args) throws MQBrokerException, RemotingException, UnsupportedEncodingException, InterruptedException, MQClientException {
//        test1();
//        test2();
//        test3();
//        test4();
        test5();

    }

    /**
     * 发送同步消息
     */
    private static void test1() throws MQClientException, UnsupportedEncodingException, MQBrokerException, RemotingException, InterruptedException{
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_name");
        // 设置NameServer的地址
        producer.setNamesrvAddr("192.168.164.128:9876");
        // 启动Producer实例
        producer.start();

        Message msg = new Message("TopicTest" /* Topic */,
                "TagA" /* Tag */,
                ("Hello RocketMQ 123").getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
        );
        // 发送消息到一个Broker
        SendResult sendResult = producer.send(msg);
        // 通过sendResult返回消息是否成功送达
        System.out.printf("%s%n", sendResult);
        // 如果不再发送消息，关闭Producer实例。
        producer.shutdown();
    }

    /**
     * 发送异步消息
     */
    private static void test2() throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException{
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_name");
        // 设置NameServer的地址
        producer.setNamesrvAddr("192.168.164.128:9876");
        // 启动Producer实例
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);

        int messageCount = 100;
        // 根据消息数量实例化倒计时计算器
        final CountDownLatch2 countDownLatch = new CountDownLatch2(messageCount);
        for (int i = 0; i < messageCount; i++) {
            final int index = i;
            // 创建消息，并指定Topic，Tag和消息体
            Message msg = new Message("TopicTest",
                    "TagA",
                    "OrderID188",
                    "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
            // SendCallback接收异步返回结果的回调
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    countDownLatch.countDown();
                    System.out.printf("%-10d OK %s %n", index,
                            sendResult.getMsgId());
                }
                @Override
                public void onException(Throwable e) {
                    countDownLatch.countDown();
                    System.out.printf("%-10d Exception %s %n", index, e);
                    e.printStackTrace();
                }
            });
        }
        // 等待5s
        countDownLatch.await(5, TimeUnit.SECONDS);
        // 如果不再发送消息，关闭Producer实例。
        producer.shutdown();
    }

    /**
     * 单向发送消息（没有返回结果）
     */
    public static void test3() throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException {
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_name");
        // 设置NameServer的地址
        producer.setNamesrvAddr("192.168.164.128:9876");
        // 启动Producer实例
        producer.start();
        // 创建消息，并指定Topic，Tag和消息体
        Message msg = new Message("TopicTest" /* Topic */,
                "TagA" /* Tag */,
                ("Hello RocketMQ ").getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
        );
        // 发送单向消息，没有任何返回结果
        producer.sendOneway(msg);

        // 如果不再发送消息，关闭Producer实例。
        producer.shutdown();
    }


    /**
     * 发送顺序消息
     */
    public static void test4() throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_name");

        producer.setNamesrvAddr("192.168.164.128:9876");

        producer.start();

        for (Integer orderId = 0; orderId <= 10; orderId++) {
            for (int i = 0; i < 3; i++) {
                String data = "";
                switch (i % 3){
                    case 0:
                        data = orderId + "号创建订单";
                        break;
                    case 1:
                        data = orderId + "号订单减库存";
                        break;
                    case 2:
                        data = orderId + "号订单加积分";
                        break;
                }
                Message msg = new Message("TopicTest", "order", orderId.toString(), data.getBytes(StandardCharsets.UTF_8));
                SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                        int orderId = Integer.parseInt(msg.getKeys());  //根据订单id选择发送queue
                        // 配置文件里面配的
                        log.info("队列数量{}",mqs.size());
                        int index = orderId % mqs.size();
                        MessageQueue messageQueue = mqs.get(index);
                        log.info("id:{},data:{},quese:{}",orderId,new String(msg.getBody()),messageQueue);
                        return mqs.get(index);
                    }
                }, null);
            }
        }

        producer.shutdown();
    }

    /**
     * 发送延迟消息
     */
    public static void test5() throws MQClientException, RemotingException, InterruptedException, MQBrokerException, UnsupportedEncodingException {

        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_name");
        // 设置NameServer的地址
        producer.setNamesrvAddr("192.168.164.128:9876");
        // 启动Producer实例
        producer.start();
        // 创建消息，并指定Topic，Tag和消息体
        Message msg = new Message("TopicTest" /* Topic */,
                ("发送延迟消息5秒钟后会到达").getBytes() /* Message body */
        );
        // 共有12个等级 "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";
        msg.setDelayTimeLevel(2);
        // 发送单向消息，没有任何返回结果
        SendResult send = producer.send(msg);
        System.out.printf("%s%n", send);
        // 如果不再发送消息，关闭Producer实例。
        producer.shutdown();

    }

}
