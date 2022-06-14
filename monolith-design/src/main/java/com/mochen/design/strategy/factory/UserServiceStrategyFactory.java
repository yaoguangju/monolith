package com.mochen.design.strategy.factory;

import com.mochen.design.strategy.service.UserService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserServiceStrategyFactory {

    private static final Map<String, UserService> services = new ConcurrentHashMap<>();
    //根据用户类型获取对应服务
    public static UserService getByUserType(String userType){
        return services.get(userType);
    }
    //根据用户类型存储服务
    public static void register(String userType,UserService userPayService){
        services.put(userType,userPayService);
    }
}
