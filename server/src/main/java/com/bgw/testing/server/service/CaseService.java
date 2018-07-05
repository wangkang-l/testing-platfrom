package com.bgw.testing.server.service;

import com.bgw.testing.common.dto.*;
import com.bgw.testing.common.enums.CaseStatus;
import com.bgw.testing.common.enums.ErrorCode;
import com.bgw.testing.common.enums.StepType;
import com.bgw.testing.dao.mapper.bgw_automation.TsCaseInfoMapper;
import com.bgw.testing.dao.pojo.bgw_automation.TsCaseInfo;
import com.bgw.testing.server.GroupContext;
import com.bgw.testing.server.VariableContext;
import com.bgw.testing.server.config.ServerException;
import com.bgw.testing.server.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CaseService {

    @Autowired
    private TsCaseInfoMapper tsCaseInfoMapper;
    @Autowired
    private StepService stepService;
    @Autowired
    private MysqlStepService mysqlStepService;
    @Autowired
    private RedisStepService redisStepService;
    @Autowired
    private HttpStepService httpStepService;
    @Autowired
    private DataCacheService dataCacheService;
    @Autowired
    private ReportService reportService;

    private static Pattern PATTERN_METHOD = Pattern.compile("([^(]*)(?:\\((.*)\\))?");

    /**
     * 指定组执行用例
     * @param groupId
     * @return
     */
    @Async
    public ReportInfoDto runCase(String groupId) {
        Map<String, List<CaseReportDto>> reportCaseResultDtos = new HashMap<>();
        List<GroupInfoDto> groupInfoDtos = getAllChildGroup(groupId);
        groupInfoDtos.forEach(groupInfoDto -> {
            reportCaseResultDtos.put(groupInfoDto.getGroupId(), runCase(getCaseInfoByGroupId(groupInfoDto.getGroupId())));
        });
        return reportService.initReportInfo(reportCaseResultDtos, "");
    }


    /**
     * 执行指定的用例内容(按照优先级大小顺序执行)
     * @param caseInfoDtos
     * @return
     */
    public List<CaseReportDto> runCase(List<CaseInfoDto> caseInfoDtos) {
        List<CaseReportDto> caseReportDtos = new ArrayList<>();
        groupByPriority(caseInfoDtos).forEach(caseInfoDtos1 -> {
            caseInfoDtos1.parallelStream().forEach(caseInfoDto -> {
                BaseMDCUtils.put(BaseStringUtils.uuidSimple());
                CaseReportDto caseReportDto = runCase(caseInfoDto, false);
                caseReportDto.setTraceLogId(BaseMDCUtils.get());
                BaseMDCUtils.clear();
                caseReportDtos.add(caseReportDto);
            });
        });
        return caseReportDtos;
    }

    /**
     * 根据优先级对指定用例进行分组
     * @param caseInfoDtos
     * @return
     */
    private List<List<CaseInfoDto>> groupByPriority(List<CaseInfoDto> caseInfoDtos) {
        TreeMap<Integer, List<CaseInfoDto>> treeMap = new TreeMap();
        caseInfoDtos.forEach(caseInfoDto -> {
            Integer priority = caseInfoDto.getPriority();
            if (!treeMap.containsKey(priority)) {
                treeMap.put(priority, new ArrayList<>());
            }
            treeMap.get(priority).add(caseInfoDto);
        });
        List<List<CaseInfoDto>> result = new ArrayList<>();
        treeMap.entrySet().forEach(entry -> result.add(entry.getValue()));
        return result;
    }

    /**
     * 执行case
     * @param caseInfoDto
     * @return
     */
    private CaseReportDto runCase(CaseInfoDto caseInfoDto, boolean fromStep) {

        VariableContext context = VariableContext.getInstance();
        Map<String, String> caseVariable = context.getTemporaryVariable(caseInfoDto.getCaseId());
        //占位符替换
        caseInfoDto.setCaseName(ContextUtils.fillValue(caseInfoDto.getCaseName(), caseVariable, null));
        caseInfoDto.setPrecondition(ContextUtils.fillValue(caseInfoDto.getPrecondition(), caseVariable, null));
        caseInfoDto.setDescription(ContextUtils.fillValue(caseInfoDto.getDescription(), caseVariable, null));
        //初始化用例临时变量
        context.initTemporaryVariable(caseInfoDto.getCaseId(), ContextUtils.fillValue(caseInfoDto.getInitTemporaryVariables(), caseVariable, null));

        //用例执行开始前报告赋值
        CaseReportDto caseReportDto = new CaseReportDto(caseInfoDto.getCaseId(), caseInfoDto.getCaseName());

        //执行用例步骤
        if (caseInfoDto.getSteps().size() > 0) {

            if (!fromStep) {
                log.info(
                        "=================测试用例开始[{}/{}]: {}",
                        caseInfoDto.getGroupId(),
                        caseInfoDto.getCaseId(),
                        caseInfoDto.getDescription()
                );
            }
            //根据步骤优先级排序
            List<StepDto> stepDtos = caseInfoDto.getSteps();
            Collections.sort(stepDtos, new MultiFieldSorting(new String[]{"priority"}, new String[]{"asc"}));

            //运行步骤
            stepDtos.forEach(stepDto -> {
                if (stepDto.getStepType().equals(StepType.TEMPLATE.type)) {
                    CaseReportDto stepCaseResultDto = runCase(getInheritCaseInfo(stepDto.getTemplateId()), true);
                    caseReportDto.getStepReportDtoList().addAll(stepCaseResultDto.getStepReportDtoList());
                } else {
                    caseReportDto.getStepReportDtoList().add(runStep(stepDto));
                }
            });

        }

        //清空用例变量
        caseVariable.clear();

        //用例执行结束后报告赋值
        caseReportDto.setEndTime(new Date());
        if (caseReportDto.getStepReportDtoList()
                .stream()
                .filter(stepReportDto -> !caseReportDto.getResult())
                .findAny()
                .orElse(null) != null) {
            caseReportDto.setResult(false);
        }

        if (!fromStep) {
            log.info("=================测试用例{}[{}/{}]: {}\n",
                    caseReportDto.getResult() ? "成功" : "失败",
                    caseInfoDto.getGroupId(),
                    caseInfoDto.getCaseId(),
                    caseInfoDto.getDescription()
            );
        }

        return caseReportDto;
    }

    /**
     * 执行step
     * @param stepDto
     * @return
     */
    private StepReportDto runStep(StepDto stepDto) {
        //变量获取
        VariableContext context = VariableContext.getInstance();
        //步骤变量初始化
        context.initTemporaryVariable(stepDto.getStepId(),
                ContextUtils.fillValue(
                        stepDto.getInitTemporaryVariables(),
                        context.getTemporaryVariable(stepDto.getCaseId()),
                        context.getTemporaryVariable(stepDto.getStepId())
                )
        );

        Map<String, String> caseVariable = context.getTemporaryVariable(stepDto.getCaseId());
        Map<String, String> stepVariable = context.getTemporaryVariable(stepDto.getStepId());

        //占位符替换
        stepDto = BaseJsonUtils.readValue(ContextUtils.fillValue(BaseJsonUtils.writeValue(stepDto), caseVariable, stepVariable), StepDto.class);

        //前置条件校验
        if (!ConditionUtils.verify(stepDto.getPrecondition())) {
            log.info("步骤[{}]条件[{}]不满足, 无需执行.", stepDto.getDescription(), stepDto.getPrecondition());
            return new StepReportDto(stepDto.getDescription(), false, String.format("条件[%s]不满足", stepDto.getPrecondition()));
        }

        //步骤报告内容赋值
        StepReportDto stepReportDto = new StepReportDto();
        stepReportDto.setStartTime(new Date());
        stepReportDto.setStepContent(BaseJsonUtils.writeValue(stepDto));
        stepReportDto.setDescription(stepDto.getDescription());
        stepReportDto.setStepType(stepDto.getStepType());

        //执行步骤
        String data = "";
        if (stepDto.getStepType().equals(StepType.HTTP.type)) { //http请求
            StepResultDto stepResultDto = httpStepService.executeHttpRequest(stepDto.getHttpRequest());
            stepReportDto.setRequestContent(BaseJsonUtils.writeValue(stepDto.getHttpRequest()));
            if (stepResultDto.getException() != null) {
                this.stepFail(stepReportDto, stepResultDto.getData(), stepResultDto.getException());
            } else {
                //校验api状态码
                if (stepDto.getHttpRequest().getExpectedStatusCode() != null) {
                    if (stepDto.getHttpRequest().getExpectedStatusCode() != stepResultDto.getStatusCode()) {
                        String msg = String.format(
                                "返回的状态码%s和期望值%s不匹配.响应内容%s",
                                stepResultDto.getStatusCode(),
                                stepDto.getHttpRequest().getExpectedStatusCode(),
                                stepResultDto.getData()
                        );
                        this.stepFail(stepReportDto, msg);
                    }
                }
                data = stepResultDto.getData();
            }
        } else if (stepDto.getStepType().equals(StepType.FUNC.type)) { //func
            stepReportDto.setRequestContent(stepDto.getFunc());
            Matcher matcher = PATTERN_METHOD.matcher(stepDto.getFunc());
            if (matcher.matches()) {
                String methodName = matcher.group(1).trim();
                Object[] finalArgs = new Object[0];
                if (matcher.group(2) != null) {
                    String[] args = matcher.group(2).split("(?<!\\\\),", -1);
                    finalArgs = new Object[args.length];
                    for (int i = 0; i < args.length; i++) {
                        finalArgs[i] = args[i].replaceAll("\\\\,", ",");
                    }
                }
                Object funcValue = FuncUtils.getDynamicFuncValue(methodName, finalArgs, caseVariable, stepVariable);
                if (funcValue != null) {
                    data = funcValue.toString();
                }
            }
        } else { //redis或mysql
            StepResultDto stepResultDto;
            if (stepDto.getStepType().equals(StepType.REDIS.type)) {
                stepReportDto.setRequestContent(BaseJsonUtils.writeValue(stepDto.getRedisInfo()));
                stepResultDto = redisStepService.executeRedisRequest(stepDto.getRedisInfo());
            } else {
                stepReportDto.setRequestContent(BaseJsonUtils.writeValue(stepDto.getMysqlInfo()));
                stepResultDto = mysqlStepService.executeMysqlRequest(stepDto.getMysqlInfo());
            }

            if (stepResultDto.isSuccess()) {
                data = stepResultDto.getData();
            } else {
                this.stepFail(stepReportDto, stepResultDto.getData(), stepResultDto.getException());
            }
        }

        //清空步骤变量
        stepVariable.clear();
        //步骤执行结束时间赋值
        stepReportDto.setEndTime(new Date());

        //如执行成功，则将响应结果保存到用例变量"step.content"中
        if (data != null) {
            stepReportDto.setResponseContent(data);
            caseVariable.put("step.content", data);
        }

        //解析step结果
        if (stepReportDto.getResult() && stepDto.getExtractor() != null) {
            try {
                ExtractorUtils.extract(data, stepDto.getExtractor(), caseVariable, stepVariable);
            } catch (Throwable e) {
                String msg = String.format("使用%s解析结果%s时异常", stepDto.getExtractor(), data);
                this.stepFail(stepReportDto, msg, e);
            }
        }

        //处理context
        if (stepReportDto.getResult()) {
            if (StringUtils.isNotBlank(data) && StringUtils.isNotBlank(stepDto.getElement())) {
                Object str = BaseJsonUtils.valueFromJsonKey(data, stepDto.getElement());
                data = str != null ? str.toString() : "";
            }
            if (StringUtils.isNotBlank(stepDto.getKeyInGlobalVariable())) {
                String key = stepDto.getKeyInGlobalVariable();
                context.getGlobalVariable().put(key, data);
            }
            if (StringUtils.isNotBlank(stepDto.getKeyInEnvironmentVariable())) {
                String key = stepDto.getKeyInEnvironmentVariable();
                context.getEnvironmentVariable().put(key, data);
            }
            if (StringUtils.isNotBlank(stepDto.getKeyInTemporaryVariable())) {
                String key = stepDto.getKeyInTemporaryVariable();
                caseVariable.put(key, data);
            }
            log.info("步骤[{}]成功", stepReportDto.getDescription());
        }

        //使用verifier对step进行校验
        if (stepReportDto.getResult() && stepDto.getVerifiers() != null && stepDto.getVerifiers().size() > 0) {
            try {
                stepDto.getVerifiers().forEach(verifier -> {
                    if (!ConditionUtils.verify(verifier.getCondition())) {
                        String msg = StringUtils.isBlank(verifier.getMsg()) ? "未满足条件:" + verifier.getCondition() : verifier.getMsg();
                        this.stepFail(stepReportDto, msg);
                    }
                });
            } catch (Throwable e) {
                this.stepFail(stepReportDto, "执行verify时异常", e);
            }
        }

        return stepReportDto;
    }

    private void stepFail(StepReportDto stepReportDto, String msg) {
        stepFail(stepReportDto, msg, null);
    }

    private void stepFail(StepReportDto stepReportDto, String msg, Throwable e) {
        stepReportDto.setResult(false);
        stepReportDto.setErrorInfo(e != null ? ExceptionUtils.getRootCauseMessage(e): msg);
        stepReportDto.setEndTime(new Date());
        log.error("步骤[{}]失败[{}]{}\n", stepReportDto.getDescription(), msg, e != null ? ExceptionUtils.getStackTrace(e) : "");
    }

    /**
     * 获取当前组下所有用例(包括子组)并排序
     * @param groupId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<CaseInfoDto> getCaseInfo(String groupId, int pageNum, int pageSize, String var4) {

        List<String> groupIds = GroupContext.getInstance().getAllChildGroupId(groupId);
        List<CaseInfoDto> caseInfoDtos = new ArrayList<>();
        groupIds.parallelStream().forEach(tempGroupId -> {
            caseInfoDtos.addAll(getCaseInfoByGroupId(tempGroupId));
        });

        List<CaseInfoDto> finalCaseInfo = caseInfoDtos;
        finalCaseInfo.parallelStream().forEach(caseInfoDto -> {
            String path = GroupContext.getInstance().getGroupInfo(caseInfoDto.getGroupId()).getPath();
            caseInfoDto.setCasePath(path);
        });

        if (StringUtils.isNotBlank(var4)) {
            finalCaseInfo = finalCaseInfo.stream()
                    .filter(caseInfoDto -> caseInfoDto.getCaseName().contains(var4)
                            || caseInfoDto.getCasePath().contains(var4))
                    .collect(Collectors.toList());
        }

        return new PageInfo<>(sortingByPriorityAndUpdateTime(finalCaseInfo), pageNum, pageSize);
    }

    /**
     * 获取所有子组并根据path与priority排序
     * @param groupId
     * @return
     */
    private List<GroupInfoDto> getAllChildGroup(String groupId) {
        GroupContext context = GroupContext.getInstance();
        List<String> groupIds = context.getAllChildGroupId(groupId);
        List<GroupInfoDto> groupInfoDtos = context.getGroupInfoList()
                .stream()
                .filter(groupInfoDto -> groupIds.contains(groupInfoDto.getGroupId()))
                .collect(Collectors.toList());

        String[] fields = new String[]{"path", "priority"};
        String[] orders = new String[]{"asc", "asc"};
        Collections.sort(groupInfoDtos, new MultiFieldSorting(fields, orders));
        return groupInfoDtos;
    }

    /**
     * 初始化所有用例信息
     * @return
     */
    public void initAllCaseInfo() {
        List<TsCaseInfo> tsCaseInfos = tsCaseInfoMapper.selectAll();
        List<CaseInfoDto> caseInfoDtos = tsCaseInfos.parallelStream()
                .map(this::convertToCaseInfoDto)
                .collect(Collectors.toList());
        if (caseInfoDtos != null && caseInfoDtos.size() > 0) {
            caseInfoDtos.forEach(caseInfoDto -> {
                dataCacheService.addOrUpdateCaseInfo(caseInfoDto);
            });
        }
    }

    /**
     * 获取当前组下的用例
     * @param groupId
     * @return
     */
    public List<CaseInfoDto> getCaseInfoByGroupId(String groupId) {
        return dataCacheService.getAllCaseListByGroupId(groupId);
    }

    /**
     * 根据用例ID获取用例信息
     * @param caseId
     * @return
     */
    public CaseInfoDto getCaseInfo(String caseId) {
        return dataCacheService.getCaseInfo(caseId);
    }

    /**
     * 根据用例优先级与更新时间排序
     * @param caseInfoDtos
     * @return
     */
    private List<CaseInfoDto> sortingByPriorityAndUpdateTime(List<CaseInfoDto> caseInfoDtos) {
        String[] fields = new String[]{"casePath", "priority", "updateTime"};
        String[] orders = new String[]{"asc", "asc", "desc"};
        Collections.sort(caseInfoDtos, new MultiFieldSorting(fields, orders));
        return caseInfoDtos;
    }

    /**
     * 新增用例
     * @param caseInfoDto
     */
    @Transactional
    public void addCaseInfo(CaseInfoDto caseInfoDto) {
        caseInfoDto.setCaseId(BaseStringUtils.uuidSimple());
        caseInfoDto.setStatus(CaseStatus.NORMAL.status);
        caseInfoDto.setCreateTime(new Date());
        caseInfoDto.setCreateBy("System");
        caseInfoDto.setUpdateTime(new Date());
        caseInfoDto.setUpdateBy("System");
        checkCaseName(caseInfoDto);
        caseInfoDto.getSteps().forEach(stepDto -> {
            stepDto.setCaseId(caseInfoDto.getCaseId());
            stepService.addStepInfo(stepDto);
        });
        tsCaseInfoMapper.insertSelective(convertToTsCaseInfo(caseInfoDto));
        dataCacheService.addOrUpdateCaseInfo(caseInfoDto);
    }

    /**
     * 更新用例
     * @param caseInfoDto
     */
    @Transactional
    public void updateCaseInfo(CaseInfoDto caseInfoDto) {
        if (StringUtils.isNotBlank(caseInfoDto.getCaseId()) && dataCacheService.getCaseInfo(caseInfoDto.getCaseId()) != null) {
            checkCaseName(caseInfoDto);
            caseInfoDto.setUpdateTime(new Date());
            caseInfoDto.setUpdateBy("System");
            caseInfoDto.getSteps().forEach(stepDto -> stepService.updateStepInfo(stepDto));
            tsCaseInfoMapper.updateByPrimaryKeySelective(convertToTsCaseInfo(caseInfoDto));
            dataCacheService.addOrUpdateCaseInfo(caseInfoDto);
        } else {
            throw new ServerException("4000", "用例ID为空或用例不存在");
        }
    }

    /**
     * 移动测试用例
     * @param caseIds
     * @param newGroupId
     */
    public void moveCaseInfo(List<String> caseIds, String newGroupId) {
        if (caseIds == null || caseIds.size() == 0) {
            throw new ServerException(ErrorCode.NOT_NULL, "用例ID");
        }

        List<CaseInfoDto> data = dataCacheService.getAllCaseList()
                .parallelStream()
                .filter(caseInfoDto -> caseIds.contains(caseInfoDto.getCaseId()))
                .collect(Collectors.toList());

        data.parallelStream().forEach(caseInfoDto -> {
            dataCacheService.delCaseInfo(caseInfoDto.getGroupId(), caseInfoDto.getCaseId());
            caseInfoDto.setGroupId(newGroupId);
            caseInfoDto.setUpdateTime(new Date());
            caseInfoDto.setUpdateBy("System");
            tsCaseInfoMapper.updateByPrimaryKeySelective(convertToTsCaseInfo(caseInfoDto));
            dataCacheService.addOrUpdateCaseInfo(caseInfoDto);
        });
    }

    /**
     * 删除用例
     * @param caseId
     */
    @Transactional
    public void deleteCaseInfo(String groupId, String caseId) {
        stepService.delStepInfoByCaseId(caseId);
        tsCaseInfoMapper.deleteByPrimaryKey(caseId);
        dataCacheService.delCaseInfo(groupId, caseId);
    }

    /**
     * 根据Id获取可继承用例内容
     * @param caseId
     * @return
     */
    private CaseInfoDto getInheritCaseInfo(String caseId) {
        return dataCacheService.getInheritCaseInfo(caseId);
    }

    /**
     * 检查当前组内是否存在相同名称的用例
     * @param caseInfoDto
     */
    private void checkCaseName(CaseInfoDto caseInfoDto) {
        List<CaseInfoDto> caseInfoDtos = getCaseInfoByGroupId(caseInfoDto.getGroupId());
        long count = caseInfoDtos.stream()
                .filter(temp -> !temp.getCaseId().equals(caseInfoDto.getCaseId())
                        && temp.getCaseName().equals(caseInfoDto.getCaseName().trim()))
                .count();
        if (count > 0) {
            throw new ServerException(ErrorCode.ALREADY_EXISTS, "用例名称：" + caseInfoDto.getCaseName());
        }
    }

    private CaseInfoDto convertToCaseInfoDto(TsCaseInfo tsCaseInfo) {
        CaseInfoDto caseInfoDto = new CaseInfoDto();
        caseInfoDto.setCaseId(tsCaseInfo.getId());
        caseInfoDto.setCaseName(tsCaseInfo.getCaseName());
        caseInfoDto.setDescription(tsCaseInfo.getDescription());
        caseInfoDto.setGroupId(tsCaseInfo.getGroupId());
        caseInfoDto.setPriority(tsCaseInfo.getPriority() == null ? 0 : tsCaseInfo.getPriority());
        caseInfoDto.setPrecondition(tsCaseInfo.getPrecondition());
        caseInfoDto.setInitTemporaryVariables(BaseJsonUtils.readValue(tsCaseInfo.getInitTemporaryVariables(), Map.class));
        caseInfoDto.setStatus(tsCaseInfo.getStatus());
        caseInfoDto.setInherit(tsCaseInfo.getInherit());
        caseInfoDto.setSteps(stepService.getStepInfo(tsCaseInfo.getId()));
        caseInfoDto.setCreateTime(tsCaseInfo.getCreateTime());
        caseInfoDto.setCreateBy(tsCaseInfo.getCreateBy());
        caseInfoDto.setUpdateTime(tsCaseInfo.getUpdateTime());
        caseInfoDto.setUpdateBy(tsCaseInfo.getUpdateBy());
        return caseInfoDto;
    }

    private TsCaseInfo convertToTsCaseInfo(CaseInfoDto caseInfoDto) {
        TsCaseInfo tsCaseInfo = new TsCaseInfo();
        tsCaseInfo.setId(caseInfoDto.getCaseId());
        tsCaseInfo.setCaseName(caseInfoDto.getCaseName());
        tsCaseInfo.setDescription(caseInfoDto.getDescription());
        tsCaseInfo.setGroupId(caseInfoDto.getGroupId());
        tsCaseInfo.setPrecondition(caseInfoDto.getPrecondition());
        tsCaseInfo.setInitTemporaryVariables(BaseJsonUtils.writeValue(caseInfoDto.getInitTemporaryVariables()));
        tsCaseInfo.setPriority(caseInfoDto.getPriority() == null ? 0 : caseInfoDto.getPriority());
        tsCaseInfo.setInherit(caseInfoDto.isInherit());
        tsCaseInfo.setStatus(caseInfoDto.getStatus());
        tsCaseInfo.setCreateTime(caseInfoDto.getCreateTime());
        tsCaseInfo.setCreateBy(caseInfoDto.getCreateBy());
        tsCaseInfo.setUpdateTime(caseInfoDto.getUpdateTime());
        tsCaseInfo.setUpdateBy(caseInfoDto.getUpdateBy());
        return tsCaseInfo;
    }

}