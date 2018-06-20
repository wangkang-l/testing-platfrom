package com.bgw.testing.server.service;

import com.bgw.testing.common.dto.RedisInfoDto;
import com.bgw.testing.common.dto.StepResultDto;
import com.bgw.testing.common.enums.ErrorCode;
import com.bgw.testing.server.config.ServerException;
import com.bgw.testing.server.util.BaseJsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisStepService {

    @Autowired
    private RedisService redisService;

    public StepResultDto executeRedisRequest(RedisInfoDto redisInfo) {

        StepResultDto stepResultDto = new StepResultDto();

        String result = "";

        try {
            switch (redisInfo.getAction()) {
                case get:
                    result = redisService.get(redisInfo.getDbIndex(), redisInfo.getKey());
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
                    result = redisService.hGet(redisInfo.getDbIndex(), redisInfo.getKey(), redisInfo.getField());
                    break;
                case hGetAll:
                    result = BaseJsonUtils.writeValue(redisService.hGetAll(redisInfo.getDbIndex(), redisInfo.getKey()));
                    break;
                case hSet:
                    redisService.hSet(redisInfo.getDbIndex(), redisInfo.getKey(), redisInfo.getField(), redisInfo.getValue());
                    break;
                case hDel:
                    redisService.hDel(redisInfo.getDbIndex(), redisInfo.getKey(), redisInfo.getField());
                    break;
                case lRange:
                    result = BaseJsonUtils.writeValue(redisService.lRange(redisInfo.getDbIndex(), redisInfo.getKey(), redisInfo.getStart(), redisInfo.getStop()));
                    break;
                case lPush:
                    redisService.lPush(redisInfo.getDbIndex(), redisInfo.getKey(), redisInfo.getValue());
                    break;
                case lIndex:
                    result = redisService.lIndex(redisInfo.getDbIndex(), redisInfo.getKey(), redisInfo.getIndex());
                    break;
                case lTrim:
                    result = redisService.lTrim(redisInfo.getDbIndex(), redisInfo.getKey(), redisInfo.getStart(), redisInfo.getStop());
                    break;
                default:
                    throw new ServerException(ErrorCode.UNKNOWN_TYPE, "redis Action");
            }
        } catch (Throwable e) {
            return new StepResultDto(false, "redis操作异常:" + redisInfo, e);
        }

        stepResultDto.setData(result);

        return stepResultDto;
    }
}
