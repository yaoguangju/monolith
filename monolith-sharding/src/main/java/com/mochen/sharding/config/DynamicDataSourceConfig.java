package com.mochen.sharding.config;

import com.mochen.sharding.common.contanst.DataSourceConstants;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@Configuration
@PropertySource("classpath:config/jdbc.properties")
@MapperScan(basePackages = "me.mason.demo.dynamicdatasource.mapper")
public class DynamicDataSourceConfig {

    @Bean(DataSourceConstants.DS_KEY_2019)
    @ConfigurationProperties(prefix = "spring.datasource.student2019")
    public DataSource student2019DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(DataSourceConstants.DS_KEY_2020)
    @ConfigurationProperties(prefix = "spring.datasource.student2020")
    public DataSource student2020DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(DataSourceConstants.DS_KEY_2021)
    @ConfigurationProperties(prefix = "spring.datasource.student2021")
    public DataSource student2021DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public DataSource dynamicDataSource() {
        Map<Object, Object> dataSourceMap = new HashMap<>(16);
        dataSourceMap.put(DataSourceConstants.DS_KEY_2019, student2019DataSource());
        dataSourceMap.put(DataSourceConstants.DS_KEY_2020, student2020DataSource());
        dataSourceMap.put(DataSourceConstants.DS_KEY_2021, student2021DataSource());
        //设置动态数据源
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(dataSourceMap);
        dynamicDataSource.setDefaultTargetDataSource(student2019DataSource());

        return dynamicDataSource;
    }
}
