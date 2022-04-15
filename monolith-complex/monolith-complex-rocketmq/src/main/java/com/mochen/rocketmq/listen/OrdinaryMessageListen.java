package com.mochen.rocketmq.listen;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

// MessageExt：是一个消息接收通配符，不管发送的是String还是对象，都可接收，当然也可以像上面明确指定类型（我建议还是指定类型较方便）
@Service
@Slf4j
@RocketMQMessageListener(topic = "topic1", selectorExpression = "tag1", consumerGroup = "Con_Group")
public class OrdinaryMessageListen implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt messageExt) {
        byte[] body = messageExt.getBody();
        String msg = new String(body);
        log.info("监听到消息：msg={}", msg);
    }
}
