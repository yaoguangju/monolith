package com.mochen.log.aspect;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mochen.core.common.xbo.Result;
import com.mochen.log.entity.OptLogDTO;
import com.mochen.log.event.SysLogEvent;
import com.mochen.log.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

/**
 * 操作日志使用spring event异步入库
 *
 */
@Slf4j
@Aspect
public class SysLogAspect {

    /**
     * 事件发布是由ApplicationContext对象管控的，我们发布事件前需要注入ApplicationContext对象调用publishEvent方法完成事件发布
     **/
    @Autowired
    private ApplicationContext applicationContext;

    private static final ThreadLocal<OptLogDTO> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 切入点
     */
    @Pointcut("@annotation(com.mochen.log.annotation.SysLog)")
    public void sysLogAspect() {

    }

    private OptLogDTO get() {
        OptLogDTO sysLog = THREAD_LOCAL.get();
        if (sysLog == null) {
            return new OptLogDTO();
        }
        return sysLog;
    }


    @Before("sysLogAspect()")
    public void deAround(JoinPoint joinPoint) {
        OptLogDTO sysLog = get();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 请求ip
        sysLog.setRequestIp(request.getRemoteAddr());
        // 日志类型
        sysLog.setType("OPT");
        // 类路径
        sysLog.setClassPath(joinPoint.getTarget().getClass().getName());
        // 执行的方法名
        sysLog.setActionMethod(joinPoint.getSignature().getName());
        // 请求地址
        sysLog.setRequestUri(request.getRequestURL().toString());
        // GET or POST
        sysLog.setHttpMethod(request.getMethod());
        // 请求参数
        Object[] args = joinPoint.getArgs();
        String strArgs = "";
        try {
            if (!request.getContentType().contains("multipart/form-data")) {
                strArgs = JSONObject.toJSONString(args);
            }
        } catch (Exception e) {
            try {
                strArgs = Arrays.toString(args);
            } catch (Exception ex) {
                log.warn("解析参数异常", ex);
            }
        }
        sysLog.setParams(getText(strArgs));
        sysLog.setStartTime(LocalDateTime.now());
        THREAD_LOCAL.set(sysLog);
    }

    /**
     * 返回通知
     *
     * @param ret
     */
    @AfterReturning(returning = "ret", pointcut = "sysLogAspect()")
    public void doAfterReturning(Object ret) {
        Result r = Convert.convert(Result.class, ret);
        OptLogDTO sysLog = get();
        sysLog.setType("OPT");
        sysLog.setResult(getText(r.toString()));
        publishEvent(sysLog);
    }

    /**
     * 异常通知
     *
     * @param e
     */
    @AfterThrowing(pointcut = "sysLogAspect()", throwing = "e")
    public void doAfterThrowable(Throwable e) {
        OptLogDTO sysLog = get();
        sysLog.setType("EX");
        // 异常对象
        sysLog.setExDetail(LogUtil.getStackTrace(e));
        // 异常信息
        sysLog.setExDesc(e.getMessage());
        publishEvent(sysLog);
    }


    private void publishEvent(OptLogDTO sysLog) {
        sysLog.setFinishTime(LocalDateTime.now());
        sysLog.setConsumingTime(sysLog.getStartTime().until(sysLog.getFinishTime(), ChronoUnit.MILLIS));
        applicationContext.publishEvent(new SysLogEvent(sysLog));
        THREAD_LOCAL.remove();
    }

    /**
     * 截取指定长度的字符串
     *
     * @param val
     * @return
     */
    private String getText(String val) {
        return StrUtil.sub(val, 0, 65535);
    }


}
