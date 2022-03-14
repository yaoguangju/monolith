package com.mochen.edudata.common.security;

import com.alibaba.fastjson.JSON;
import com.mochen.core.common.xbo.Result;
import com.mochen.edudata.common.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 授权过程中出现异常
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) {

        Result result = new Result(40002, "权限不足");
        String json = JSON.toJSONString(result);
        WebUtils.renderString(response,json);
    }
}
