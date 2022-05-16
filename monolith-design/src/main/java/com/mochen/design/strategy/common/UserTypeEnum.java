package com.mochen.design.strategy.common;

public enum UserTypeEnum {
    PROXY("代理"),
    CUSTOMER("消费者"),
    FACTORY("工厂");
    private final String userType;

    UserTypeEnum(String userType) {
        this.userType = userType;
    }
}
