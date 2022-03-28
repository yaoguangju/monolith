package com.mochen.complex.quartz.job;

import cn.hutool.core.date.DateUtil;
import com.mochen.complex.quartz.job.base.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;

/**
 * <p>
 * Hello Job
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2018-11-26 13:22
 */
@Slf4j
public class HelloJob implements BaseJob {


    @Override
    public void execute(JobExecutionContext context) {
        JobDetail jobDetail = context.getJobDetail();
        log.info("Hello Job 执行时间: {}", DateUtil.now());
        log.info("执行时间参数: {}", jobDetail.getJobDataMap().get("param"));
    }
}
