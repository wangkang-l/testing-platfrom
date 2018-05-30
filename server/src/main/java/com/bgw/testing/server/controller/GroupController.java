package com.bgw.testing.server.controller;

import com.bgw.testing.common.AppConst;
import com.bgw.testing.common.dto.GroupInfoDto;
import com.bgw.testing.server.service.GroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@Api(description = "组管理")
@RequestMapping(value = AppConst.BASE_PATH + "group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @ApiOperation(value = "根据父ID获取子组信息")
    @RequestMapping(value = "/{parentId}", method = RequestMethod.GET)
    public List<GroupInfoDto> getGroupInfoByParentId(@PathVariable String parentId) {
        return groupService.getGroupInfoByParentId(parentId);
    }

    @ApiOperation(value = "根据组名称模糊查询组信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public List<GroupInfoDto> getGroupInfoByGroupName(@RequestParam String groupName) {
        return groupService.getGroupInfoByGroupName(groupName);
    }

    @ApiOperation(value = "新增组")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public boolean addGroupInfo(@RequestBody GroupInfoDto groupInfoDto) {
        groupService.addGroupInfo(groupInfoDto);
        return true;
    }

    @ApiOperation(value = "更新组，移动组")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public boolean updateGroupInfo(@RequestBody GroupInfoDto groupInfoDto) {
        groupService.updateGroupInfo(groupInfoDto);
        return true;
    }

    @ApiOperation(value = "删除组")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public boolean deleteGroupInfo(@RequestParam(value = "groupId") String groupId) {
        groupService.deleteGroupInfo(groupId);
        return true;
    }
}
