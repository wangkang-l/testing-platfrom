package com.bgw.testing.server.controller;

import com.bgw.testing.common.AppConst;
import com.bgw.testing.common.dto.PageInfo;
import com.bgw.testing.common.dto.TaskConfigDto;
import com.bgw.testing.server.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@Api(description = "任务管理")
@RequestMapping(value = AppConst.BASE_PATH)
public class TaskController {

    @Autowired
    private TaskService taskService;

    @ApiOperation(value = "新增任务")
    @RequestMapping(value = "/task", method = RequestMethod.POST)
    public Boolean addTaskConfig(@RequestBody @Valid TaskConfigDto taskConfigDto) {
        taskService.addTaskConfig(taskConfigDto);
        return true;
    }

    @ApiOperation(value = "立即执行任务")
    @RequestMapping(value = "/task/{task_id}", method = RequestMethod.GET)
    public Boolean executeTaskConfig(@PathVariable(name = "task_id") String taskId) {
        taskService.executeTask(taskId);
        return true;
    }

    @ApiOperation(value = "更新任务")
    @RequestMapping(value = "/task", method = RequestMethod.PUT)
    public Boolean updateTaskConfig(@RequestBody @Valid TaskConfigDto taskConfigDto) {
        taskService.updateTaskConfig(taskConfigDto);
        return true;
    }

    @ApiOperation(value = "删除任务")
    @RequestMapping(value = "/task/{task_id}", method = RequestMethod.DELETE)
    public Boolean addTaskConfig(@PathVariable(value = "task_id") String taskId) {
        taskService.delTaskConfig(taskId);
        return true;
    }

    @ApiOperation(value = "分页查询任务")
    @RequestMapping(value = "/task", method = RequestMethod.GET)
    public PageInfo<TaskConfigDto> addTaskConfig(
            @RequestParam(value = "page_num") Integer pageNum,
            @RequestParam(value = "page_size") Integer pageSize) {
        return taskService.getTaskConfig(pageNum, pageSize);
    }

}
