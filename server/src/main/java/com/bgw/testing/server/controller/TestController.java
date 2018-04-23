package com.bgw.testing.server.controller;

import com.bgw.testing.common.AppConst;
import com.bgw.testing.dao.integration.pojo.TCase;
import com.bgw.testing.dao.usercenter.pojo.UserBasic888888;
import com.bgw.testing.server.service.IntegrationService;
import com.bgw.testing.server.service.JdbcTemplateService;
import com.bgw.testing.server.service.RedisTestService;
import com.bgw.testing.server.service.UserCenterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@Api(description = AppConst.SERVICE_NAME_CN)
@RequestMapping(value = AppConst.BASE_PATH)
public class TestController {

    @Autowired
    private IntegrationService integrationService;
    @Autowired
    private UserCenterService userCenterService;
    @Autowired
    private RedisTestService redisTestService;
    @Autowired
    private JdbcTemplateService jdbcTemplateService;

    @ApiOperation(value = "获取测试用例")
    @RequestMapping(value = "/testCase", method = RequestMethod.GET)
    public TCase getTestCase(@RequestParam String id) {
        return integrationService.getTCase(id);
    }

    @ApiOperation(value = "获取用户基本信息")
    @RequestMapping(value = "/userBasicInfo", method = RequestMethod.GET)
    public UserBasic888888 getUserBasicInfo(@RequestParam String id) {
        return userCenterService.getUserBasicInfo(id);
    }

    @ApiOperation(value = "删除Redis指定key")
    @RequestMapping(value = "/redisInfo", method = RequestMethod.DELETE)
    public Boolean deleteRedisInfo(@RequestParam String key) {
        return redisTestService.deleteRedisInfo(key);
    }

    @ApiOperation(value = "获取测试用例JdbcTemplate")
    @RequestMapping(value = "/testCase2", method = RequestMethod.GET)
    public TCase getTestCase2(@RequestParam String sql, @RequestParam Object[] params) {
        return jdbcTemplateService.getTestCase(sql, params);
    }

}
