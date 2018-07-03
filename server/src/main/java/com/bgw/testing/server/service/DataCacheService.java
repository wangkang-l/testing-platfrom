package com.bgw.testing.server.service;

import com.bgw.testing.common.dto.CaseInfoDto;
import com.bgw.testing.server.util.BaseJsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataCacheService {

    @Autowired
    private RedisService redisService;

    private static final String REDIS_KEY_TEST_CASE = "test_case_";
    private static final String REDIS_KEY_INHERIT_TEST_CASE = "inherit_test_case";
    private static final String REDIS_KEY_TEST_CASE_LIST = "test_case_list";
    private static final Integer DEFAULT_REDIS_DB = 11;

    /**
     * 新增或更新用例
     * @param caseInfoDto
     */
    public void addOrUpdateCaseInfo(CaseInfoDto caseInfoDto) {
        if (caseInfoDto.isInherit()) {
            redisService.hSet(DEFAULT_REDIS_DB, REDIS_KEY_INHERIT_TEST_CASE, caseInfoDto.getCaseId(), BaseJsonUtils.writeValue(caseInfoDto));
        }
        redisService.hSet(DEFAULT_REDIS_DB, REDIS_KEY_TEST_CASE_LIST, caseInfoDto.getCaseId(), BaseJsonUtils.writeValue(caseInfoDto));
        redisService.hSet(DEFAULT_REDIS_DB, REDIS_KEY_TEST_CASE + caseInfoDto.getGroupId(), caseInfoDto.getCaseId(), BaseJsonUtils.writeValue(caseInfoDto));
    }

    /**
     * 移动用例
     * @param caseInfoDto
     * @param newGroupId
     */
    public void moveCaseInfo(CaseInfoDto caseInfoDto, String newGroupId) {
        redisService.hDel(DEFAULT_REDIS_DB, REDIS_KEY_TEST_CASE + caseInfoDto.getGroupId(), caseInfoDto.getCaseId());
        caseInfoDto.setGroupId(newGroupId);
        addOrUpdateCaseInfo(caseInfoDto);
    }

    /**
     * 删除用例
     * @param groupId
     * @param caseId
     */
    public void delCaseInfo(String groupId, String caseId) {
        redisService.hDel(DEFAULT_REDIS_DB, REDIS_KEY_TEST_CASE_LIST, caseId);
        redisService.hDel(DEFAULT_REDIS_DB, REDIS_KEY_TEST_CASE + groupId, caseId);
        redisService.hDel(DEFAULT_REDIS_DB, REDIS_KEY_INHERIT_TEST_CASE, caseId);
    }

    /**
     * 获取所有用例
     * @return
     */
    public List<CaseInfoDto> getAllCaseList() {
        return redisService.hGetAll(DEFAULT_REDIS_DB, REDIS_KEY_TEST_CASE_LIST)
                .keySet()
                .stream()
                .map(str -> BaseJsonUtils.readValue(str, CaseInfoDto.class))
                .collect(Collectors.toList());
    }

    /**
     * 根据groupId获取所有用例
     * @return
     */
    public List<CaseInfoDto> getAllCaseListByGroupId(String groupId) {
        return redisService.hGetAll(DEFAULT_REDIS_DB, REDIS_KEY_TEST_CASE + groupId)
                .values()
                .stream()
                .map(str -> BaseJsonUtils.readValue(str, CaseInfoDto.class))
                .collect(Collectors.toList());
    }

    /**
     * 获取可继承用例内容
     * @param caseId
     * @return
     */
    public CaseInfoDto getCaseInfo(String caseId) {
        return BaseJsonUtils.readValue(redisService.hGet(DEFAULT_REDIS_DB, REDIS_KEY_TEST_CASE_LIST, caseId), CaseInfoDto.class);
    }

    /**
     * 获取可继承用例内容
     * @param caseId
     * @return
     */
    public CaseInfoDto getInheritCaseInfo(String caseId) {
        return BaseJsonUtils.readValue(redisService.hGet(DEFAULT_REDIS_DB, REDIS_KEY_INHERIT_TEST_CASE, caseId), CaseInfoDto.class);
    }

    /**
     * 清理所有用例信息
     */
    public void clearCaseInfo() {
        redisService.del(DEFAULT_REDIS_DB, REDIS_KEY_TEST_CASE_LIST);
        redisService.del(DEFAULT_REDIS_DB, REDIS_KEY_INHERIT_TEST_CASE);
        redisService.delPattern(DEFAULT_REDIS_DB, REDIS_KEY_TEST_CASE + "*");
    }

}
