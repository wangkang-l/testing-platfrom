package com.bgw.testing.server.controller;

import com.bgw.testing.common.AppConst;
import com.bgw.testing.common.dto.GroupInfoDto;
import com.bgw.testing.server.service.GroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@Api(description = "组管理")
@RequestMapping(value = AppConst.BASE_PATH)
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Deprecated
    @ApiOperation(value = "查询组")
    @RequestMapping(value = "/group/{group_id}", method = RequestMethod.GET)
    public List<GroupInfoDto> getChildGroupInfo(@PathVariable(value = "group_id") String groupId) {
        return groupService.getChildGroupInfo(groupId);
    }


    @ApiOperation(value = "查询组")
    @RequestMapping(value = "/groups/{group_id}", method = RequestMethod.GET)
    public GroupInfoDto getAllChildGroupInfo(@PathVariable(value = "group_id") String groupId) {
        return groupService.getAllChildGroupInfo(groupId);
    }

    @ApiOperation(value = "新增组")
    @RequestMapping(value = "/group", method = RequestMethod.POST)
    public boolean addGroupInfo(@RequestBody @Valid GroupInfoDto groupInfoDto) {
        groupService.addGroupInfo(groupInfoDto);
        return true;
    }

    @ApiOperation(value = "更新组")
    @RequestMapping(value = "/group", method = RequestMethod.PUT)
    public boolean updateGroupInfo(@RequestBody @Valid GroupInfoDto groupInfoDto) {
        groupService.updateGroupInfo(groupInfoDto);
        return true;
    }

    @ApiOperation(value = "删除组")
    @RequestMapping(value = "/group/{group_id}", method = RequestMethod.DELETE)
    public boolean deleteGroupInfo(@PathVariable(value = "group_id") String groupId) {
        groupService.deleteGroupInfo(groupId);
        return true;
    }
}
