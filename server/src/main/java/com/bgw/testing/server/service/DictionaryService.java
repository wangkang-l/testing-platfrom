package com.bgw.testing.server.service;

import com.bgw.testing.common.dto.DictionaryDto;
import com.bgw.testing.dao.mapper.bgw_automation.TsDictionaryMapper;
import com.bgw.testing.dao.pojo.bgw_automation.TsDictionary;
import com.bgw.testing.server.util.BaseStringUtils;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DictionaryService {

    @Autowired
    private TsDictionaryMapper tsDictionaryMapper;

    /**
     * 根据类型查询字典信息
     * @param dictType
     * @return
     */
    public List<DictionaryDto> getDictionaryByDictType(String dictType){
        List<TsDictionary> tsDictionaries = tsDictionaryMapper.selectByDictType(dictType);
        if (tsDictionaries != null && tsDictionaries.size() > 0) {
            return tsDictionaries.parallelStream()
                    .map(this::convertToDictionaryDto)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * 新增字典值
     * @add TsDictionary
     * @return
     */
    public void addDictionary(DictionaryDto dictionaryDto){
        TsDictionary tsDictionary = convertToTsDictionary(dictionaryDto);
        tsDictionary.setId(BaseStringUtils.uuidSimple());
        tsDictionary.setCreateTime(new Date());
        tsDictionary.setCreateBy("System");
        tsDictionaryMapper.insertSelective(tsDictionary);
    }

    /**
     * 根据id删除字典信息
     * @delete Dictionaryid
     * @Void
     */
    public void deleteDictionary(String id){
        if (StringUtil.isNotBlank(id)) {
            tsDictionaryMapper.deleteByPrimaryKey(id);
        }
    }

    /**
     * 根据id修改字典信息
     * @set Dictionaryid
     * @Void
     */
    public void updateDictionary(DictionaryDto dictionaryDto){
        TsDictionary tsDictionary = convertToTsDictionary(dictionaryDto);
        tsDictionary.setUpdateTime(new Date());
        tsDictionary.setUpdateBy("System");
        tsDictionaryMapper.updateByPrimaryKey(tsDictionary);
    }

    private DictionaryDto convertToDictionaryDto(TsDictionary tsDictionary) {
        DictionaryDto dictionaryDto = new DictionaryDto();
        dictionaryDto.setDictId(tsDictionary.getId());
        dictionaryDto.setDictType(tsDictionary.getDictType());
        dictionaryDto.setDictNo(tsDictionary.getDictNo());
        dictionaryDto.setDictValue(tsDictionary.getDictValue());
        dictionaryDto.setOrder(tsDictionary.getOrderNo());
        dictionaryDto.setRemark(tsDictionary.getRemark());
        return dictionaryDto;
    }

    private TsDictionary convertToTsDictionary(DictionaryDto dictionaryDto) {
        TsDictionary tsDictionary = new TsDictionary();
        tsDictionary.setId(dictionaryDto.getDictId());
        tsDictionary.setDictType(dictionaryDto.getDictType());
        tsDictionary.setDictNo(dictionaryDto.getDictNo());
        tsDictionary.setDictValue(dictionaryDto.getDictValue());
        tsDictionary.setOrderNo(dictionaryDto.getOrder());
        tsDictionary.setRemark(dictionaryDto.getRemark());
        return tsDictionary;
    }

}





