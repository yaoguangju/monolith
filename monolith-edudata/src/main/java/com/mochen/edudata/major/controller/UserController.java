package com.mochen.edudata.major.controller;


import com.mochen.core.common.xbo.Result;
import com.mochen.core.exception.CommonException;
import com.mochen.edudata.major.entity.dto.LoginDTO;
import com.mochen.edudata.major.entity.vo.LoginVO;
import com.mochen.edudata.major.service.IUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 学生 前端控制器
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-12
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO) throws CommonException {
        LoginVO loginVO = userService.login(loginDTO);
        return Result.success(loginVO);
    }

    @PostMapping("/logout")
    public Result logout() throws CommonException {
        userService.logout();
        return Result.success();
    }
}

