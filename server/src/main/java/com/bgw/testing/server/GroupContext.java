package com.bgw.testing.server;

import com.bgw.testing.common.dto.GroupInfoDto;
import com.bgw.testing.server.util.BeanCopyUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class GroupContext {

    private static List<GroupInfoDto> groupInfoList = new ArrayList<>();

    private GroupContext(){
    }

    public static GroupContext getInstance(){
        return GroupContext.GroupContextHolder.gInstance;
    }

    private static class GroupContextHolder {
        private static final GroupContext gInstance = new GroupContext();
    }

    public List<GroupInfoDto> getGroupInfoList() {
        return groupInfoList;
    }

    public GroupInfoDto getGroupInfo(String groupId) {
        return groupInfoList.stream()
                .filter(tempGroupInfo -> tempGroupInfo.getGroupId().equals(groupId))
                .findFirst()
                .orElse(null);
    }



    public List<GroupInfoDto> getCurrentChildGroupInfo(String groupId) {
        List<GroupInfoDto> currentGroupInfoDtos = new ArrayList<>();
        groupInfoList.forEach(groupInfo -> {
            if(StringUtils.isNotBlank(groupInfo.getParentId()) && groupInfo.getParentId().equals(groupId)){
                currentGroupInfoDtos.add(groupInfo);
            }
        });
        return currentGroupInfoDtos;
    }

    /**
     * 根据父组ID查询所有子组的ID(包括自身)
     * @param groupId
     * @return
     */
    public List<String> getAllChildGroupId(String groupId) {
        List<String> groups = new ArrayList<>();
        groups.add(groupId);
        groupInfoList.forEach(groupInfo -> {
            if (StringUtils.isNotBlank(groupInfo.getParentId()) && groupInfo.getParentId().equalsIgnoreCase(groupId)) {
                if (!groups.contains(groupInfo.getGroupId())) {
                    groups.add(groupInfo.getGroupId());
                }
                if (!groupInfo.getIsFinally()) {
                    groups.addAll(getAllChildGroupId(groupInfo.getGroupId()));
                }
            }
        });
        return groups;
    }

    /**
     * 根据组ID获取完整的组路径
     * @param groupId
     * @return
     */
    public String getCompleteGroupPath(String groupId) {
        StringBuffer groupPath = new StringBuffer();
        groupInfoList.forEach(groupInfo -> {
            if (groupInfo.getGroupId().equalsIgnoreCase(groupId)) {
                groupPath.insert(0, groupInfo.getGroupName());
                if (StringUtils.isNotBlank(groupInfo.getParentId())) {
                    groupPath.insert(0, "/");
                    groupPath.insert(0, getCompleteGroupPath(groupInfo.getParentId()));
                }
            }
        });

        if (!groupPath.toString().substring(0,1).equals("/")) {
            groupPath.insert(0, "/");
        }

        return groupPath.toString();
    }

    public void initGroup(List<GroupInfoDto> groupInfoDtos) {
        if (groupInfoDtos != null && groupInfoDtos.size() > 0) {
            groupInfoList.addAll(groupInfoDtos);
        }
        groupInfoList.parallelStream().forEach(groupInfo -> {
            groupInfo.setPath(getCompleteGroupPath(groupInfo.getGroupId()));
        });
    }

    public void addGroupInfo(GroupInfoDto groupInfoDto){
        groupInfoList.forEach(groupInfo -> {
            if (groupInfo.getGroupId().equals(groupInfoDto.getParentId())) {
               groupInfo.setIsFinally(false);
            }
        });
        groupInfoList.add(groupInfoDto);
        groupInfoList.forEach(groupInfo -> {
            if(groupInfo.getGroupId().equals(groupInfoDto.getGroupId())) {
                groupInfo.setPath(getCompleteGroupPath(groupInfo.getGroupId()));
            }
        });
    }

    public void updateGroupInfo(GroupInfoDto groupInfoDto,GroupInfoDto oldGroupInfoDto){
        if(groupInfoDto.getParentId().equals(oldGroupInfoDto.getParentId())){
            groupInfoList.forEach(groupInfo -> {
                //未修改父节点
                if(groupInfo.getGroupId().equals(groupInfoDto.getGroupId())) {
                    groupInfo.setGroupName(groupInfoDto.getGroupName());
                    groupInfo.setPriority(groupInfoDto.getPriority());
                    groupInfo.setPath(getCompleteGroupPath(groupInfoDto.getGroupId()));
                }
            });
        } else{
            groupInfoList.forEach(groupInfo -> {
                //修改本节点
                if(groupInfo.getGroupId().equals(groupInfoDto.getGroupId())) {
                    groupInfo.setGroupName(groupInfoDto.getGroupName());
                    groupInfo.setParentId(groupInfoDto.getParentId());
                    groupInfo.setPriority(groupInfoDto.getPriority());
                    groupInfo.setPath(getCompleteGroupPath(groupInfoDto.getGroupId()));

                }else if(groupInfo.getGroupId().equals(groupInfoDto.getParentId())) {
                    //修改新父节点
                    groupInfo.setIsFinally(false);
                }else if(groupInfo.getGroupId().equals(oldGroupInfoDto.getGroupId())){
                    // 修改原父节点
                    groupInfo.setIsFinally(oldGroupInfoDto.getIsFinally());
                }
            });
        }
        if(!groupInfoDto.getIsFinally()){
            List<String> groupIdList = getAllChildGroupId(groupInfoDto.getGroupId());
            groupInfoList.forEach(groupInfo -> {
                if(groupIdList.contains(groupInfo.getGroupId())) {
                    groupInfo.setPath(getCompleteGroupPath(groupInfo.getGroupId()));
                }
            });
        }
    }

}
