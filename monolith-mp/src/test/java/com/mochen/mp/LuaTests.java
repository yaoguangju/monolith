package com.mochen.mp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class LuaTests {

    private static final String RELEASE_LOCK_LUA_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void testLua(){
        String lockKey = "12334";

        String UUID = cn.hutool.core.lang.UUID.fastUUID().toString();

        boolean success = redisTemplate.opsForValue().setIfAbsent(lockKey,UUID,3, TimeUnit.MINUTES);

        if (!success){

            System.out.println("锁已存在");

        }

        // 指定 lua 脚本，并且指定返回值类型

        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(RELEASE_LOCK_LUA_SCRIPT,Long.class);

        // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）

        Long result = (Long) redisTemplate.execute(redisScript, Collections.singletonList(lockKey),UUID);

        System.out.println(result);
    }

    @Test
    public void testLua1(){
        final String RELEASE_LOCK_LUA_SCRIPT = "local score = tonumber(redis.call('ZSCORE', KEYS[1], KEYS[2])); local poetry = math.floor(score / 10^13); local updateScore = (poetry + 1) * 10^13 + tonumber(ARGV[1]);redis.call('ZADD', KEYS[1],updateScore , KEYS[2]); return 0;";
        // 指定 lua 脚本，并且指定返回值类型
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(RELEASE_LOCK_LUA_SCRIPT,Long.class);
        List<String> keyList = new ArrayList<>();
        keyList.add("123");
        keyList.add("2L");
        // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）
        Long result = redisTemplate.execute(redisScript, keyList,"1649304543407");
        System.out.println(result);

    }

    @Test
    public void testLua2(){
        final String RELEASE_LOCK_LUA_SCRIPT = "local score = tonumber(redis.call('ZSCORE', KEYS[1], KEYS[2]));local poetry = math.floor(score / 10^13);local updateScore = (poetry + 1) * 10^13 + ARGV[1];redis.call('ZADD', KEYS[1],updateScore , KEYS[2]); return 0;";
        // 指定 lua 脚本，并且指定返回值类型
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(RELEASE_LOCK_LUA_SCRIPT,Long.class);
        List<String> keyList = new ArrayList<>();
        keyList.add("123");
        keyList.add("2L");
        // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）
        Long result = redisTemplate.execute(redisScript, keyList,System.currentTimeMillis());
        System.out.println(result);
    }


}
