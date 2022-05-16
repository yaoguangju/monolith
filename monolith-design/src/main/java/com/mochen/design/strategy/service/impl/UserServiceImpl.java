package com.mochen.design.strategy.service.impl;

import com.mochen.design.strategy.entity.User;
import com.mochen.design.strategy.factory.UserServiceStrategyFactory;
import com.mochen.design.strategy.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public String Service(User user) {
        UserService byUserType = UserServiceStrategyFactory.getByUserType(user.getUserType());
        return byUserType.Service(user);
    }

}
