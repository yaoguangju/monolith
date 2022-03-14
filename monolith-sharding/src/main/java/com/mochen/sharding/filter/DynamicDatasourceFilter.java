package com.mochen.sharding.filter;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
//@WebFilter(filterName = "dsFilter", urlPatterns = {"/*"})
public class DynamicDatasourceFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("loading filter {}", filterConfig.getFilterName());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = request.getHeader("token");
        String dsKey = "analysis";
        if(StringUtils.isNotBlank(token)){
            JWT jwt = JWTUtil.parseToken(token);
            String year = jwt.getPayload("year").toString();

            switch(year){
                case "2019":
                    dsKey =  "student2019";
                    break;
                case "2020":
                    dsKey =  "student2020";
                    break;
                case "2021":
                    dsKey =  "student2021";
                    break;
                default:
                    dsKey =  "analysis";
            }
        }

        //执行
        try {
            DynamicDataSourceContextHolder.push(dsKey);
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            DynamicDataSourceContextHolder.poll();
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
