package com.mochen.sharding.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.mochen.core.exception.CommonException;
import com.mochen.redis.common.manager.RedisManager;
import com.mochen.sharding.entity.dto.LoginDTO;
import com.mochen.sharding.entity.vo.LoginVO;
import com.mochen.sharding.entity.xdo.UserDO;
import com.mochen.sharding.mapper.UserMapper;
import com.mochen.sharding.security.JwtManager;
import com.mochen.sharding.security.SecurityUser;
import com.mochen.sharding.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 学生 服务实现类
 * </p>
 *
 * @author 姚广举
 * @since 2022-03-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements IUserService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtManager jwtManager;

    @Resource
    private RedisManager redisManager;

    @Resource
    private UserMapper userMapper;

    @Override
    public LoginVO login(LoginDTO loginDTO) throws CommonException {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getAnalysisNo(),loginDTO.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        if(Objects.isNull(authenticate)){
            throw new CommonException("用户名或密码错误");
        }
        //使用userid生成token
        SecurityUser securityUser = (SecurityUser) authenticate.getPrincipal();
        Long userId = securityUser.getUserDO().getId();

        String token = jwtManager.createToken(userId);
        //authenticate存入redis
        redisManager.setCacheObject("login:"+userId,securityUser);
        //封装前端对象
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        LoginVO.UserVO userVO = new LoginVO.UserVO();
        BeanUtils.copyProperties(securityUser.getUserDO(),userVO);
        loginVO.setUserVO(userVO);

        return loginVO;
    }

    @Override
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Long userid = securityUser.getUserDO().getId();
        redisManager.deleteObject("login:"+userid);
    }

    @Override
    public void createPassword() {
        List<UserDO> userDOList = userMapper.selectList(null);
        List<UserDO> userDOS = new ArrayList<>();
        userDOList.forEach(userDO -> {
            UserDO userDOSave = new UserDO();
               BeanUtils.copyProperties(userDO,userDOSave);
            String analysisNo = userDO.getAnalysisNo();
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String s = passwordEncoder.encode(analysisNo.substring(analysisNo.length() - 6));
            userDOSave.setPassword(s);
            userDOS.add(userDOSave);
        });
        this.updateBatchById(userDOS,10000);
    }


}
