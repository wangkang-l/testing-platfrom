package com.bgw.testing.dao.bgw_automation.pojo;

import java.util.Date;

public class TsReportCaseResult {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ts_report_case_result.id
     *
     * @mbg.generated
     */
    private String id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ts_report_case_result.batch_no
     *
     * @mbg.generated
     */
    private String batchNo;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ts_report_case_result.case_id
     *
     * @mbg.generated
     */
    private String caseId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ts_report_case_result.case_name
     *
     * @mbg.generated
     */
    private String caseName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ts_report_case_result.result
     *
     * @mbg.generated
     */
    private Boolean result;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ts_report_case_result.start_time
     *
     * @mbg.generated
     */
    private Date startTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ts_report_case_result.end_time
     *
     * @mbg.generated
     */
    private Date endTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ts_report_case_result.id
     *
     * @return the value of ts_report_case_result.id
     *
     * @mbg.generated
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ts_report_case_result.id
     *
     * @param id the value for ts_report_case_result.id
     *
     * @mbg.generated
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ts_report_case_result.batch_no
     *
     * @return the value of ts_report_case_result.batch_no
     *
     * @mbg.generated
     */
    public String getBatchNo() {
        return batchNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ts_report_case_result.batch_no
     *
     * @param batchNo the value for ts_report_case_result.batch_no
     *
     * @mbg.generated
     */
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo == null ? null : batchNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ts_report_case_result.case_id
     *
     * @return the value of ts_report_case_result.case_id
     *
     * @mbg.generated
     */
    public String getCaseId() {
        return caseId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ts_report_case_result.case_id
     *
     * @param caseId the value for ts_report_case_result.case_id
     *
     * @mbg.generated
     */
    public void setCaseId(String caseId) {
        this.caseId = caseId == null ? null : caseId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ts_report_case_result.case_name
     *
     * @return the value of ts_report_case_result.case_name
     *
     * @mbg.generated
     */
    public String getCaseName() {
        return caseName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ts_report_case_result.case_name
     *
     * @param caseName the value for ts_report_case_result.case_name
     *
     * @mbg.generated
     */
    public void setCaseName(String caseName) {
        this.caseName = caseName == null ? null : caseName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ts_report_case_result.result
     *
     * @return the value of ts_report_case_result.result
     *
     * @mbg.generated
     */
    public Boolean getResult() {
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ts_report_case_result.result
     *
     * @param result the value for ts_report_case_result.result
     *
     * @mbg.generated
     */
    public void setResult(Boolean result) {
        this.result = result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ts_report_case_result.start_time
     *
     * @return the value of ts_report_case_result.start_time
     *
     * @mbg.generated
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ts_report_case_result.start_time
     *
     * @param startTime the value for ts_report_case_result.start_time
     *
     * @mbg.generated
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ts_report_case_result.end_time
     *
     * @return the value of ts_report_case_result.end_time
     *
     * @mbg.generated
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ts_report_case_result.end_time
     *
     * @param endTime the value for ts_report_case_result.end_time
     *
     * @mbg.generated
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}