package com.mochen.mp.controller;

import com.mochen.core.common.xbo.Result;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/leaderboard")
public class LeaderboardController {

    @Resource
    private RedisTemplate<String, Long> redisTemplate;

    @PostMapping("/update")
    public Result update(){
        Double score = redisTemplate.opsForZSet().score("123", 1L);
        long timeStamp = System.currentTimeMillis();
        long poetry = (long) (score / 10000000000000L + 1);
        long updateScore = (poetry * 10000000000000L + timeStamp);
        redisTemplate.opsForZSet().add("123", 1L,updateScore);
        System.out.println(updateScore);
        return Result.success();
    }

    @PostMapping("/update1")
    public Result update1(){
        redisTemplate.opsForZSet().incrementScore("123", 2L,1);
        return Result.success();
    }

    @PostMapping("/testLua")
    public Result testLua(){
        final String RELEASE_LOCK_LUA_SCRIPT = "local score = tonumber(redis.call('ZSCORE', KEYS[1], KEYS[2]));local poetry = math.floor(score / 10^13);local updateScore = (poetry + 1) * 10^13 + ARGV[1];redis.call('ZADD', KEYS[1],updateScore , KEYS[2]); return 0;";
        // 指定 lua 脚本，并且指定返回值类型
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(RELEASE_LOCK_LUA_SCRIPT,Long.class);
        List<String> keyList = new ArrayList<>();
        keyList.add("123");
        keyList.add("2L");
        // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）
        Long result = redisTemplate.execute(redisScript, keyList,System.currentTimeMillis());
        return Result.success();
    }
}
