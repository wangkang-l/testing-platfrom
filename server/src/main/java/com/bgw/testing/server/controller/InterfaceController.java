package com.bgw.testing.server.controller;

import com.bgw.testing.common.AppConst;
import com.bgw.testing.common.dto.InterfaceInfoDto;
import com.bgw.testing.common.dto.InterfaceParamDto;
import com.bgw.testing.server.service.InterfaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@Api(description = "接口管理")
@RequestMapping(value = AppConst.BASE_PATH + "interface")
public class InterfaceController {
    @Autowired
    private InterfaceService interfaceService;

    @ApiOperation(value = "根据接口描述模糊查询接口")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public List<InterfaceInfoDto> getInterfaceInfo(@RequestParam String description) {
        return interfaceService.getInterfaceInfoByDescription(description);
    }

    @ApiOperation(value = "新增接口")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public boolean addInterfaceInfo(@RequestBody InterfaceInfoDto interfaceInfoDto) {
        interfaceService.addInterfaceInfo(interfaceInfoDto);
        return true;
    }

    @ApiOperation(value = "更新接口")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public boolean updateInterfaceInfo(@RequestBody InterfaceInfoDto interfaceInfoDto) {
        interfaceService.updateInterfaceInfo(interfaceInfoDto);
        return true;
    }

    @ApiOperation(value = "删除接口")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public boolean deleteInterfaceInfo(@RequestParam(value = "interfaceId") String interfaceId) {
        interfaceService.deleteInterfaceInfo(interfaceId);
        return true;
    }
//    @ApiOperation(value = "新增接口参数")
//    @RequestMapping(value = "/add1", method = RequestMethod.POST)
//    public boolean addInterfaceInfo(@RequestBody InterfaceParamDto interfaceParamDto) {
//        interfaceService.addInterfaceParam(interfaceParamDto);
//        return true;
//    }
}
