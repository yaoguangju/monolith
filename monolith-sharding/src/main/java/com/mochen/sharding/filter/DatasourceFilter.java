package com.mochen.sharding.filter;

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
public class DatasourceFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String year = request.getHeader("year");
        String dsKey = "analysis";
        if(StringUtils.isNotBlank(year)){
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
                case "0":
                default:
                    dsKey =  "analysis";
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
