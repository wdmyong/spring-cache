package com.wdm.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    public void setCache(final String key, final String value, final int expireInSecond) {
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                if (expireInSecond <= 0) {
                    connection.set(redisTemplate.getStringSerializer().serialize(key),
                            redisTemplate.getStringSerializer().serialize(value));
                } else {
                    connection.set(redisTemplate.getStringSerializer().serialize(key),
                            redisTemplate.getStringSerializer().serialize(value),
                            Expiration.seconds(expireInSecond), null);
                }
                return null;
            }
        });
    }

    public String getCache(final String key) {
        return redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] byteKey = redisTemplate.getStringSerializer().serialize(key);
                if (connection.exists(byteKey)) {
                    byte[] byteValue = connection.get(byteKey);
                    String value = redisTemplate.getStringSerializer().deserialize(byteValue);
                    return value;
                }
                return null;
            }
        });
    }

    public void set(String key, Object value) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    public void set(String key, Object value, Long timeout) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, timeout, TimeUnit.SECONDS);
    }

    public Object get(String key) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public Long incr(String key) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        return valueOperations.increment(key, 1);
    }

    public Long decr(String key) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        return valueOperations.increment(key, -1);
    }

    public void del(String key) {
        redisTemplate.delete(key);
    }

    public void expire(String key, Long timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    public void hashPushAll(String key, Map<String, Object> map) {
        HashOperations<String, String, Object> hash = redisTemplate.opsForHash();
        hash.putAll(key, map);
    }
    
    public void hashPush(String key, String hashKey, Object value) {
        HashOperations<String, String, Object> hash = redisTemplate.opsForHash();
        hash.put(key, hashKey, value);
    }

    public Map<String, Object> getMap(String key) {
        HashOperations<String, String, Object> hash = redisTemplate.opsForHash();
        return hash.entries(key);
    }

    public void listLeftPush(String key, Object value) {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        listOperations.leftPush(key, value);
    }

    public void listRightPush(String key, Object value) {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        listOperations.rightPush(key, value);
    }

    public Object listRightPop(String key) {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        return listOperations.rightPop(key);
    }

    public Object listLeftPop(String key) {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        return listOperations.leftPop(key);
    }

    public List<Object> listRange(String key, Long start, Long end) {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        return listOperations.range(key, start, end);
    }

    public Long sizeOfList(String key) {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        return listOperations.size(key);
    }

    public Long sizeOfSet(String key) {
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        return setOperations.size(key);
    }

    public Long setAdd(String key, Object... values) {
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        return setOperations.add(key, values);
    }

    public Set<Object> setMembers(String key) {
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        return setOperations.members(key);
    }

}
