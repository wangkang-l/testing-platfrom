package com.bgw.testing.server.service;

import com.bgw.testing.common.dto.GroupInfoDto;
import com.bgw.testing.dao.mapper.bgw_automation.TsCaseInfoMapper;
import com.bgw.testing.dao.mapper.bgw_automation.TsGroupInfoMapper;
import com.bgw.testing.dao.pojo.bgw_automation.TsCaseInfo;
import com.bgw.testing.dao.pojo.bgw_automation.TsGroupInfo;
import com.bgw.testing.server.util.BaseStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GroupService {

    @Autowired
    private TsGroupInfoMapper tsGroupInfoMapper;
    @Autowired
    private TsCaseInfoMapper tsCaseInfoMapper;

    /**
     * 根据父Id获取子组信息
     * @param parentId
     * @return
     */
    public List<GroupInfoDto> getGroupInfoByParentId(String parentId) {
        List<TsGroupInfo> tsGroupInfos = tsGroupInfoMapper.selectByParentId(parentId);
        List<GroupInfoDto> groupInfoDtos = new ArrayList<>();
        if (tsGroupInfos != null && tsGroupInfos.size() > 0) {
            tsGroupInfos.forEach(tsGroupInfo -> {
                groupInfoDtos.add(groupInfoDbConvertDto(tsGroupInfo));
            });
        }
        return groupInfoDtos;
    }

    /**
     * 根据组名称模糊查询组信息
     * @param groupName
     * @return
     */
    public List<GroupInfoDto> getGroupInfoByGroupName(String groupName) {
        List<TsGroupInfo> tsGroupInfos = tsGroupInfoMapper.selectLikeByGroupName(groupName);
        List<GroupInfoDto> groupInfoDtos = new ArrayList<>();
        if (tsGroupInfos != null && tsGroupInfos.size() > 0) {
            tsGroupInfos.forEach(tsGroupInfo -> {
                groupInfoDtos.add(groupInfoDbConvertDto(tsGroupInfo));
            });
        }
        return groupInfoDtos;
    }

    /**
     * 新增组
     * @param groupInfoDto
     */
    public void addGroupInfo(GroupInfoDto groupInfoDto) {
        List<TsGroupInfo> tsGroupInfos = tsGroupInfoMapper.selectByGroupName(groupInfoDto.getGroupName(),groupInfoDto.getParentId());
        if(tsGroupInfos.size()==0){
            groupInfoDto.setGroupId(BaseStringUtils.uuidSimple());
            groupInfoDto.setCreateTime(new Date());
            groupInfoDto.setCreateBy("System");
            tsGroupInfoMapper.insertSelective(groupInfoDtoConvertDb(groupInfoDto));
        }
    }

    /**
     * 更新组，移动组
     * @param groupInfoDto
     */
    public void updateGroupInfo(GroupInfoDto groupInfoDto) {
        groupInfoDto.setUpdateTime(new Date());
        groupInfoDto.setUpdateBy("System");
        tsGroupInfoMapper.updateByPrimaryKeySelective(groupInfoDtoConvertDb(groupInfoDto));
    }

    /**
     * 删除组（有用例不能删）
     * @param groupId
     */
    public String deleteGroupInfo(String groupId) {
        String result = "";
        List<TsGroupInfo> tsGroupInfos = tsGroupInfoMapper.selectByParentId(groupId);
        Integer childGroupNum = tsGroupInfos.size();
        if (childGroupNum == 0){
            List<TsCaseInfo> tsCaseInfos = tsCaseInfoMapper.selectByGroupId(groupId);
            if(tsCaseInfos.size()==0){
                tsGroupInfoMapper.deleteByPrimaryKey(groupId);
            }
            else{
                result = "请删除组内用例再删除组";
            }
        }else if(childGroupNum > 0){
            int loop = 0;
            while((loop < childGroupNum)&&(StringUtils.isBlank(result))){
                result = deleteGroupInfo(tsGroupInfos.get(loop).getId());
                loop ++;
            }
            if(StringUtils.isBlank(result))
            {
                result = deleteGroupInfo(groupId);
            }
        }
        return result;
    }

    private GroupInfoDto groupInfoDbConvertDto(TsGroupInfo tsGroupInfo) {
        GroupInfoDto groupInfoDto = new GroupInfoDto();
        groupInfoDto.setGroupId(tsGroupInfo.getId());
        groupInfoDto.setGroupName(tsGroupInfo.getGroupName());
        groupInfoDto.setParentId(tsGroupInfo.getParentId());
        groupInfoDto.setPriority(tsGroupInfo.getPriority());
        groupInfoDto.setCreateTime(tsGroupInfo.getCreateTime());
        groupInfoDto.setCreateBy(tsGroupInfo.getCreateBy());
        groupInfoDto.setUpdateTime(tsGroupInfo.getUpdateTime());
        groupInfoDto.setUpdateBy(tsGroupInfo.getUpdateBy());
        return groupInfoDto;
    }

    private TsGroupInfo groupInfoDtoConvertDb(GroupInfoDto groupInfoDto){
        TsGroupInfo tsGroupInfo = new TsGroupInfo();
        tsGroupInfo.setId(groupInfoDto.getGroupId());
        tsGroupInfo.setGroupName(groupInfoDto.getGroupName());
        tsGroupInfo.setParentId(groupInfoDto.getParentId());
        tsGroupInfo.setPriority(groupInfoDto.getPriority());
        tsGroupInfo.setCreateTime(groupInfoDto.getCreateTime());
        tsGroupInfo.setCreateBy(groupInfoDto.getCreateBy());
        tsGroupInfo.setUpdateTime(groupInfoDto.getUpdateTime());
        tsGroupInfo.setUpdateBy(groupInfoDto.getUpdateBy());
        return tsGroupInfo;
    }
}
