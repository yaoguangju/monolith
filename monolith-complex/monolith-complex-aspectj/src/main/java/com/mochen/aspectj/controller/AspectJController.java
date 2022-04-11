package com.mochen.aspectj.controller;

import com.mochen.aspectj.annoation.MethodExporter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/aspectJ")
public class AspectJController {

    @MethodExporter
    @GetMapping("/list")
    public String list() {
        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "123";
    }
}
