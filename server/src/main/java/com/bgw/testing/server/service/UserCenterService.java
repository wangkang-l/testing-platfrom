package com.bgw.testing.server.service;

import com.bgw.testing.dao.usercenter.mapper.UserBasic888888Mapper;
import com.bgw.testing.dao.usercenter.pojo.UserBasic888888;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCenterService {

    @Autowired
    private UserBasic888888Mapper userBasic888888Mapper;

    public UserBasic888888 getUserBasicInfo(String userId) {
        return userBasic888888Mapper.selectByPrimaryKey(userId);
    }

}
