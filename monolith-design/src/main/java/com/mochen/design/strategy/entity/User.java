package com.mochen.design.strategy.entity;

import lombok.Data;

@Data
public class User {

    String name; //名称
    String userType; //类型，

    public User(String name, String userType) {
        this.name = name;
        this.userType = userType;
    }
}
