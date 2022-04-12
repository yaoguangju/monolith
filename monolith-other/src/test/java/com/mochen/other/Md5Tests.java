package com.mochen.other;

import cn.hutool.crypto.SecureUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Md5Tests {

    @Test
    public void md5Test(){
        String s = SecureUtil.md5("itlaoqi");
        System.out.println(s);

    }
}
