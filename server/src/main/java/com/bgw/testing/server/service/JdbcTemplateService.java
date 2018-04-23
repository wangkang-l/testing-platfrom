package com.bgw.testing.server.service;

import com.bgw.testing.dao.integration.pojo.TCase;
import com.bgw.testing.server.util.BaseJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JdbcTemplateService {

    @Autowired
    @Qualifier(value = "templateIntegrationTest")
    private JdbcTemplate template;

    public TCase getTestCase(String sql, Object[] params) {
        return BaseJsonUtils.readValue(BaseJsonUtils.writeValue(template.queryForMap(sql, params)), TCase.class);
    }

}
