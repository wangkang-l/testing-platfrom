package com.bgw.testing.server.controller;

import com.bgw.testing.common.AppConst;
import com.bgw.testing.common.dto.EnvironmentDto;
import com.bgw.testing.server.service.EnvironmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@Api(description = "环境管理")
@RequestMapping(value = AppConst.BASE_PATH)
public class EnvironmentController {

    @Autowired
    private EnvironmentService environmentService;

    @ApiOperation(value = "查询环境列表")
    @RequestMapping(value = "/environment", method = RequestMethod.GET)
    public List<EnvironmentDto> getEnvironmentList() {
        return environmentService.getEnvironmentList();
    }

    @ApiOperation(value = "新增环境列表")
    @RequestMapping(value = "/environment", method = RequestMethod.POST)
    public Boolean addEnvironmentList(@RequestBody EnvironmentDto environmentDto) {
        environmentService.addEnvironmentList(environmentDto);
        return true;
    }

    @ApiOperation(value = "更新环境列表")
    @RequestMapping(value = "/environment", method = RequestMethod.PUT)
    public Boolean updateEnvironmentList(@RequestBody EnvironmentDto environmentDto) {
        environmentService.updateEnvironmentList(environmentDto);
        return true;
    }

    @ApiOperation(value = "删除环境列表")
    @RequestMapping(value = "/environment/{environment_id}", method = RequestMethod.DELETE)
    public Boolean delEnvironmentList(@PathVariable(value = "environment_id") String environmentId) {
        environmentService.delEnvironmentList(environmentId);
        return true;
    }

}
