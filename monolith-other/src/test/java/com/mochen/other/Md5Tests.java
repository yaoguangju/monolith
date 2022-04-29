package com.mochen.other;

import cn.hutool.crypto.SecureUtil;
import org.junit.jupiter.api.Test;

//@SpringBootTest
public class Md5Tests {

    @Test
    public void md5Test(){
        String s = SecureUtil.md5("itlaoqi");
        System.out.println(s);

    }

    @Test
    public void TestStringToInteger(){
        String a = "123";
        Integer b = Integer.valueOf(a);
        System.out.println(b);

    }
}
