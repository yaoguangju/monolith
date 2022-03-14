package com.mochen.edudata.common.datasource;

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
        String year = request.getHeader("year");
        String dsKey = "edudata";
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

        //执行
        try {
            DynamicDataSourceContextHolder.push(dsKey);
            filterChain.doFilter(request, response);
        } finally {
            DynamicDataSourceContextHolder.poll();
        }
    }
}
