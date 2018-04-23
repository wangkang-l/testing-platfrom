package com.bgw.testing.dao.config;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@ConditionalOnProperty({"integrationTest.mysql.url"})
@ComponentScan(basePackages = {"com.bgw.testing.dao"})
public class JdbcTemplateConfig {

    private Map<String, Object> initMap;

    @Value("${mysql.user}")
    private String user;
    @Value("${mysql.pass}")
    private String pass;
    @Value("${mysql.max.active:10}")
    private String maxActive;
    @Value("${integrationTest.mysql.url}")
    private String integrationTestUrl;

    @PostConstruct
    public void init() {
        initMap = new HashMap<>();
        initMap.put("driverClassName", "com.mysql.jdbc.Driver");
        initMap.put("initialSize", "1");
        initMap.put("minIdle", "1");
        initMap.put("maxWait", "20000");
        initMap.put("removeAbandoned", "true");
        initMap.put("removeAbandonedTimeout", "180");
        initMap.put("timeBetweenEvictionRunsMillis", "60000");
        initMap.put("minEvictableIdleTimeMillis", "300000");
        initMap.put("validationQuery", "SELECT 1");
        initMap.put("testWhileIdle", "true");
        initMap.put("testOnBorrow", "false");
        initMap.put("testOnReturn", "false");
        initMap.put("poolPreparedStatements", "true");
        initMap.put("maxPoolPreparedStatementPerConnectionSize", "50");
        initMap.put("initConnectionSqls", "SELECT 1");
        initMap.put("maxActive", maxActive + "");
    }

    @Primary
    @Bean(name = "dsIntegrationTest")
    public DataSource dsIntegrationTest() {
        log.info("初始化integrationTest数据源");
        try {
            return DruidDataSourceFactory.createDataSource(dbProps(integrationTestUrl));
        } catch (Exception e) {
            log.error("无法获得数据源[{}]:{}", integrationTestUrl, ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("无法获得数据源");
        }
    }

    @Bean(name = "templateIntegrationTest")
    public JdbcTemplate templateIntegrationTest() {
        return new JdbcTemplate(this.dsIntegrationTest());
    }

    @Bean(name = "namedTemplateIntegrationTest")
    public NamedParameterJdbcTemplate namedTemplateIntegrationTest() {
        return new NamedParameterJdbcTemplate(this.dsIntegrationTest());
    }

    @Bean(name = "tmIntegrationTest")
    public DataSourceTransactionManager tsIntegrationTest() {
        return new DataSourceTransactionManager(this.dsIntegrationTest());
    }

    private Map<String, Object> dbProps(String url) {
        Map<String, Object> dbProperties = new HashMap<>(initMap);
        dbProperties.put("url", url);
        dbProperties.put("username", user);
        if (StringUtils.isNotBlank(pass)) {
            dbProperties.put("password", pass);
        }
        return dbProperties;
    }

}
