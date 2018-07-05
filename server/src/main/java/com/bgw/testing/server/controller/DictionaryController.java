package com.bgw.testing.server.controller;

import com.bgw.testing.common.AppConst;
import com.bgw.testing.common.dto.DictionaryDto;
import com.bgw.testing.server.service.DictionaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@Api(description = "字典管理")
@RequestMapping(value = AppConst.BASE_PATH)
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    @ApiOperation(value = "根据字典值类型获取信息")
    @RequestMapping(value = "/dictionary/{dict_type}", method = RequestMethod.GET)
    public List<DictionaryDto> getDictionaryBydictType(
            @PathVariable(value = "dict_type") String dictType) {
        return dictionaryService.getDictionaryByDictType(dictType);
    }

    @ApiOperation(value = "新增字典值信息")
    @RequestMapping(value = "/dictionary", method = RequestMethod.POST)
    public Boolean addDictionary(@RequestBody DictionaryDto dictionaryDto){
        dictionaryService.addDictionary(dictionaryDto);
        return true;
    }

    @ApiOperation(value = "修改字典值信息")
    @RequestMapping(value = "/dictionary/{dict_id}", method = RequestMethod.PUT)
    public Boolean updateDictionary(@PathVariable(value = "dict_id") String dictId, @RequestBody DictionaryDto dictionaryDto){
        if (StringUtils.isNotBlank(dictId)){
            dictionaryService.updateDictionary(dictionaryDto);
        }
        return true;
    }

    @ApiOperation(value = "删除字典值信息")
    @RequestMapping(value = "/{dict_id}", method = RequestMethod.DELETE)
    public Boolean deleteDictionary(@PathVariable(value = "dict_id") String dictId){
        dictionaryService.deleteDictionary(dictId);
        return true;
    }
}

