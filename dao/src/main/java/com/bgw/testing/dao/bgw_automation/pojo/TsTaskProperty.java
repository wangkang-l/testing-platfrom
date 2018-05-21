package com.bgw.testing.dao.bgw_automation.pojo;

public class TsTaskProperty {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ts_task_property.id
     *
     * @mbg.generated
     */
    private String id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ts_task_property.task_id
     *
     * @mbg.generated
     */
    private String taskId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ts_task_property.group_id
     *
     * @mbg.generated
     */
    private String groupId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ts_task_property.priority
     *
     * @mbg.generated
     */
    private Integer priority;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ts_task_property.case_id
     *
     * @mbg.generated
     */
    private String caseId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ts_task_property.id
     *
     * @return the value of ts_task_property.id
     *
     * @mbg.generated
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ts_task_property.id
     *
     * @param id the value for ts_task_property.id
     *
     * @mbg.generated
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ts_task_property.task_id
     *
     * @return the value of ts_task_property.task_id
     *
     * @mbg.generated
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ts_task_property.task_id
     *
     * @param taskId the value for ts_task_property.task_id
     *
     * @mbg.generated
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId == null ? null : taskId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ts_task_property.group_id
     *
     * @return the value of ts_task_property.group_id
     *
     * @mbg.generated
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ts_task_property.group_id
     *
     * @param groupId the value for ts_task_property.group_id
     *
     * @mbg.generated
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId == null ? null : groupId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ts_task_property.priority
     *
     * @return the value of ts_task_property.priority
     *
     * @mbg.generated
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ts_task_property.priority
     *
     * @param priority the value for ts_task_property.priority
     *
     * @mbg.generated
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ts_task_property.case_id
     *
     * @return the value of ts_task_property.case_id
     *
     * @mbg.generated
     */
    public String getCaseId() {
        return caseId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ts_task_property.case_id
     *
     * @param caseId the value for ts_task_property.case_id
     *
     * @mbg.generated
     */
    public void setCaseId(String caseId) {
        this.caseId = caseId == null ? null : caseId.trim();
    }
}