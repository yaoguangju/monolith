package com.mochen.edudata.common.datasource;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class DynamicDatasourceFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        String dsKey = "edudata";
        // 如果有token有可能需要切换数据源
        if(StringUtils.isNotBlank(token)){
            JWT jwt = JWTUtil.parseToken(token);
            String year = jwt.getPayload("year").toString();
            // 如果能解析出year，说明需要切换数据源
            if(StringUtils.isNotBlank(year)){
                switch(year){
                    case "2019":
                        dsKey =  "edudata2019";
                        break;
                    case "2020":
                        dsKey =  "edudata2020";
                        break;
                    case "2021":
                        dsKey =  "edudata2021";
                        break;
                    default:
                        dsKey =  "edudata";
                }
            }
        }
        //执行
        try {
            DynamicDataSourceContextHolder.push(dsKey);
            filterChain.doFilter(request, response);
        } finally {
            DynamicDataSourceContextHolder.poll();
        }
    }
}
