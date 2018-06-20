package com.bgw.testing.server.controller;

import com.bgw.testing.common.AppConst;
import com.bgw.testing.dao.pojo.bgw_automation.TsDictionary;
import com.bgw.testing.server.service.DictionaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@Api(description = "字典管理")
@RequestMapping(value = AppConst.BASE_PATH + "dictionary")
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    @ApiOperation(value = "根据字典值类型获取信息")
    @RequestMapping(value = "/{dict_type}", method = RequestMethod.GET)
    public List<TsDictionary> getDictionaryBydictType(
            @PathVariable(value = "dict_type") String dictType) {
        return dictionaryService.getDictionaryByDictType(dictType);
    }

    @ApiOperation(value = "新增/修改字典值信息")
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public Boolean saveDictionary(@RequestBody TsDictionary tsDictionary){
        if (tsDictionary.getId() != null && !"".equals(tsDictionary.getId())){
            //当编号存在时，认为是修改
            dictionaryService.updateDictionary(tsDictionary);
        }else{
            //否则为新增
            dictionaryService.addDictionary(tsDictionary);
        }

        return true;
    }

    @ApiOperation(value = "删除字典值信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Boolean deleteDictionary(@PathVariable String id){
        dictionaryService.deleteDictionary(id);
        return true;
    }
}

