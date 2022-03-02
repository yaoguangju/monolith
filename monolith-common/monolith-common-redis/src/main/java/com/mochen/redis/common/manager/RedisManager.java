package com.mochen.redis.common.manager;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * spring redis 工具类
 *
 * @author 姚广举
 **/
@SuppressWarnings(value = { "unchecked", "rawtypes" })
@Component
public class RedisManager
{
    @Resource
    public RedisTemplate redisTemplate;

    // ============================ common =============================

    /**
     * 设置有效时间
     *  @param key Redis键
     * @param timeout 超时时间
     */
    public void setExpire(final String key, final long timeout)
    {
        try {
            if (timeout > 0) {
                redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取有效时间
     * @param key Redis键
     * @return 有效时间
     */
    public long getExpire(final String key)
    {
        try {
            return redisTemplate.getExpire(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 判断 key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(final String key)
    {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 删除单个对象
     *
     * @param key Redis键
     */
    public boolean deleteObject(final String key)
    {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return 删除的数量
     */
    public boolean deleteObject(final Collection collection)
    {
        try {
            redisTemplate.delete(collection);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ============================ Integer、String、实体类 =============================

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *  @param key 缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value)
    {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 缓存基本的对象，Integer、String、实体类等,并设置时间
     * @param key 缓存的键值
     * @param value 缓存的值
     * @param timeout 时间
     */
    public <T> void setCacheObject(final String key, final T value, final Long timeout)
    {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
            if (timeout > 0)
                setExpire(key, timeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得缓存的基本对象。
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key)
    {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * Integer递增、递减
     * @param key 键
     * @param num 变化量
     * @return 返回对象
     */
    public boolean increment(String key, long num) {
        try {
            redisTemplate.opsForValue().increment(key, num);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // ============================ List =============================

    /**
     * 缓存List数据
     *
     * @param key 缓存的键值
     * @param dataList 待缓存的List数据
     */
    public <T> void setCacheList(final String key, final List<T> dataList)
    {
        try{
            redisTemplate.opsForList().rightPushAll(key, dataList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 将list放入缓存，并设置过期时间
     *
     * @param key   键
     * @param dataList 待缓存的List数据
     * @param timeout  时间(秒)
     */
    public <T> void setCacheList(final String key, final List<T> dataList, long timeout) {
        try {
            redisTemplate.opsForList().rightPushAll(key, dataList);
            if (timeout > 0)
                setExpire(key, timeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将值放入list缓存中
     *
     * @param key   键
     * @param value 值
     */
    public void setCacheObjectToList(final String key, final Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将值放入list缓存中，设置时间
     *
     * @param key   键
     * @param value 值
     * @param timeout  时间(秒)
     */
    public void setCacheObjectToList(final String key, final Object value, long timeout) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (timeout > 0)
                setExpire(key, timeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得缓存的list所有内容
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key)
    {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return 长度
     */
    public long getCacheListSize(final String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }



    // ============================ Set =============================

    /**
     * 缓存Set
     *
     * @param key  键
     * @param values 值 可以是多个
     */
    public void setCacheSet(final String key, final Object... values) {
        try {
            redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 缓存set，设置时间
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     */
    public void setCacheSet(final String key, final long time, final Object... values) {
        try {
            redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                setExpire(key, time);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得缓存的set
     *
     * @param key Redis键
     * @return Set
     */
    public <T> Set<T> getCacheSet(final String key)
    {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 判断set中是否有该key值
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean hasCacheSetValue(final String key, final Object value) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return 数量
     */
    public long getCacheSetSize(final String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除set中的value
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return boolean
     */
    public boolean deleteCacheSet(String key, Object... values) {
        try {
            redisTemplate.opsForSet().remove(key, values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    // ================================Map/Hash=======================================

    /**
     * 缓存Map
     * @param key Redis键
     * @param dataMap Map
     */
    public <T> void setCacheHashMap(final String key, final Map<String, T> dataMap)
    {
        try {
            if (dataMap != null) {
                redisTemplate.opsForHash().putAll(key, dataMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 缓存Map,并设置时间
     *
     * @param key Redis键
     * @param dataMap Map
     * @param timeout 时间
     */
    public <T> void setCacheHashMap(final String key, final Map<String, T> dataMap, final Long timeout)
    {
        try {
            if (dataMap != null) {
                redisTemplate.opsForHash().putAll(key, dataMap);
                if (timeout > 0) {
                    setExpire(key, timeout);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key Redis键
     * @return Map中的对象
     */
    public <T> Map<String, T> getCacheHashMap(final String key)
    {
        return redisTemplate.opsForHash().entries(key);
    }


    /**
     * 往Hash中存入数据
     *  @param key Redis键
     * @param hKey Hash键
     * @param value 值
     */
    public <T> void setCacheHashMapValue(final String key, final String hKey, final T value)
    {
        try {
            redisTemplate.opsForHash().put(key, hKey, value);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取Hash中的数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T getCacheHashMapValue(final String key, final String hKey)
    {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public boolean deleteHashMapValues(final String key, final Object... item) {
        try {
            redisTemplate.opsForHash().delete(key, item);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hasHashMapKey(final String key, final String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }


    /**
     * 获取多个Hash中的数据
     *
     * @param key Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public <T> List<T> getMultiCacheHashMapValue(final String key, final Collection<Object> hKeys)
    {
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

}
