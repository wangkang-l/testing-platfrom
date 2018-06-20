package com.bgw.testing.dao.mapper.bgw_automation;

import com.bgw.testing.dao.pojo.bgw_automation.TsReportCaseResult;

import java.util.List;

public interface TsReportCaseResultMapper {
    int deleteByPrimaryKey(String id);

    int insert(TsReportCaseResult record);

    int insertSelective(TsReportCaseResult record);

    TsReportCaseResult selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TsReportCaseResult record);

    int updateByPrimaryKey(TsReportCaseResult record);

    List<TsReportCaseResult> selectByBatchNo(String batchNo);
}