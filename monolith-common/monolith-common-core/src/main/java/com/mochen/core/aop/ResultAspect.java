package com.mochen.core.aop;

import com.mochen.core.exception.ControllerExceptionAdvice;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * @author 姚广举
 * @theme 结果切面
 * @date 2020-11-20
 */
@Aspect
@Component
@Slf4j
public class ResultAspect {

    /** 换行符 */
    private static final String LINE_SEPARATOR = System.lineSeparator();

    // 切入点
    @Pointcut("execution(public com.mochen.core.common.xbo.Result *(..))")
    public void ResultAspect() {

    }

    // 环绕通知，参数必须为ProceedingJoinPoint,proceed就是被切面的方法
    @Around("ResultAspect()")
    public Object deAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        // 执行耗时
        log.info("Time-Consuming : {} ms", System.currentTimeMillis() - startTime);
        log.info("====== End ======" + LINE_SEPARATOR);
        return result;
    }

    /**
     * 在切点之前织入
     */
    @Before("ResultAspect()")
    public void doBefore(JoinPoint joinPoint) {
        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 打印请求相关参数
        log.info("====== Start ======");
        // 打印请求 url
        log.info("URL            : {}", request.getRequestURL().toString());
        // 打印 Http method
        log.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        // 打印请求的 IP
        log.info("IP             : {}", request.getRemoteAddr());
    }


}

