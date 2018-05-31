package com.bgw.testing.server.controller;

import com.bgw.testing.common.AppConst;
import com.bgw.testing.common.dto.PageInfo;
import com.bgw.testing.common.dto.VariableInfoDto;
import com.bgw.testing.server.service.VariableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@Api(description = "变量管理")
@RequestMapping(value = AppConst.BASE_PATH + "variable")
public class VariableController {

    @Autowired
    private VariableService variableService;

    @ApiOperation(value = "查询变量列表")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public PageInfo<VariableInfoDto> getVariableList(
            @RequestParam String type,
            @RequestParam(value = "environment_id", required = false) String environmentId,
            @RequestParam(required = false) String var4,
            @RequestParam(value = "page_num") Integer pageNum,
            @RequestParam(value = "page_size") Integer pageSize) {
        return variableService.getVariableList(type, environmentId, pageNum, pageSize, var4);
    }

    @ApiOperation(value = "新增变量")
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public Boolean addVariable(@RequestBody VariableInfoDto variableInfoDto) {
        variableService.addVariable(variableInfoDto);
        return true;
    }

    @ApiOperation(value = "更新变量")
    @RequestMapping(value = "/info", method = RequestMethod.PUT)
    public Boolean updateVariable(@RequestBody VariableInfoDto variableInfoDto) {
        variableService.updateVariable(variableInfoDto);
        return true;
    }

    @ApiOperation(value = "删除变量")
    @RequestMapping(value = "/info/{variable_id}", method = RequestMethod.DELETE)
    public Boolean delVariable(@PathVariable(value = "variable_id") String variableId ){
        variableService.delVariable(variableId);
        return true;
    }

}
