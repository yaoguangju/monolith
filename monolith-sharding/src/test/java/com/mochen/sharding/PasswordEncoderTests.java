package com.mochen.sharding;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.mochen.sharding.entity.xdo.UserDO;
import com.mochen.sharding.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@Slf4j
public class PasswordEncoderTests {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testCreatePassword(){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String s = passwordEncoder.encode("123");
        log.info("生成的密文密码为，{}",s);
    }

    @Test
    @DS("analysis")
    public void testCreatePasswordToDataBase(){
        List<UserDO> userDOList = userMapper.selectList(null);
        userDOList.forEach(UserDO -> {
            this.createPassword(UserDO.getId());
        });

    }

    @Async("asyncServiceExecutor")
    @DS("analysis")
    void createPassword(Long id){
        UserDO userDO = userMapper.selectById(id);
        String analysisNo = userDO.getAnalysisNo();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String s = passwordEncoder.encode(analysisNo.substring(analysisNo.length() - 6));
        userDO.setPassword(s);
        userMapper.updateById(userDO);
    }
}
