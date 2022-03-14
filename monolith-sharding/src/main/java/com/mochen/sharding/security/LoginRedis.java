package com.mochen.sharding.security;

import lombok.Data;

@Data
public class LoginRedis {

    private Long id;
    private String username;
    private String password;
    private String role;
    private String name;
    private Long schoolId;
    private Long year;
}
