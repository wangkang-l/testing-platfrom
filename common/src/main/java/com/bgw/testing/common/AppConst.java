package com.bgw.testing.common;

public interface AppConst {
    String SERVICE_NAME = "testing-platform";
    String SERVICE_NAME_CN = "自动化测试平台";
    String VERSION = "v1";
    String DEFAULT_DAO_CLASS_PATH = "com.bgw.testing.dao.config.JdbcTemplateAutowired";
    String BASE_PATH = "/" + SERVICE_NAME + "/" + VERSION + "/";
}
