package com.bgw.testing.server.service;

import com.bgw.testing.common.dto.RedisInfoDto;
import com.bgw.testing.common.enums.ErrorCode;
import com.bgw.testing.server.config.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisStepService {

    @Autowired
    private RedisService redisService;

    public void executeRedisRequest(RedisInfoDto redisInfo) {
        //TODO 占位符替换

        switch (redisInfo.getAction()) {
            case get:
                redisService.get(redisInfo.getDbIndex(), redisInfo.getKey());
                break;
            case set:
                if (redisInfo.getExpire() != 0) {
                    redisService.setEx(redisInfo.getDbIndex(), redisInfo.getKey(), redisInfo.getValue(), redisInfo.getExpire());
                } else {
                    redisService.set(redisInfo.getDbIndex(), redisInfo.getKey(), redisInfo.getValue());
                }
                break;
            case del:
                redisService.del(redisInfo.getDbIndex(), redisInfo.getKey());
                break;
            case hGet:
                redisService.hGet(redisInfo.getDbIndex(), redisInfo.getKey(), redisInfo.getField());
                break;
            case hGetAll:
                redisService.hGetAll(redisInfo.getDbIndex(), redisInfo.getKey());
                break;
            case hSet:
                redisService.hSet(redisInfo.getDbIndex(), redisInfo.getKey(), redisInfo.getField(), redisInfo.getValue());
                break;
            case hDel:
                redisService.hDel(redisInfo.getDbIndex(), redisInfo.getKey(), redisInfo.getField());
                break;
            case lRange:
                redisService.lRange(redisInfo.getDbIndex(), redisInfo.getKey(), redisInfo.getStart(), redisInfo.getStop());
                break;
            case lPush:
                redisService.lPush(redisInfo.getDbIndex(), redisInfo.getKey(), redisInfo.getValue());
                break;
            case lIndex:
                redisService.lIndex(redisInfo.getDbIndex(), redisInfo.getKey(), redisInfo.getIndex());
                break;
            case lTrim:
                redisService.lTrim(redisInfo.getDbIndex(), redisInfo.getKey(), redisInfo.getStart(), redisInfo.getStop());
                break;
            default:
                throw new ServerException(ErrorCode.UNKNOWN_TYPE, "redis Action");
        }

    }

}
