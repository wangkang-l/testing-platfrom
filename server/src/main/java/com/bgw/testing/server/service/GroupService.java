package com.bgw.testing.server.service;

import com.bgw.testing.common.dto.GroupInfoDto;
import com.bgw.testing.common.enums.ErrorCode;
import com.bgw.testing.dao.mapper.bgw_automation.TsCaseInfoMapper;
import com.bgw.testing.dao.mapper.bgw_automation.TsGroupInfoMapper;
import com.bgw.testing.dao.pojo.bgw_automation.TsCaseInfo;
import com.bgw.testing.dao.pojo.bgw_automation.TsGroupInfo;
import com.bgw.testing.server.GroupContext;
import com.bgw.testing.server.util.BaseStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bgw.testing.server.config.ServerException;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;


@Service
public class GroupService {

    @Autowired
    private TsGroupInfoMapper tsGroupInfoMapper;
    @Autowired
    private TsCaseInfoMapper tsCaseInfoMapper;

    @PostConstruct
    public void initGroup() {
        List<TsGroupInfo> tsGroupInfos = tsGroupInfoMapper.selectAll();
        List<GroupInfoDto> groupInfoDtos = new ArrayList<>();
        if (tsGroupInfos != null && tsGroupInfos.size() > 0) {
            tsGroupInfos.forEach(tsGroupInfo -> {
                groupInfoDtos.add(convertToGroupInfoDto(tsGroupInfo));
            });
            GroupContext.getInstance().initGroup(groupInfoDtos);
        }
    }

    /**
     * 从缓存查询子组
     * @param groupId
     * @return
     */
    public List<GroupInfoDto> getChildGroupInfo(String groupId){
        return GroupContext.getInstance().getCurrentChildGroupInfo(groupId);
    }

    /**
     * 是否存在组
     * @param groupId
     * @return
     */
    public boolean isExistGroup(String groupId) {
        TsGroupInfo tsGroupInfo = tsGroupInfoMapper.selectByPrimaryKey(groupId);
        if (tsGroupInfo != null) {
            return true;
        }
        return false;
    }

    /**
     * 是否终节点
     * @param groupId
     * @return
     */
    private boolean isFinallyGroup(String groupId) {
        if (tsGroupInfoMapper.selectByParentId(groupId).size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 新增组
     * @param groupInfoDto
     */
    public void addGroupInfo(GroupInfoDto groupInfoDto) {
        if(StringUtils.isBlank(groupInfoDto.getParentId())) {
            throw new ServerException(ErrorCode.NOT_NULL, "parentId");
        } else if (isExistGroup(groupInfoDto.getParentId())){
            if (tsGroupInfoMapper.selectByGroupName(groupInfoDto.getGroupName(), groupInfoDto.getParentId()) == null) {
                TsGroupInfo tsGroupInfo = convertToTsGroupInfo(groupInfoDto);
                tsGroupInfo.setId(BaseStringUtils.uuidSimple());
                tsGroupInfo.setCreateTime(new Date());
                tsGroupInfo.setCreateBy("System");
                tsGroupInfo.setUpdateTime(new Date());
                tsGroupInfo.setUpdateBy("System");
                tsGroupInfoMapper.insertSelective(tsGroupInfo);
                GroupContext.getInstance().addGroupInfo(convertToGroupInfoDto(tsGroupInfo));
            } else {
                throw new ServerException(ErrorCode.ALREADY_EXISTS, groupInfoDto.getGroupName());
            }
        } else{
            throw new ServerException(ErrorCode.NOT_EXIST,groupInfoDto.getParentId());
        }
    }

    /**
     * 更新组，移动组
     * @param groupInfoDto
     */
    public void updateGroupInfo(GroupInfoDto groupInfoDto) {
        TsGroupInfo oldTsGroupInfo = tsGroupInfoMapper.selectByPrimaryKey(groupInfoDto.getGroupId());

        TsGroupInfo tsGroupInfo = convertToTsGroupInfo(groupInfoDto);
        tsGroupInfo.setUpdateTime(new Date());
        tsGroupInfo.setUpdateBy("System");
        tsGroupInfoMapper.updateByPrimaryKeySelective(tsGroupInfo);

        GroupInfoDto oldGroupInfoDto = convertToGroupInfoDto(tsGroupInfoMapper.selectByPrimaryKey(oldTsGroupInfo.getParentId()));
        GroupContext.getInstance().updateGroupInfo(convertToGroupInfoDto(tsGroupInfo),oldGroupInfoDto);
    }
    /**
     * 删除组（有用例不能删）
     * @param groupId
     */
    public String deleteGroupInfo(String groupId) {
        TsGroupInfo oldTsGroupInfo = tsGroupInfoMapper.selectByPrimaryKey(groupId);

        String result = deleteGroup(groupId);

        GroupInfoDto oldGroupInfoDto = convertToGroupInfoDto(tsGroupInfoMapper.selectByPrimaryKey(oldTsGroupInfo.getParentId()));
        GroupContext.getInstance().deleteGroupInfo(groupId,oldGroupInfoDto);
        return result;
    }

    /**
     * 删除组（有用例不能删）
     * @param groupId
     */
    @Transactional
    public String deleteGroup(String groupId) {
        String result = "";
        List<TsGroupInfo> tsGroupInfos = tsGroupInfoMapper.selectByParentId(groupId);
        Integer childGroupNum = tsGroupInfos.size();
        if (childGroupNum == 0){
            Map params = new HashMap();
            params.put("groupId", groupId);
            List<TsCaseInfo> tsCaseInfos = tsCaseInfoMapper.selectByParams(params);
            if(tsCaseInfos.size()==0){
                tsGroupInfoMapper.deleteByPrimaryKey(groupId);
            }
            else{
                throw new ServerException(ErrorCode.ALREADY_EXISTS,"组里的用例");
            }
        }else if(childGroupNum > 0){
            int loop = 0;
            while((loop < childGroupNum)&&(StringUtils.isBlank(result))){
                result = deleteGroup(tsGroupInfos.get(loop).getId());
                loop ++;
            }
            result = deleteGroup(groupId);
        }
        return result;
    }

    private GroupInfoDto convertToGroupInfoDto(TsGroupInfo tsGroupInfo) {
        GroupInfoDto groupInfoDto = new GroupInfoDto();
        groupInfoDto.setGroupId(tsGroupInfo.getId());
        groupInfoDto.setGroupName(tsGroupInfo.getGroupName().trim());
        groupInfoDto.setParentId(tsGroupInfo.getParentId());
        groupInfoDto.setPriority(tsGroupInfo.getPriority());
        groupInfoDto.setIsFinally(isFinallyGroup(tsGroupInfo.getId()));
        return groupInfoDto;
    }

    private TsGroupInfo convertToTsGroupInfo(GroupInfoDto groupInfoDto){
        TsGroupInfo tsGroupInfo = new TsGroupInfo();
        tsGroupInfo.setId(groupInfoDto.getGroupId());
        tsGroupInfo.setGroupName(groupInfoDto.getGroupName());
        tsGroupInfo.setParentId(groupInfoDto.getParentId());
        tsGroupInfo.setPriority(groupInfoDto.getPriority());
        return tsGroupInfo;
    }
}
