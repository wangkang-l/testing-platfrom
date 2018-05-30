package com.bgw.testing.server.service;

import com.bgw.testing.common.AppConst;
import com.bgw.testing.common.dto.CaseContentDto;
import com.bgw.testing.common.dto.CaseInfoDto;
import com.bgw.testing.common.dto.PageInfo;
import com.bgw.testing.dao.mapper.bgw_automation.TsCaseInfoMapper;
import com.bgw.testing.dao.pojo.bgw_automation.TsCaseInfo;
import com.bgw.testing.server.util.BaseJsonUtils;
import com.bgw.testing.server.util.BaseStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class CaseService {

    @Autowired
    private TsCaseInfoMapper tsCaseInfoMapper;
    @Autowired
    private RedisService redisService;

    @PostConstruct
    public void initCaseInfo() {
        redisService.delPattern(AppConst.DEFAULT_REDIS_DB, AppConst.REDIS_KEY + "*");
        getAllCaseInfo();
    }

    /**
     * 获取所有用例信息
     * @return
     */
    private List<CaseInfoDto> getAllCaseInfo() {
        List<TsCaseInfo> tsCaseInfos = tsCaseInfoMapper.selectAll();
        List<CaseInfoDto> caseInfoDtos = new ArrayList<>();
        if (tsCaseInfos != null && tsCaseInfos.size() > 0) {
            tsCaseInfos.forEach(tsCaseInfo -> {
                CaseInfoDto caseInfoDto = convertToCaseInfoDto(tsCaseInfo);
                caseInfoDtos.add(caseInfoDto);
                redisService.hSet(AppConst.DEFAULT_REDIS_DB, AppConst.REDIS_KEY + tsCaseInfo.getGroupId(), tsCaseInfo.getId(), BaseJsonUtils.writeValue(caseInfoDto));
            });
        }
        return caseInfoDtos;
    }

    /**
     * 分页查询
     * @param groupId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<CaseInfoDto> getCaseInfo(String groupId, int pageNum, int pageSize) {
        Map<String, String> data = redisService.hGetAll(AppConst.DEFAULT_REDIS_DB, AppConst.REDIS_KEY + groupId);

        //根据组内用例按照优先级排序
        TreeMap<Integer, List<CaseInfoDto>> treeMap = new TreeMap<>();
        data.keySet().forEach(key -> {
            CaseInfoDto caseInfoDto = BaseJsonUtils.readValue(data.get(key), CaseInfoDto.class);
            if (!treeMap.containsKey(caseInfoDto.getPriority())) {
                treeMap.put(caseInfoDto.getPriority(), new ArrayList<>());
            }
            treeMap.get(caseInfoDto.getPriority()).add(caseInfoDto);
        });

        //相同优先级的用例在根据更新时间倒序排序
        List<CaseInfoDto> result = new ArrayList<>();
        treeMap.keySet().forEach(key -> {
            List<CaseInfoDto> caseInfoDtos = treeMap.get(key);
            Collections.sort(caseInfoDtos, new CaseSortClass());
            result.addAll(caseInfoDtos);
        });

        return new PageInfo<>(result, pageNum, pageSize);
    }

    /**
     * 新增用例
     * @param caseInfoDto
     */
    public void addCaseInfo(CaseInfoDto caseInfoDto) {
        caseInfoDto.setCaseId(BaseStringUtils.uuidSimple());
        caseInfoDto.setCreateTime(new Date());
        caseInfoDto.setCreateBy("System");
        caseInfoDto.setUpdateTime(new Date());
        caseInfoDto.setUpdateBy("System");
        tsCaseInfoMapper.insertSelective(convertToTsCaseInfo(caseInfoDto));
        redisService.hSet(AppConst.DEFAULT_REDIS_DB, AppConst.REDIS_KEY + caseInfoDto.getGroupId(), caseInfoDto.getCaseId(), BaseJsonUtils.writeValue(caseInfoDto));
    }

    /**
     * 更新用例
     * @param caseInfoDto
     */
    public void updateCaseInfo(CaseInfoDto caseInfoDto) {
        caseInfoDto.setUpdateTime(new Date());
        caseInfoDto.setUpdateBy("System");
        tsCaseInfoMapper.updateByPrimaryKeySelective(convertToTsCaseInfo(caseInfoDto));
        redisService.hSet(AppConst.DEFAULT_REDIS_DB, AppConst.REDIS_KEY + caseInfoDto.getGroupId(), caseInfoDto.getCaseId(), BaseJsonUtils.writeValue(caseInfoDto));
    }

    /**
     * 删除用例
     * @param caseId
     */
    public void deleteCaseInfo(String groupId, String caseId) {
        tsCaseInfoMapper.deleteByPrimaryKey(caseId);
        redisService.hDel(AppConst.DEFAULT_REDIS_DB, AppConst.REDIS_KEY + groupId, caseId);
    }

    private CaseInfoDto convertToCaseInfoDto(TsCaseInfo tsCaseInfo) {
        CaseInfoDto caseInfoDto = new CaseInfoDto();
        caseInfoDto.setCaseId(tsCaseInfo.getId());
        caseInfoDto.setCaseName(tsCaseInfo.getCaseName());
        caseInfoDto.setDescription(tsCaseInfo.getDescription());
        caseInfoDto.setGroupId(tsCaseInfo.getGroupId());
        caseInfoDto.setPriority(tsCaseInfo.getPriority() == null ? 0 : tsCaseInfo.getPriority());
        caseInfoDto.setStatus(tsCaseInfo.getStatus());
        caseInfoDto.setCreateTime(tsCaseInfo.getCreateTime());
        caseInfoDto.setCreateBy(tsCaseInfo.getCreateBy());
        caseInfoDto.setUpdateTime(tsCaseInfo.getUpdateTime());
        caseInfoDto.setUpdateBy(tsCaseInfo.getUpdateBy());
        caseInfoDto.setCaseContent(BaseJsonUtils.readValue(tsCaseInfo.getCaseContent(), CaseContentDto.class));
        return caseInfoDto;
    }

    private TsCaseInfo convertToTsCaseInfo(CaseInfoDto caseInfoDto) {
        TsCaseInfo tsCaseInfo = new TsCaseInfo();
        tsCaseInfo.setId(caseInfoDto.getCaseId());
        tsCaseInfo.setCaseName(caseInfoDto.getCaseName());
        tsCaseInfo.setDescription(caseInfoDto.getDescription());
        tsCaseInfo.setGroupId(caseInfoDto.getGroupId());
        tsCaseInfo.setPriority(caseInfoDto.getPriority() == null ? 0 : caseInfoDto.getPriority());
        tsCaseInfo.setStatus(caseInfoDto.getStatus());
        tsCaseInfo.setCreateTime(caseInfoDto.getCreateTime());
        tsCaseInfo.setCreateBy(caseInfoDto.getCreateBy());
        tsCaseInfo.setUpdateTime(caseInfoDto.getUpdateTime());
        tsCaseInfo.setUpdateBy(caseInfoDto.getUpdateBy());
        tsCaseInfo.setCaseContent(BaseJsonUtils.writeValue(caseInfoDto.getCaseContent()));
        return tsCaseInfo;
    }

    public class CaseSortClass implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            CaseInfoDto case1 = (CaseInfoDto) o1;
            CaseInfoDto case2 = (CaseInfoDto) o2;
            //更新时间倒序排序
            if (case1.getUpdateTime().getTime() > case2.getUpdateTime().getTime()) {
                return -1;
            } else if (case1.getUpdateTime().getTime() < case2.getUpdateTime().getTime()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

}
