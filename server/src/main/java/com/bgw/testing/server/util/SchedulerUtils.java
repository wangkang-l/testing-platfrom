package com.bgw.testing.server.util;

import com.bgw.testing.common.dto.TaskConfigDto;
import com.bgw.testing.server.config.AutoWiringSpringBeanJobFactory;
import org.quartz.*;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.text.ParseException;

public class SchedulerUtils {

    //CronTrigger的工厂类
    private static CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
    //JobDetail的工厂类
    private static JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
    //自动注入Spring Bean的工厂类
    private static AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
    //定时任务Scheduler的工厂类，Spring Framework提供
    private static SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

    static {
        //加载指定路径的配置
        schedulerFactoryBean.setConfigLocation(new ClassPathResource("quartz.properties"));
    }
    /**
     * 创建定时任务，根据参数，创建对应的定时任务，并使之生效
     * @param config
     * @param context
     * @return
     */
    public static boolean createScheduler(TaskConfigDto config, ApplicationContext context) {
        try {
            //创建新的定时任务
            return create(config, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除旧的定时任务，创建新的定时任务
     * @param oldTaskInfo
     * @param newTaskInfo
     * @param context
     * @return
     */
    public static Boolean modifyScheduler(TaskConfigDto oldTaskInfo, TaskConfigDto newTaskInfo, ApplicationContext context) {
        if (oldTaskInfo == null || newTaskInfo == null || context == null) {
            return false;
        }
        //1、清除旧的定时任务
        delete(oldTaskInfo.getTaskName(), oldTaskInfo.getTaskId());
        //2、创建新的定时任务
        return create(newTaskInfo, context);
    }

    /**
     * 提取的删除任务的方法
     * @param oldName
     * @param oldGroup
     * @return
     * @throws SchedulerException
     */
    public static Boolean delete(String oldName, String oldGroup) {
        TriggerKey key = TriggerKey.triggerKey(oldName, oldGroup);
        Scheduler oldScheduler = null;
        try {
            oldScheduler = schedulerFactoryBean.getScheduler();
            //根据TriggerKey获取trigger是否存在，如果存在则根据key进行删除操作
            Trigger keyTrigger = oldScheduler.getTrigger(key);
            if (keyTrigger != null) {
                oldScheduler.unscheduleJob(key);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return true;
    }
    /**
     * 提取出的创建定时任务的方法
     * @param taskConfigDto
     * @param context
     * @return
     */
    private static Boolean create(TaskConfigDto taskConfigDto, ApplicationContext context) {
        try {
            //创建新的定时任务
            String name = taskConfigDto.getTaskName();
            String group = taskConfigDto.getTaskId();
            String description = taskConfigDto.getTaskName();
            Class clazz = Class.forName("com.xb.server.integration.config.MyJob");
            String time = taskConfigDto.getCron();
            JobDetail jobDetail = createJobDetail(clazz, name, group, description);
            if (jobDetail == null) {
                return false;
            }
            Trigger trigger = createCronTrigger(jobDetail, time, name, group, description);
            if (trigger == null) {
                return false;
            }
            jobFactory.setApplicationContext(context);
            schedulerFactoryBean.setJobFactory(jobFactory);
            schedulerFactoryBean.setJobDetails(jobDetail);
            schedulerFactoryBean.setTriggers(trigger);
            schedulerFactoryBean.afterPropertiesSet();

            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            if (!scheduler.isStarted()) {
                scheduler.start();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据指定的参数，创建JobDetail
     * @param clazz
     * @param name
     * @param group
     * @param description
     * @return
     */
    public static JobDetail createJobDetail(Class clazz, String name, String group, String description) {
        jobDetailFactory.setJobClass(clazz);
        jobDetailFactory.setName(name);
        jobDetailFactory.setGroup(group);
        jobDetailFactory.setDescription(description);
        jobDetailFactory.setDurability(true);
        jobDetailFactory.afterPropertiesSet();
        return jobDetailFactory.getObject();
    }

    /**
     * 根据参数，创建对应的CronTrigger对象
     * @param job
     * @param time
     * @param name
     * @param group
     * @param description
     * @return
     */
    public static CronTrigger createCronTrigger(JobDetail job, String time, String name, String group, String description) {
        factoryBean.setName(name);
        factoryBean.setJobDetail(job);
        factoryBean.setCronExpression(time);
        factoryBean.setDescription(description);
        factoryBean.setGroup(group);
        try {
            factoryBean.afterPropertiesSet();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return factoryBean.getObject();
    }

}
