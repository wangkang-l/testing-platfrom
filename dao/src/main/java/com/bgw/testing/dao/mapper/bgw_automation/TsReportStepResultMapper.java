package com.bgw.testing.dao.mapper.bgw_automation;

import com.bgw.testing.dao.pojo.bgw_automation.TsReportStepResult;

import java.util.List;

public interface TsReportStepResultMapper {
    int deleteByPrimaryKey(String id);

    int insert(TsReportStepResult record);

    int insertSelective(TsReportStepResult record);

    TsReportStepResult selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TsReportStepResult record);

    int updateByPrimaryKeyWithBLOBs(TsReportStepResult record);

    int updateByPrimaryKey(TsReportStepResult record);

    List<TsReportStepResult> selectByCaseResultId(String caseResultId);
}