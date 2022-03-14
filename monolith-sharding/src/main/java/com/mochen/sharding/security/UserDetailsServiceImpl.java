package com.mochen.sharding.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mochen.sharding.entity.xdo.UserDO;
import com.mochen.sharding.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 自定义userDetailsService - 认证用户详情
 * </p>
 *
 * @author qy
 * @since 2019-11-08
 *
 *
 * Spring Security在进行认证的时候，会UserDetailsService接口的loadUserByUsername方法，去查询应该有用户信息
 * 然后封装成UserDetails对象，如果然后根据自定义的密码规则匹配客户端传来的密码是否正确
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    /***
     * 根据账号获取用户信息
     * @param username:
     * @return: org.springframework.security.core.userdetails.UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecurityUser securityUser = new SecurityUser();

        QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
        if(username.startsWith("JN")){
            wrapper.eq("analysis_no", username);
            UserDO userDO = userMapper.selectOne(wrapper);
            securityUser.setId(userDO.getId());
            securityUser.setUsername(userDO.getAnalysisNo());
            securityUser.setPassword(userDO.getPassword());
            securityUser.setRole(userDO.getRole());

            securityUser.setName(userDO.getName());
            securityUser.setSchoolId(userDO.getSchoolId());
            securityUser.setYear(userDO.getYear());
        }else {
            wrapper.eq("phone", username);
            UserDO userDO = userMapper.selectOne(wrapper);
            securityUser.setId(userDO.getId());
            securityUser.setUsername(userDO.getPhone());
            securityUser.setPassword(userDO.getPassword());
            securityUser.setRole(userDO.getRole());

            securityUser.setName(userDO.getName());
            securityUser.setSchoolId(userDO.getSchoolId());
            securityUser.setYear(userDO.getYear());
        }
        return securityUser;

    }

}
