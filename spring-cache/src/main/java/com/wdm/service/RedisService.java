package com.wdm.service;

import java.io.Serializable;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Resource
    RedisTemplate<Serializable, Serializable> redisTemplate;

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
}
