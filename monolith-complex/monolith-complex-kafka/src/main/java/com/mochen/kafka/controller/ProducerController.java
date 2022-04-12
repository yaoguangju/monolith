package com.mochen.kafka.controller;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/kafka")
public class ProducerController {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/data")
    public String data(@RequestParam String msg) {
        kafkaTemplate.send("first", msg);
        return "ok";
    }
}
