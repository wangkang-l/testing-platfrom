package com.bgw.testing.dao.bgw_automation.pojo;

import java.util.Date;

public class TsReportStepResult {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ts_report_step_result.id
     *
     * @mbg.generated
     */
    private String id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ts_report_step_result.case_result_id
     *
     * @mbg.generated
     */
    private String caseResultId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ts_report_step_result.description
     *
     * @mbg.generated
     */
    private String description;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ts_report_step_result.step_type
     *
     * @mbg.generated
     */
    private String stepType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ts_report_step_result.result
     *
     * @mbg.generated
     */
    private Boolean result;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ts_report_step_result.start_time
     *
     * @mbg.generated
     */
    private Date startTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ts_report_step_result.end_time
     *
     * @mbg.generated
     */
    private Date endTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ts_report_step_result.id
     *
     * @return the value of ts_report_step_result.id
     *
     * @mbg.generated
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ts_report_step_result.id
     *
     * @param id the value for ts_report_step_result.id
     *
     * @mbg.generated
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ts_report_step_result.case_result_id
     *
     * @return the value of ts_report_step_result.case_result_id
     *
     * @mbg.generated
     */
    public String getCaseResultId() {
        return caseResultId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ts_report_step_result.case_result_id
     *
     * @param caseResultId the value for ts_report_step_result.case_result_id
     *
     * @mbg.generated
     */
    public void setCaseResultId(String caseResultId) {
        this.caseResultId = caseResultId == null ? null : caseResultId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ts_report_step_result.description
     *
     * @return the value of ts_report_step_result.description
     *
     * @mbg.generated
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ts_report_step_result.description
     *
     * @param description the value for ts_report_step_result.description
     *
     * @mbg.generated
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ts_report_step_result.step_type
     *
     * @return the value of ts_report_step_result.step_type
     *
     * @mbg.generated
     */
    public String getStepType() {
        return stepType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ts_report_step_result.step_type
     *
     * @param stepType the value for ts_report_step_result.step_type
     *
     * @mbg.generated
     */
    public void setStepType(String stepType) {
        this.stepType = stepType == null ? null : stepType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ts_report_step_result.result
     *
     * @return the value of ts_report_step_result.result
     *
     * @mbg.generated
     */
    public Boolean getResult() {
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ts_report_step_result.result
     *
     * @param result the value for ts_report_step_result.result
     *
     * @mbg.generated
     */
    public void setResult(Boolean result) {
        this.result = result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ts_report_step_result.start_time
     *
     * @return the value of ts_report_step_result.start_time
     *
     * @mbg.generated
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ts_report_step_result.start_time
     *
     * @param startTime the value for ts_report_step_result.start_time
     *
     * @mbg.generated
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ts_report_step_result.end_time
     *
     * @return the value of ts_report_step_result.end_time
     *
     * @mbg.generated
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ts_report_step_result.end_time
     *
     * @param endTime the value for ts_report_step_result.end_time
     *
     * @mbg.generated
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}