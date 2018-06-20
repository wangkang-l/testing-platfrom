package com.bgw.testing.dao.mapper.bgw_automation;

import com.bgw.testing.dao.pojo.bgw_automation.TsStepInfo;

import java.util.List;

public interface TsStepInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(TsStepInfo record);

    int insertSelective(TsStepInfo record);

    TsStepInfo selectByPrimaryKey(String id);

    List<TsStepInfo> selectByCaseId(String caseId);

    int updateByPrimaryKeySelective(TsStepInfo record);

    int updateByPrimaryKey(TsStepInfo record);

    int deleteByCaseId(String caseId);
}