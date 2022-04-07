package com.mochen.mp;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class RedisTests {

    @Resource
    private RedisTemplate<String, Long> redisTemplate;

    @Test
    public void zAdd(){
        long timeStamp = System.currentTimeMillis();
        long poetry = 2L;
        long score = poetry * 10000000000000L + timeStamp;
        redisTemplate.opsForZSet().add("123", 2L,1);
    }

    @Test
    public void zUpdate(){
        Double score = redisTemplate.opsForZSet().score("123", 1L);
        long timeStamp = System.currentTimeMillis();
        long poetry = (long) (score / 10000000000000L + 1);
        long updateScore = (poetry * 10000000000000L + timeStamp);
        redisTemplate.opsForZSet().add("123", 1L,updateScore);
        System.out.println(updateScore);
    }


}
