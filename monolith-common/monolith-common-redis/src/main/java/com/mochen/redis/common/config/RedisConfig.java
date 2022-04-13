package com.mochen.redis.common.config;

import com.mochen.redis.common.utils.FastJsonRedisSerializer;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis配置
 *
 * @author ruoyi
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport
{
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)
    {
        // 创建redisTemplate对象
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        // 将redis的连接工厂与模板对象进行绑定
        redisTemplate.setConnectionFactory(redisConnectionFactory);


        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Object.class);

        // 设置key值序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 设置value序列化
        redisTemplate.setValueSerializer(serializer);

        // Hash的key和value的序列化
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

//    @Bean
//    public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory factory){
//        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(factory);
//        // 使用Jackson2JsonRedisSerialize 替换默认序列化
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper objectMapper = new ObjectMapper();
//        //忽略任何值为null的属性
//        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
//        // 设置key和value的序列化规则
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
//        // 设置hashKey和hashValue的序列化规则
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
//        //afterPropertiesSet和init-method之间的执行顺序是afterPropertiesSet 先执行，init-method 后执行。
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//
//    }
}