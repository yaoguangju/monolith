package com.mochen.resource.common.init;


import com.mochen.resource.service.ICanalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Slf4j
@Component
public class SpringInitRunner implements ApplicationRunner {

    @Resource
    private ICanalService canalService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("项目初始化时启动。。");
//        canalService.synchronousData();
    }

}
