package com.bgw.testing.server.service;

import com.bgw.testing.common.dto.GroupInfoDto;
import com.bgw.testing.common.enums.ErrorCode;
import com.bgw.testing.dao.mapper.bgw_automation.TsGroupInfoMapper;
import com.bgw.testing.dao.pojo.bgw_automation.TsGroupInfo;
import com.bgw.testing.server.GroupContext;
import com.bgw.testing.server.util.BaseStringUtils;
import com.bgw.testing.server.util.BeanCopyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bgw.testing.server.config.ServerException;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class GroupService {

    @Autowired
    private TsGroupInfoMapper tsGroupInfoMapper;
    @Autowired
    private CaseService caseService;

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
        return GroupContext.getInstance().getGroupInfoList()
                .parallelStream()
                .anyMatch(groupInfoDto -> groupInfoDto.getGroupId().equals(groupId));
    }

    /**
     * 是否终节点
     * @param groupId
     * @return
     */
    private boolean isFinallyGroup(String groupId) {
        return GroupContext.getInstance().getGroupInfoList()
                .parallelStream()
                .anyMatch(groupInfoDto -> groupInfoDto.getParentId() != null
                        && groupInfoDto.getParentId().equals(groupId));
    }

    public GroupInfoDto getAllChildGroupInfo(String groupId) {
        GroupInfoDto groupInfoDto = GroupContext.getInstance().getGroupInfo(groupId).copy();
        if (groupInfoDto != null && !groupInfoDto.getIsFinally()) {
            List<GroupInfoDto> childGroup =
                    BeanCopyUtil.convertList(GroupContext.getInstance().getGroupInfoList(), GroupInfoDto.class)
                    .stream()
                    .filter(tempGroupInfo -> StringUtils.isNotBlank(tempGroupInfo.getParentId())
                            && tempGroupInfo.getParentId().equals(groupId))
                    .collect(Collectors.toList());
            childGroup.forEach(tempGroupInfo -> {
                if (!tempGroupInfo.getIsFinally()) {
                    tempGroupInfo.setGroupInfoDtos(getAllChildGroupInfo(tempGroupInfo.getGroupId()).getGroupInfoDtos());
                }
            });
            groupInfoDto.setGroupInfoDtos(childGroup);
        }
        return groupInfoDto;
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
    public void deleteGroupInfo(String groupId) {
        //校验groupId是否存在
        GroupInfoDto groupInfoDto = GroupContext.getInstance().getGroupInfo(groupId);
        if (groupInfoDto == null) {
            throw new ServerException(ErrorCode.NOT_EXIST, "groupId:" + groupId);
        }

        //校验组内是否存在用例
        boolean isExistCase = GroupContext.getInstance().getAllChildGroupId(groupId)
                .parallelStream()
                .anyMatch(tempGroupId -> caseService.getCaseInfoByGroupId(tempGroupId).size()>0);
        if (isExistCase) {
            throw new ServerException("4000", "组内用例不为空，不能删除");
        }

        //删除组
        List<String> groupIds = GroupContext.getInstance().getAllChildGroupId(groupId);
        GroupContext.getInstance().getGroupInfoList().removeIf(temp -> groupIds.contains(temp.getGroupId()));
        tsGroupInfoMapper.deleteByGroupIds(groupIds);

        //更新父节点缓存
        updateParentNode(groupInfoDto.getParentId());
    }

    /**
     * 更新父节点
     * @param parentId
     */
    private void updateParentNode(String parentId) {
        List<GroupInfoDto> groupInfos = GroupContext.getInstance().getGroupInfoList();
        if (StringUtils.isNotBlank(parentId)) {
            if (GroupContext.getInstance().getAllChildGroupId(parentId).size() > 1) {
                groupInfos.forEach(groupInfo -> {
                    if (groupInfo.getGroupId().equalsIgnoreCase(parentId)) {
                        groupInfo.setIsFinally(false);
                    }
                });
            } else {
                groupInfos.forEach(groupInfo -> {
                    if (groupInfo.getGroupId().equalsIgnoreCase(parentId)) {
                        groupInfo.setIsFinally(true);
                    }
                });
            }
        }
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
