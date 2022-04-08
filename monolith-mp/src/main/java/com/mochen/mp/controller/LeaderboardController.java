package com.mochen.mp.controller;

import cn.hutool.core.util.RandomUtil;
import com.mochen.core.common.xbo.Result;
import com.mochen.mp.entity.Leaderboard;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@RestController
@RequestMapping("/leaderboard")
public class LeaderboardController {

    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    @PostMapping("/addScore")
    public Result addScore() {
        final String SCRIPT = "local score = tonumber(redis.call('ZSCORE', KEYS[1], KEYS[2])); if(score == nil) then redis.call('ZADD', KEYS[1], 10^13 + ARGV[1], KEYS[2]); else local poetry = math.floor(score / 10^13);local updateScore = (poetry + 1) * 10^13 + ARGV[1]; redis.call('ZADD', KEYS[1],updateScore , KEYS[2]);end return 0";
        // 指定 lua 脚本，并且指定返回值类型
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(SCRIPT, Long.class);
        List<String> keyList = new ArrayList<>();
        keyList.add("leaderboard");
        Random r = new Random();
        keyList.add(String.valueOf(RandomUtil.randomLong(1L,1000L)));
        // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）
        redisTemplate.execute(redisScript, keyList, System.currentTimeMillis());
        return Result.success();
    }

    @GetMapping("/getRank")
    public Result getRank() {
        Set<ZSetOperations.TypedTuple<Integer>> typedTuples = redisTemplate.opsForZSet().reverseRangeWithScores("leaderboard", 0, -1);
        assert typedTuples != null;
        List<Leaderboard> leaderboardList = new ArrayList<>();
        typedTuples.forEach(key ->{
            leaderboardList.add(new Leaderboard(key.getValue(),"学生" + key.getValue(), (int)(key.getScore() / 10000000000000L)));
        });
        return Result.success(leaderboardList);
    }

}
