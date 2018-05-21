package com.bgw.testing.dao.bgw_automation.pojo;

public class TsReportStepResultWithBLOBs extends TsReportStepResult {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ts_report_step_result.step_content
     *
     * @mbg.generated
     */
    private String stepContent;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ts_report_step_result.request_content
     *
     * @mbg.generated
     */
    private String requestContent;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ts_report_step_result.response_content
     *
     * @mbg.generated
     */
    private String responseContent;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ts_report_step_result.error_info
     *
     * @mbg.generated
     */
    private String errorInfo;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ts_report_step_result.step_content
     *
     * @return the value of ts_report_step_result.step_content
     *
     * @mbg.generated
     */
    public String getStepContent() {
        return stepContent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ts_report_step_result.step_content
     *
     * @param stepContent the value for ts_report_step_result.step_content
     *
     * @mbg.generated
     */
    public void setStepContent(String stepContent) {
        this.stepContent = stepContent == null ? null : stepContent.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ts_report_step_result.request_content
     *
     * @return the value of ts_report_step_result.request_content
     *
     * @mbg.generated
     */
    public String getRequestContent() {
        return requestContent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ts_report_step_result.request_content
     *
     * @param requestContent the value for ts_report_step_result.request_content
     *
     * @mbg.generated
     */
    public void setRequestContent(String requestContent) {
        this.requestContent = requestContent == null ? null : requestContent.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ts_report_step_result.response_content
     *
     * @return the value of ts_report_step_result.response_content
     *
     * @mbg.generated
     */
    public String getResponseContent() {
        return responseContent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ts_report_step_result.response_content
     *
     * @param responseContent the value for ts_report_step_result.response_content
     *
     * @mbg.generated
     */
    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent == null ? null : responseContent.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ts_report_step_result.error_info
     *
     * @return the value of ts_report_step_result.error_info
     *
     * @mbg.generated
     */
    public String getErrorInfo() {
        return errorInfo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ts_report_step_result.error_info
     *
     * @param errorInfo the value for ts_report_step_result.error_info
     *
     * @mbg.generated
     */
    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo == null ? null : errorInfo.trim();
    }
}