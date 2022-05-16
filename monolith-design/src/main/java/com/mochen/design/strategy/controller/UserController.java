package com.mochen.design.strategy.controller;

import com.mochen.design.strategy.entity.User;
import com.mochen.design.strategy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Qualifier("userServiceImpl")
    @Autowired
    UserService userService;

    @GetMapping("/userType")
    public String userType(User user){
        return userService.Service(user);
    }
}
