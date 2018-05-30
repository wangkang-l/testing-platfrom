package com.bgw.testing.server.service;

import com.bgw.testing.server.util.BaseJsonUtils;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class RedisService {

    @Value("${spring.redis.host}")
    private String hostName;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String passWord;
    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdl;
    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdl;
    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxTotal;
    @Value("${spring.redis.jedis.pool.max-wait}")
    private int maxWait;
    @Value("${spring.redis.timeout}")
    private int timeout;

    private JedisPool jedisPool(int dbIndex) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdl);
        config.setMinIdle(minIdl);
        config.setMaxWaitMillis(maxWait);
        return StringUtils.isBlank(passWord) ? new JedisPool(config, hostName, port, timeout, (String)null, dbIndex) : new JedisPool(config, hostName, port, timeout, passWord, dbIndex);
    }

    public Boolean exists(Integer dbIndex, String key) {
        Preconditions.checkArgument(StringUtils.isNotBlank(key), "key不能为空");
        Jedis jedis = null;

        Boolean var4;
        try {
            jedis = this.jedisPool(dbIndex).getResource();
            var4 = jedis.exists(key);
        } finally {
            IOUtils.closeQuietly(jedis);
        }

        return var4;
    }

    public String get(int dbIndex, String key) {
        Preconditions.checkArgument(StringUtils.isNotBlank(key), "key不能为空");
        Jedis jedis = null;

        String var4;
        try {
            jedis = this.jedisPool(dbIndex).getResource();
            var4 = jedis.get(key);
        } finally {
            IOUtils.closeQuietly(jedis);
        }

        return var4;
    }

    public Long set(int dbIndex, String key, Object value) {
        Preconditions.checkArgument(StringUtils.isNotBlank(key), "key不能为空");
        Preconditions.checkArgument(value != null, "value不能为null");
        Jedis jedis = null;

        Long var5;
        try {
            jedis = this.jedisPool(dbIndex).getResource();
            if (!"ok".equalsIgnoreCase(jedis.set(key, BaseJsonUtils.writeValue(value)))) {
                var5 = 0L;
                return var5;
            }

            var5 = 1L;
        } finally {
            IOUtils.closeQuietly(jedis);
        }

        return var5;
    }

    public Long del(Integer dbIndex, String key) {
        Preconditions.checkArgument(StringUtils.isNotBlank(key), "key不能为空");
        Jedis jedis = null;

        Long var4;
        try {
            jedis = this.jedisPool(dbIndex).getResource();
            var4 = jedis.del(key);
        } finally {
            IOUtils.closeQuietly(jedis);
        }

        return var4;
    }

    public void delPattern(Integer dbIndex, String pattern) {
        Preconditions.checkArgument(StringUtils.isNotBlank(pattern), "pattern不能为空");
        Jedis jedis = null;

        try {
            jedis = this.jedisPool(dbIndex).getResource();
            Set<String> keys = jedis.keys(pattern);
            if (keys.size() > 0) {
                for(String key : keys) {
                    jedis.del(key);
                }
            }
        } finally {
            IOUtils.closeQuietly(jedis);
        }
    }

    public void hSet(Integer dbIndex, String key, String field, Object value) {
        Preconditions.checkArgument(StringUtils.isNotBlank(key), "key不能为空");
        Preconditions.checkArgument(StringUtils.isNotBlank(field), "field不能为空");
        Preconditions.checkArgument(value != null, "value不能为null");
        Jedis jedis = null;

        try {
            jedis = this.jedisPool(dbIndex).getResource();
            jedis.hset(key, field, BaseJsonUtils.writeValue(value));
        } finally {
            IOUtils.closeQuietly(jedis);
        }

    }

    public String hGet(Integer dbIndex, String key, String field) {
        Preconditions.checkArgument(StringUtils.isNotBlank(key), "key不能为空");
        Preconditions.checkArgument(StringUtils.isNotBlank(field), "field不能为空");
        Jedis jedis = null;

        String var5;
        try {
            jedis = this.jedisPool(dbIndex).getResource();
            var5 = jedis.hget(key, field);
        } finally {
            IOUtils.closeQuietly(jedis);
        }

        return var5;
    }

    public Map<String, String> hGetAll(Integer dbIndex, String key) {
        Preconditions.checkArgument(StringUtils.isNotBlank(key), "key不能为空");
        Jedis jedis = null;

        Map<String, String> var4;
        try {
            jedis = this.jedisPool(dbIndex).getResource();
            var4 = jedis.hgetAll(key);
        } finally {
            IOUtils.closeQuietly(jedis);
        }

        return var4;
    }

    public void hDel(Integer dbIndex, String key, String... fields) {
        Preconditions.checkArgument(StringUtils.isNotBlank(key), "key不能为空");
        Preconditions.checkArgument(fields != null && fields.length > 0, "fields不能为空");
        Jedis jedis = null;

        try {
            jedis = this.jedisPool(dbIndex).getResource();
            jedis.hdel(key, fields);
        } finally {
            IOUtils.closeQuietly(jedis);
        }
    }

    public void lPush(Integer dbIndex, String key, Object value) {
        Preconditions.checkArgument(StringUtils.isNotBlank(key), "key不能为空");
        Preconditions.checkArgument(value != null, "value不能为null");
        Jedis jedis = null;

        try {
            jedis = this.jedisPool(dbIndex).getResource();
            jedis.lpush(key, new String[]{BaseJsonUtils.writeValue(value)});
        } finally {
            IOUtils.closeQuietly(jedis);
        }
    }

    public List<String> lRange(Integer dbIndex, String key, Long min, Long max) {
        Preconditions.checkArgument(StringUtils.isNotBlank(key), "key不能为空");
        Preconditions.checkArgument(min <= max, "min不能大于max");
        Jedis jedis = null;

        List<String> var6;
        try {
            jedis = this.jedisPool(dbIndex).getResource();
            var6 = jedis.lrange(key, min, max);
        } finally {
            IOUtils.closeQuietly(jedis);
        }

        return var6;
    }

    public String lIndex(Integer dbIndex, String key, Integer index) {
        Preconditions.checkArgument(StringUtils.isNotBlank(key), "key不能为空");
        Jedis jedis = null;

        String var6;
        try {
            jedis = this.jedisPool(dbIndex).getResource();
            var6 = jedis.lindex(key, index);
        } finally {
            IOUtils.closeQuietly(jedis);
        }

        return var6;
    }

    public String lTrim(Integer dbIndex, String key, Long start, Long stop) {
        Preconditions.checkArgument(StringUtils.isNotBlank(key), "key不能为空");
        Jedis jedis = null;

        String var6;
        try {
            jedis = this.jedisPool(dbIndex).getResource();
            var6 = jedis.ltrim(key, start, stop);
        } finally {
            IOUtils.closeQuietly(jedis);
        }

        return var6;
    }


}
