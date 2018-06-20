package com.bgw.testing.server.service;

import com.bgw.testing.common.dto.EnvironmentDto;
import com.bgw.testing.common.enums.ErrorCode;
import com.bgw.testing.dao.mapper.bgw_automation.TsEnvironmentListMapper;
import com.bgw.testing.dao.pojo.bgw_automation.TsEnvironmentList;
import com.bgw.testing.server.config.ServerException;
import com.bgw.testing.server.util.BaseStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnvironmentService {

    @Autowired
    private TsEnvironmentListMapper tsEnvironmentListMapper;

    public List<EnvironmentDto> getEnvironmentList() {
        List<TsEnvironmentList> tsEnvironmentLists = tsEnvironmentListMapper.selectAll();
        if (tsEnvironmentLists != null && tsEnvironmentLists.size() > 0) {
            return tsEnvironmentLists.stream()
                    .map(EnvironmentService::convertToEnvironmentDto)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public void addEnvironmentList(EnvironmentDto environmentDto) {
        if (!isExist(environmentDto.getEnvironmentName())) {
            TsEnvironmentList tsEnvironmentList = convertToTsEnvironmentList(environmentDto);
            tsEnvironmentList.setId(BaseStringUtils.uuidSimple());
            tsEnvironmentList.setCreateTime(new Date());
            tsEnvironmentList.setCreateBy("System");
            tsEnvironmentListMapper.insertSelective(tsEnvironmentList);
        } else {
            throw new ServerException(ErrorCode.ALREADY_EXISTS, environmentDto.getEnvironmentName());
        }
    }

    private boolean isExist(String environmentName) {
        TsEnvironmentList tsEnvironmentList = tsEnvironmentListMapper.selectByEnvironmentName(environmentName);
        if (tsEnvironmentList == null) {
            return false;
        } else {
            return true;
        }
    }

    public void updateEnvironmentList(EnvironmentDto environmentDto) {
        TsEnvironmentList tsEnvironmentList = convertToTsEnvironmentList(environmentDto);
        tsEnvironmentList.setUpdateTime(new Date());
        tsEnvironmentList.setUpdateBy("System");
        tsEnvironmentListMapper.updateByPrimaryKeySelective(tsEnvironmentList);
    }

    public void delEnvironmentList(String environmentId) {
        tsEnvironmentListMapper.deleteByPrimaryKey(environmentId);
    }

    private TsEnvironmentList convertToTsEnvironmentList(EnvironmentDto environmentDto) {
        TsEnvironmentList tsEnvironmentList = new TsEnvironmentList();
        tsEnvironmentList.setId(environmentDto.getEnvironmentId());
        tsEnvironmentList.setName(environmentDto.getEnvironmentName());
        tsEnvironmentList.setRemark(environmentDto.getRemark());
        return tsEnvironmentList;
    }

    private static EnvironmentDto convertToEnvironmentDto(TsEnvironmentList tsEnvironmentList) {
        EnvironmentDto environmentDto = new EnvironmentDto();
        environmentDto.setEnvironmentId(tsEnvironmentList.getId());
        environmentDto.setEnvironmentName(tsEnvironmentList.getName());
        environmentDto.setRemark(tsEnvironmentList.getRemark());
        return environmentDto;
    }
}
