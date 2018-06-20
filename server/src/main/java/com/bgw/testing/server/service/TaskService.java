package com.bgw.testing.server.service;

import com.bgw.testing.common.dto.*;
import com.bgw.testing.common.enums.ErrorCode;
import com.bgw.testing.common.enums.TaskRunMethod;
import com.bgw.testing.common.enums.TaskStatus;
import com.bgw.testing.dao.mapper.bgw_automation.TsTaskConfigMapper;
import com.bgw.testing.dao.mapper.bgw_automation.TsTaskPropertyMapper;
import com.bgw.testing.dao.pojo.bgw_automation.TsTaskConfig;
import com.bgw.testing.dao.pojo.bgw_automation.TsTaskProperty;
import com.bgw.testing.server.config.ServerException;
import com.bgw.testing.server.util.BaseStringUtils;
import com.bgw.testing.server.util.PageConvert;
import com.bgw.testing.server.util.SchedulerUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskService {

    @Autowired
    private TsTaskConfigMapper tsTaskConfigMapper;
    @Autowired
    private TsTaskPropertyMapper tsTaskPropertyMapper;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private VariableService variableService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private CaseService caseService;

    /**
     * 获取所有已启用的定时任务
     * @return
     */
    public List<TaskConfigDto> getEnableTask() {
        return tsTaskConfigMapper.selectAll().stream()
                .filter(tsTaskConfig -> tsTaskConfig.getRunMethod().equals(TaskRunMethod.TIMING.getKey())
                        && tsTaskConfig.getTaskStatus().equals(TaskStatus.ENABLED.getKey()))
                .map(this::convertToTaskConfigDto)
                .collect(Collectors.toList());
    }

    /**
     * 执行任务
     * @param taskId
     */
    @Async
    public void executeTask(String taskId) {
        log.info("定时任务开始执行：" + taskId);
        //获取任务信息
        TaskConfigDto taskConfigDto = getTaskConfig(taskId);
        //初始化环境
        variableService.initEnvironmentVariable(taskConfigDto.getEnvironmentId());
        //校验任务状态
        if (!taskConfigDto.getTaskStatus().equalsIgnoreCase(TaskStatus.ENABLED.getKey())) {
            throw new ServerException(ErrorCode.NON_ENABLED, "任务：" + taskConfigDto.getTaskName());
        }
        Map<String, List<CaseReportDto>> report = new HashMap<>();
        sortingByPriority(taskConfigDto.getTaskProperties()).forEach(taskProperties -> {
            taskProperties.parallelStream().forEach(taskProperty -> {
                //解析ids并获取对应用例
                List<CaseInfoDto> caseInfoDtos = new ArrayList<>();
                for (String caseId : taskProperty.getCaseIds()) {
                    CaseInfoDto caseInfoDto = caseService.getCaseInfo(caseId);
                    if (caseInfoDto == null) {
                        throw new ServerException(ErrorCode.NOT_EXIST, "测试用例：" + caseId);
                    } else {
                        caseInfoDtos.add(caseInfoDto);
                    }
                }
                //根据优先级顺序执行，相同优先级的并发执行
                List<CaseReportDto> groupResult = caseService.runCase(caseInfoDtos);
                report.put(taskProperty.getGroupId(), groupResult);
            });
        });
        //测试结果生成
        ReportInfoDto reportInfoDto = reportService.initReportInfo(report, taskId);
        reportService.addReportResult(reportInfoDto);
    }

    /**
     * 组排序
     * @param taskPropertyDtos
     * @return
     */
    public List<List<TaskPropertyDto>> sortingByPriority(List<TaskPropertyDto> taskPropertyDtos) {
        TreeMap<Integer, List<TaskPropertyDto>> treeMap = new TreeMap<>();
        taskPropertyDtos.forEach(taskProperty -> {
            if (!treeMap.containsKey(taskProperty.getPriority())) {
                treeMap.put(taskProperty.getPriority(), new ArrayList<>());
            }
            treeMap.get(taskProperty.getPriority()).add(taskProperty);
        });
        List<List<TaskPropertyDto>> result = new ArrayList<>();
        treeMap.keySet().forEach(key -> result.add(treeMap.get(key)));
        return result;
    }

    /**
     * 分页查询任务配置
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<TaskConfigDto> getTaskConfig(int pageNum, int pageSize) {
        Page<TaskConfigDto> page = PageHelper.startPage(pageNum, pageSize, "create_time desc");
        List<TaskConfigDto> tsTaskConfigs = tsTaskConfigMapper.selectAll()
                .stream()
                .map(this::convertToTaskConfigDto)
                .collect(Collectors.toList());
        return PageConvert.getPageInfo(page, tsTaskConfigs);
    }

    /**
     * 新增任务配置
     * @param taskConfigDto
     */
    @Transactional
    public void addTaskConfig(TaskConfigDto taskConfigDto) {
        if (taskConfigDto != null) {
            checkCron(taskConfigDto.getCron());
            taskConfigDto.setTaskId(BaseStringUtils.uuidSimple());
            taskConfigDto.setTaskStatus(TaskStatus.INIT.getKey());
            taskConfigDto.setCreateTime(new Date());
            taskConfigDto.setCreateBy("System");
            addOrUpdateTaskProperty(taskConfigDto.getTaskId(), taskConfigDto.getTaskProperties(), false);
            tsTaskConfigMapper.insertSelective(convertToTsTaskConfig(taskConfigDto));
        }
    }

    /**
     * 更新任务配置
     * @param taskConfigDto
     */
    @Transactional
    public void updateTaskConfig(TaskConfigDto taskConfigDto) {
        TaskConfigDto oldTask = getTaskConfig(taskConfigDto.getTaskId());
        if (oldTask != null) {
            checkCron(taskConfigDto.getCron());
            taskConfigDto.setUpdateTime(new Date());
            taskConfigDto.setUpdateBy("System");
            addOrUpdateTaskProperty(taskConfigDto.getTaskId(), taskConfigDto.getTaskProperties(), true);
            tsTaskConfigMapper.updateByPrimaryKeySelective(convertToTsTaskConfig(taskConfigDto));
            checkScheduledTask(oldTask, taskConfigDto);
        } else {
            throw new ServerException(ErrorCode.NOT_NULL, "任务：" + taskConfigDto.getTaskId());
        }
    }

    /**
     * 检测定时任务
     */
    private void checkScheduledTask(TaskConfigDto oldTask, TaskConfigDto newTask) {
        if (newTask.getRunMethod().equalsIgnoreCase(TaskRunMethod.TIMING.getKey())
                && newTask.getTaskStatus().equalsIgnoreCase(TaskStatus.ENABLED.getKey())) {
            SchedulerUtils.modifyScheduler(oldTask, newTask, applicationContext);
        }
    }

    /**
     * 删除任务配置
     * @param taskId
     */
    @Transactional
    public void delTaskConfig(String taskId) {
        if (StringUtils.isNotBlank(taskId)) {
            tsTaskPropertyMapper.deleteByTaskId(taskId);
            tsTaskConfigMapper.deleteByPrimaryKey(taskId);
        }
    }

    private void addOrUpdateTaskProperty(String taskId, List<TaskPropertyDto> taskPropertyDtos, boolean isUpdate) {
        if (isUpdate) {
            tsTaskPropertyMapper.deleteByTaskId(taskId);
        }
        if (taskPropertyDtos.size() > 0) {
            taskPropertyDtos.forEach(taskPropertyDto -> {
                TsTaskProperty tsTaskProperty = convertToTsTaskProperty(taskId, taskPropertyDto);
                tsTaskProperty.setId(BaseStringUtils.uuidSimple());
                tsTaskProperty.setCreateTime(new Date());
                tsTaskProperty.setCreateBy("System");
                tsTaskPropertyMapper.insertSelective(tsTaskProperty);
            });
        }
    }

    /**
     * 校验cron时间表达式格式
     * @param cron
     */
    private void checkCron(String cron) {
        if (!CronExpression.isValidExpression(cron)) {
            throw new ServerException(ErrorCode.FORMAT_ERROR, cron);
        }
    }

    /**
     * 根据任务id查询任务配置
     * @param taskId
     * @return
     */
    public TaskConfigDto getTaskConfig(String taskId){
        TsTaskConfig tsTaskConfig = tsTaskConfigMapper.selectByPrimaryKey(taskId);
        return convertToTaskConfigDto(tsTaskConfig);
    }

    private List<TaskPropertyDto> getTaskProperties(String taskId) {
        List<TsTaskProperty> tsTaskProperties = tsTaskPropertyMapper.selectByTaskId(taskId);
        if (tsTaskProperties != null && tsTaskProperties.size() > 0) {
            return tsTaskProperties
                    .stream()
                    .map(this::convertToTaskPropertyDto)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private TsTaskConfig convertToTsTaskConfig(TaskConfigDto taskConfigDto){
        TsTaskConfig tsTaskConfig = new TsTaskConfig();
        tsTaskConfig.setId(taskConfigDto.getTaskId());
        tsTaskConfig.setTaskName(taskConfigDto.getTaskName());
        tsTaskConfig.setEnvironmentId(taskConfigDto.getEnvironmentId());
        tsTaskConfig.setCron(taskConfigDto.getCron());
        tsTaskConfig.setRunMethod(taskConfigDto.getRunMethod());
        tsTaskConfig.setTaskStatus(taskConfigDto.getTaskStatus());
        tsTaskConfig.setCreateTime(taskConfigDto.getCreateTime());
        tsTaskConfig.setCreateBy(taskConfigDto.getCreateBy());
        tsTaskConfig.setUpdateTime(taskConfigDto.getUpdateTime());
        tsTaskConfig.setUpdateBy(taskConfigDto.getUpdateBy());
        return tsTaskConfig;
    }

    private TaskConfigDto convertToTaskConfigDto(TsTaskConfig tsTaskConfig){
        TaskConfigDto taskConfigDto = new TaskConfigDto();
        taskConfigDto.setTaskId(tsTaskConfig.getId());
        taskConfigDto.setTaskName(tsTaskConfig.getTaskName());
        taskConfigDto.setEnvironmentId(tsTaskConfig.getEnvironmentId());
        taskConfigDto.setCron(tsTaskConfig.getCron());
        taskConfigDto.setRunMethod(tsTaskConfig.getRunMethod());
        taskConfigDto.setTaskStatus(tsTaskConfig.getTaskStatus());
        taskConfigDto.setTaskProperties(getTaskProperties(tsTaskConfig.getId()));
        taskConfigDto.setCreateTime(tsTaskConfig.getCreateTime());
        taskConfigDto.setCreateBy(tsTaskConfig.getCreateBy());
        taskConfigDto.setUpdateTime(tsTaskConfig.getUpdateTime());
        taskConfigDto.setUpdateBy(tsTaskConfig.getUpdateBy());
        return taskConfigDto;
    }

    private TaskPropertyDto convertToTaskPropertyDto(TsTaskProperty tsTaskProperty) {
        TaskPropertyDto taskPropertyDto = new TaskPropertyDto();
        taskPropertyDto.setGroupId(tsTaskProperty.getGroupId());
        taskPropertyDto.setCaseIds(tsTaskProperty.getCaseId().split(","));
        taskPropertyDto.setPriority(tsTaskProperty.getPriority());
        return taskPropertyDto;
    }

    private TsTaskProperty convertToTsTaskProperty(String taskId, TaskPropertyDto taskPropertyDto) {
        TsTaskProperty tsTaskProperty = new TsTaskProperty();
        tsTaskProperty.setTaskId(taskId);
        tsTaskProperty.setGroupId(taskPropertyDto.getGroupId());
        StringBuilder ids = new StringBuilder();
        for (int i = 0; i< taskPropertyDto.getCaseIds().length; i++) {
            ids.append(taskPropertyDto.getCaseIds()[i]);
            if (i < taskPropertyDto.getCaseIds().length - 1) {
                ids.append(",");
            }
        }
        tsTaskProperty.setCaseId(ids.toString());
        tsTaskProperty.setPriority(taskPropertyDto.getPriority());
        return tsTaskProperty;
    }

}