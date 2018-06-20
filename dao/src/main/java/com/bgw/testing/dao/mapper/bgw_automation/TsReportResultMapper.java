package com.bgw.testing.dao.mapper.bgw_automation;

import com.bgw.testing.dao.pojo.bgw_automation.TsReportResult;

import java.util.List;

public interface TsReportResultMapper {
    int deleteByPrimaryKey(String batchNo);

    int insert(TsReportResult record);

    int insertSelective(TsReportResult record);

    TsReportResult selectByPrimaryKey(String batchNo);

    int updateByPrimaryKeySelective(TsReportResult record);

    int updateByPrimaryKey(TsReportResult record);

    List<TsReportResult> selectAll();
}