package com.mochen.aspectj.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
@Slf4j
public class MethodExporterAspect {

    @Around("@annotation(com.mochen.aspectj.annoation.MethodExporter)")
    public Object MethodExporter(ProceedingJoinPoint joinPoint) throws Throwable {
        long st = new Date().getTime();
        // 执行目标方法
        Object proceed = joinPoint.proceed();
        long et = new Date().getTime();
        ObjectMapper mapper  = new ObjectMapper();
        //将入参JSON序列化
        String jsonParam = mapper.writeValueAsString(joinPoint.getArgs());
        // 将返回结果JSON序列化
        String jsonResult;
        if(proceed != null){
            jsonResult = mapper.writeValueAsString(proceed);
        }else {
            jsonResult = "null";
        }

        log.info("正在上包服务器过程:\ntarget:{}.{}()\nexecution:{}ms,\nparameter:{}\nresult:{}"
                ,joinPoint.getTarget().getClass().getSimpleName()
        ,joinPoint.getSignature().getName()
        ,(et-st)
        ,jsonParam
        ,jsonResult);
        return proceed;

    }

}
