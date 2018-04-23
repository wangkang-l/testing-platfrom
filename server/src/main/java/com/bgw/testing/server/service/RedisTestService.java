package com.bgw.testing.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisTestService {

    @Autowired
    @Qualifier(value = "redisTemplate4")
    private RedisTemplate<String, Object> redisTemplate4;

    public Boolean deleteRedisInfo(String key) {
        return redisTemplate4.delete(key);
    }

}
