package com.mochen.kafka.controller;

import com.mochen.kafka.manager.KafkaSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    @Autowired
    private KafkaSender kafkaSender;

    @GetMapping("sendMessage/{msg}")
    public void sendMessage(@PathVariable("msg") String msg){
        kafkaSender.send(msg);
    }
}
