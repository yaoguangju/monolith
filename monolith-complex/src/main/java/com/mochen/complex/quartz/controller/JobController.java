package com.mochen.complex.quartz.controller;

import com.alibaba.fastjson.JSON;
import com.mochen.complex.quartz.entity.dto.JobDTO;
import com.mochen.complex.quartz.entity.xdo.JobAndTrigger;
import com.mochen.complex.quartz.job.HelloJob;
import com.mochen.complex.quartz.job.base.BaseJob;
import com.mochen.complex.quartz.mapper.JobMapper;
import com.mochen.complex.quartz.util.JobUtil;
import com.mochen.core.common.xbo.Result;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/quartz")
@Slf4j
public class JobController {

    @Resource
    private Scheduler scheduler;

    @Resource
    private JobMapper jobMapper;
    /**
     * 保存定时任务
     */
    @PostMapping("addJob")
    public Result addJob(@RequestBody JobDTO jobDTO) throws Exception {
        // 启动调度器
        scheduler.start();

        // 构建Job信息
        JobDetail jobDetail = JobBuilder
                .newJob(JobUtil.getClass(jobDTO.getJobClass()).getClass())
                .usingJobData("param",jobDTO.getParam().getName())
                .withIdentity(jobDTO.getJobName(), jobDTO.getGroupName())
                .build();

        // Cron表达式调度构建器(即任务执行的时间)
        CronScheduleBuilder cron = CronScheduleBuilder.cronSchedule(jobDTO.getCronExpression());

        //根据Cron表达式构建一个Trigger
        CronTrigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity(jobDTO.getJobName(), jobDTO.getGroupName())
                .withSchedule(cron)
                .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error("【定时任务】创建失败！", e);
        }

        return Result.success();
    }

    /**
     * 暂定定时任务
     */
    @PostMapping("pauseJob")
    public Result pauseJob(@RequestBody JobDTO jobDTO) throws SchedulerException {

        scheduler.pauseJob(JobKey.jobKey(jobDTO.getJobName(), jobDTO.getGroupName()));

        return Result.success();
    }

    /**
     * 恢复定时任务
     */
    @PostMapping("resumeJob")
    public Result resumeJob(@RequestBody JobDTO jobDTO) throws SchedulerException {
        scheduler.resumeJob(JobKey.jobKey(jobDTO.getJobName(), jobDTO.getGroupName()));
        return Result.success();
    }

    /**
     * 删除定时任务
     */
    @PostMapping("deleteJob")
    public Result deleteJob(@RequestBody JobDTO jobDTO) throws SchedulerException {
        scheduler.pauseTrigger(TriggerKey.triggerKey(jobDTO.getJobName(), jobDTO.getGroupName()));
        scheduler.unscheduleJob(TriggerKey.triggerKey(jobDTO.getJobName(), jobDTO.getGroupName()));
        scheduler.deleteJob(JobKey.jobKey(jobDTO.getJobName(), jobDTO.getGroupName()));
        return Result.success();
    }

    /**
     * 重新配置定时任务
     */
    @PostMapping("cronJob")
    public Result cronJob(@RequestBody JobDTO jobDTO) {
        try {
            TriggerKey triggerKey = TriggerKey
                    .triggerKey(jobDTO.getJobName(), jobDTO.getGroupName());
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobDTO.getCronExpression());

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            // 根据Cron表达式构建一个Trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            log.error("【定时任务】更新失败！", e);
        }
        return Result.success();
    }



    /**
     * 定时任务列表
     */
    @PostMapping("list")
    public Result list() {
        List<JobAndTrigger> list = jobMapper.list();
        return Result.success(list);
    }


}
