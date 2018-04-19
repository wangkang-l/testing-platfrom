package com.bgw.testing.server.service;

import com.bgw.testing.dao.integration.mapper.TCaseMapper;
import com.bgw.testing.dao.integration.pojo.TCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntegrationService {

    @Autowired
    private TCaseMapper tCaseMapper;

    public TCase getTCase(String id) {
        return tCaseMapper.selectByPrimaryKey(id);
    }

}
