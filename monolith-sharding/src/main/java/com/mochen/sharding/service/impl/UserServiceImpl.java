package com.mochen.sharding.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mochen.core.exception.CommonException;
import com.mochen.redis.common.manager.RedisManager;
import com.mochen.sharding.entity.dto.LoginDTO;
import com.mochen.sharding.entity.vo.LoginVO;
import com.mochen.sharding.entity.xdo.UserDO;
import com.mochen.sharding.mapper.UserMapper;
import com.mochen.sharding.security.JwtManager;
import com.mochen.sharding.security.SecurityUser;
import com.mochen.sharding.service.IUserService;
import org.springframework.beans.BeanUtils;
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

        // 用户登录前的前置判断，比如验证码等，判断通过后找到需要验证的账号密码
        // 将用户账号（可能多种类型），密码传入Security系统
        // Security系统的UserDetailsServiceImpl会调用loadUserByUsername的方法去数据源查询该用户，封装成UserDetails对象
        // Security系统调用UsernamePasswordAuthenticationToken生成需要验权的user对象
        // 将UserDetails对象和user对象做对比，如果成功便通过
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),loginDTO.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        if(Objects.isNull(authenticate)){
            throw new CommonException("用户名或密码错误");
        }
        //使用userid生成token
        SecurityUser securityUser = (SecurityUser) authenticate.getPrincipal();
        Long userId = securityUser.getUser().getId();
        Long year = securityUser.getUser().getYear();

        String token = jwtManager.createToken(userId,year);
        redisManager.setCacheObject("login:"+ userId,securityUser);
        //封装前端对象
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        LoginVO.UserVO userVO = new LoginVO.UserVO();
        BeanUtils.copyProperties(securityUser.getUser(),userVO);
        loginVO.setUserVO(userVO);

        return loginVO;
    }

    @Override
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Long userid = securityUser.getUser().getId();
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
