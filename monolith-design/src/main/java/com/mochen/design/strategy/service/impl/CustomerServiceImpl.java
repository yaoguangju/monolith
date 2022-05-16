package com.mochen.design.strategy.service.impl;

import com.mochen.design.strategy.common.UserTypeEnum;
import com.mochen.design.strategy.entity.User;
import com.mochen.design.strategy.factory.UserServiceStrategyFactory;
import com.mochen.design.strategy.service.UserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements UserService, InitializingBean {
    @Override
    public String Service(User user) {
        return "用户类型："+user.getUserType()+"，可以获得消费者服务";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        UserServiceStrategyFactory.register(UserTypeEnum.CUSTOMER.name(),this); //注册Bean
    }
}
