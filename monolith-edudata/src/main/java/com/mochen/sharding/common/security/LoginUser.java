package com.mochen.sharding.common.security;


import com.alibaba.fastjson.annotation.JSONField;
import com.mochen.sharding.major.entity.xdo.UserDO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {

    private UserDO user;

    //存储权限信息
    private List<String> permissions;

    public LoginUser(UserDO user, List<String> list) {
        this.user = user;
        this.permissions = list;
    }




    //存储SpringSecurity所需要的权限信息的集合
    @JSONField(serialize = false)
    private Collection<SimpleGrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(authorities!=null){
            return authorities;
        }
        //把permissions中字符串类型的权限信息转换成GrantedAuthority对象存入authorities中
        authorities = permissions.stream().
                map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getUsername() {
        if(StringUtils.isNotBlank(user.getPhone())){
            return user.getPhone();
        }
        return user.getAnalysisNo();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}

