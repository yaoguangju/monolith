package com.mochen.complex.quartz.config;

import com.mochen.complex.quartz.job.MyTaskJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Date;

@Configuration
public class QuartzConfig {


    @Resource
    private Scheduler scheduler;

    @Bean
    public void config() throws SchedulerException {

        JobDetail jobDetail = JobBuilder.newJob(MyTaskJob.class)
                // 任务标识，及任务分组
                .withIdentity("job1", "group1")
                // 链接调用，增加需要的参数
                .usingJobData("name","Java旅途")
                .usingJobData("age",18)
                .build();

        SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger()
//                .withIdentity("job1", "group1")
                .forJob("job1", "group1")
                // 立即执行
                .startNow()
                // 10s后停止
                .endAt(new Date(System.currentTimeMillis()+10*1000))
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                // 每秒执行一次
                                .withIntervalInSeconds(10)
                                // 一直执行
                                .repeatForever()
                )
                .build();

        scheduler.scheduleJob(jobDetail,simpleTrigger);
    }
}
