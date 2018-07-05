package com.bgw.testing.server.controller;

import com.bgw.testing.common.AppConst;
import com.bgw.testing.common.dto.InterfaceInfoDto;
import com.bgw.testing.common.dto.PageInfo;
import com.bgw.testing.server.service.InterfaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@CrossOrigin
@RestController
@Api(description = "接口管理")
@RequestMapping(value = AppConst.BASE_PATH)
public class InterfaceController {

    @Autowired
    private InterfaceService interfaceService;

    @ApiOperation(value = "获取当前组的接口")
    @RequestMapping(value = "/interface/{group_id}", method = RequestMethod.GET)
    public PageInfo<InterfaceInfoDto> getInterfaceInfo(@PathVariable(value = "group_id") String groupId,
                                                       @RequestParam(value = "page_num") Integer pageNum,
                                                       @RequestParam(value = "page_size") Integer pageSize,
                                                       @RequestParam String var4) {
        return interfaceService.getInterfaceInfo(groupId, pageNum, pageSize, var4);
    }

    @ApiOperation(value = "新增接口")
    @RequestMapping(value = "/interface", method = RequestMethod.POST)
    public boolean addInterfaceInfo(@RequestBody @Valid InterfaceInfoDto interfaceInfoDto) {
        interfaceService.addInterfaceInfo(interfaceInfoDto);
        return true;
    }

    @ApiOperation(value = "更新接口")
    @RequestMapping(value = "/interface/{interface_id}", method = RequestMethod.PUT)
    public boolean updateInterfaceInfo(@PathVariable(value = "interface_id") String interfaceId,
                                       @RequestBody @Valid InterfaceInfoDto interfaceInfoDto) {
        interfaceService.updateInterfaceInfo(interfaceId, interfaceInfoDto);
        return true;
    }

    @ApiOperation(value = "删除接口")
    @RequestMapping(value = "/interface/{interface_id}", method = RequestMethod.DELETE)
    public boolean deleteInterfaceInfo(@PathVariable(value = "interface_id") String interfaceId) {
        interfaceService.deleteInterfaceInfo(interfaceId);
        return true;
    }

    @ApiOperation(value = "移动测试用例")
    @RequestMapping(value = "/interface", method = RequestMethod.PATCH)
    public Boolean moveInterface(
            @RequestParam(value = "interface_ids") List<String> interfaceIds,
            @RequestParam(value = "new_group_id") String newGroupId) {
        interfaceService.moveInterface(interfaceIds, newGroupId);
        return true;
    }
}
