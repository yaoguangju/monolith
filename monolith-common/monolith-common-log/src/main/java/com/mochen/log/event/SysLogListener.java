package com.mochen.log.event;

import com.alibaba.fastjson.JSON;
import com.mochen.log.entity.OptLogDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 异步监听日志事件
 */
@Slf4j
@AllArgsConstructor
@Component
public class SysLogListener {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Async
    @Order
    @EventListener(SysLogEvent.class)
    public void saveSysLog(SysLogEvent event) {
        OptLogDTO optLog = (OptLogDTO) event.getSource();
        // TODO 推送到日志组件
        kafkaTemplate.send("log", JSON.toJSONString(optLog));
    }
}
