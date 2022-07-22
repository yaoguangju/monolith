package com.mochen.utils;

import cn.hutool.core.text.CharSequenceUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.MessageFormat;

/**
 * @author 姚广举
 * @version 创建时间：
 * @mail 邮箱: guangju.yao@casstime.com
 */
@SpringBootTest
public class JavaTests {

    @Test
    void charSequenceUtil(){
        String text = MessageFormat.format("用户:{0}您好,短信验证码为:{1}", "张三", "10086");
        System.out.println(text);
    }
}
