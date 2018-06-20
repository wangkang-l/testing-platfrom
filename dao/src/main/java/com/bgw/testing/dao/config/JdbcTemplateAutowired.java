package com.bgw.testing.dao.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class JdbcTemplateAutowired {

    @Autowired
    @Qualifier(value = "templateBgw20180418test1")
    private JdbcTemplate templateBgw20180418test1;

    @Autowired
    @Qualifier(value = "templateBgwAutomation")
    private JdbcTemplate template;

}
