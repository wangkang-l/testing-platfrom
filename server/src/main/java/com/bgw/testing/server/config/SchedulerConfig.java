package com.bgw.testing.server.config;

import com.bgw.testing.common.dto.TaskConfigDto;
import com.bgw.testing.server.service.TaskService;
import com.bgw.testing.server.util.SchedulerUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
@ConditionalOnExpression("'${quartz.enabled}'=='true'")
public class SchedulerConfig implements Job {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private TaskService taskService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        taskService.executeTask(jobExecutionContext.getJobDetail().getKey().getGroup());
    }

    @Bean
    public StdSchedulerFactory stdSchedulerFactory() {
        StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
        //获取JobConfig集合
        List<TaskConfigDto> taskConfigDtos = taskService.getEnableTask();
        if (taskConfigDtos != null && taskConfigDtos.size() > 0) {
            taskConfigDtos.forEach(taskConfigDto -> SchedulerUtils.createScheduler(taskConfigDto, applicationContext));
        }
        return stdSchedulerFactory;
    }


}
