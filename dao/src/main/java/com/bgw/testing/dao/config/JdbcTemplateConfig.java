package com.bgw.testing.dao.config;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@ConditionalOnProperty({"bgw20180418test1.mysql.url", "bgw_automation.mysql.url"})
@ComponentScan(basePackages = {"com.bgw.testing.dao"})
public class JdbcTemplateConfig {

    private Map<String, Object> initMap;

    @Value("${mysql.user}")
    private String user;
    @Value("${mysql.pass}")
    private String pass;
    @Value("${mysql.max.active:10}")
    private String maxActive;
    @Value("${bgw20180418test1.mysql.url}")
    private String bgw20180418test1TestUrl;
    @Value("${bgw_automation.mysql.url}")
    private String bgwAutomationTestUrl;

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

    @Bean(name = "dsBgw20180418test1")
    public DataSource dsBgw20180418test1() {
        log.info("初始化bgw20180418test1数据源");
        try {
            return DruidDataSourceFactory.createDataSource(dbProps(bgw20180418test1TestUrl));
        } catch (Exception e) {
            log.error("无法获得数据源[{}]:{}", bgw20180418test1TestUrl, ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("无法获得数据源");
        }
    }

    @Bean(name = "templateBgw20180418test1")
    public JdbcTemplate templateBgw20180418test1() {
        return new JdbcTemplate(this.dsBgw20180418test1());
    }

    @Bean(name = "namedTemplateBgw20180418test1")
    public NamedParameterJdbcTemplate namedTemplateBgw20180418test1() {
        return new NamedParameterJdbcTemplate(this.dsBgw20180418test1());
    }

    @Bean(name = "tmBgw20180418test1")
    public DataSourceTransactionManager tsBgw20180418test1() {
        return new DataSourceTransactionManager(this.dsBgw20180418test1());
    }

    @Primary
    @Bean(name = "dsBgwAutomation")
    public DataSource dsBgwAutomation() {
        log.info("初始化BgwAutomation数据源");
        try {
            return DruidDataSourceFactory.createDataSource(dbProps(bgwAutomationTestUrl));
        } catch (Exception e) {
            log.error("无法获得数据源[{}]:{}", bgwAutomationTestUrl, ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("无法获得数据源");
        }
    }

    @Bean(name = "templateBgwAutomation")
    public JdbcTemplate templateBgwAutomation() {
        return new JdbcTemplate(this.dsBgwAutomation());
    }

    @Bean(name = "namedTemplateBgwAutomation")
    public NamedParameterJdbcTemplate namedTemplateBgwAutomation() {
        return new NamedParameterJdbcTemplate(this.dsBgwAutomation());
    }

    @Bean(name = "tmBgwAutomation")
    public DataSourceTransactionManager tsBgwAutomation() {
        return new DataSourceTransactionManager(this.dsBgwAutomation());
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
