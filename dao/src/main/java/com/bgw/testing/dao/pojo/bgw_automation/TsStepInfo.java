package com.bgw.testing.dao.pojo.bgw_automation;

import java.util.Date;

public class TsStepInfo {
    private String id;

    private String caseId;

    private String description;

    private String precondition;

    private String type;

    private Integer priority;

    private String func;

    private String templateId;

    private String httpRequest;

    private String redisRequest;

    private String mysqlRequest;

    private String initTemporaryVariables;

    private String keyInGlobalVariable;

    private String keyInEnvironmentVariable;

    private String keyInTemporaryVariable;

    private String element;

    private String extractor;

    private String verifier;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String updateBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId == null ? null : caseId.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getPrecondition() {
        return precondition;
    }

    public void setPrecondition(String precondition) {
        this.precondition = precondition == null ? null : precondition.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func == null ? null : func.trim();
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId == null ? null : templateId.trim();
    }

    public String getHttpRequest() {
        return httpRequest;
    }

    public void setHttpRequest(String httpRequest) {
        this.httpRequest = httpRequest == null ? null : httpRequest.trim();
    }

    public String getRedisRequest() {
        return redisRequest;
    }

    public void setRedisRequest(String redisRequest) {
        this.redisRequest = redisRequest == null ? null : redisRequest.trim();
    }

    public String getMysqlRequest() {
        return mysqlRequest;
    }

    public void setMysqlRequest(String mysqlRequest) {
        this.mysqlRequest = mysqlRequest == null ? null : mysqlRequest.trim();
    }

    public String getInitTemporaryVariables() {
        return initTemporaryVariables;
    }

    public void setInitTemporaryVariables(String initTemporaryVariables) {
        this.initTemporaryVariables = initTemporaryVariables == null ? null : initTemporaryVariables.trim();
    }

    public String getKeyInGlobalVariable() {
        return keyInGlobalVariable;
    }

    public void setKeyInGlobalVariable(String keyInGlobalVariable) {
        this.keyInGlobalVariable = keyInGlobalVariable == null ? null : keyInGlobalVariable.trim();
    }

    public String getKeyInEnvironmentVariable() {
        return keyInEnvironmentVariable;
    }

    public void setKeyInEnvironmentVariable(String keyInEnvironmentVariable) {
        this.keyInEnvironmentVariable = keyInEnvironmentVariable == null ? null : keyInEnvironmentVariable.trim();
    }

    public String getKeyInTemporaryVariable() {
        return keyInTemporaryVariable;
    }

    public void setKeyInTemporaryVariable(String keyInTemporaryVariable) {
        this.keyInTemporaryVariable = keyInTemporaryVariable == null ? null : keyInTemporaryVariable.trim();
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element == null ? null : element.trim();
    }

    public String getExtractor() {
        return extractor;
    }

    public void setExtractor(String extractor) {
        this.extractor = extractor == null ? null : extractor.trim();
    }

    public String getVerifier() {
        return verifier;
    }

    public void setVerifier(String verifier) {
        this.verifier = verifier == null ? null : verifier.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }
}