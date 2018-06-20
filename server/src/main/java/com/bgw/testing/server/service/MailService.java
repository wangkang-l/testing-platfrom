package com.bgw.testing.server.service;

import com.bgw.testing.common.dto.ReportInfoDto;
import com.bgw.testing.common.dto.ReportTemplateDto;
import com.bgw.testing.common.dto.TaskConfigDto;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailService {
    private static String[] receivers = {"fushuangshuang@dafy.com"};
    private String templateName = "ReportTemplate.ftl";
    private String mailTitle = "测试报告";
    @Value("${spring.mail.username}")
    private String sender;
    @Value("${spring.freemarker.template-loader-path}")
    private String templatePath;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private ReportService reportService;
    @Autowired
    private TaskService taskService;

    /**
     * 发送邮件
     * @param batchNo
     */
    public void sendMailByBatchNo(String batchNo) throws MessagingException, IOException, TemplateException {
        ReportTemplateDto reportTemplateDto= getTemplateData(batchNo);
        setMailMessage(reportTemplateDto);
    }

    /**
     * 设置邮件内容
     * @param reportTemplateDto 模板
     */
    private void setMailMessage(ReportTemplateDto reportTemplateDto) throws MessagingException, IOException, TemplateException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);
        helper.setFrom(sender);
        helper.setTo(receivers);
        helper.setSubject(mailTitle);
        Map<String, Object> model = new HashMap<>();
        model.put("task_name",reportTemplateDto.getTaskName());
        model.put("batch_no", reportTemplateDto.getBatchNo());
        model.put("start_time", reportTemplateDto.getStartTime());
        model.put("end_time",reportTemplateDto.getEndTime());
        model.put("case_num", reportTemplateDto.getCaseNum());
        model.put("pass_num", reportTemplateDto.getPassNum());
        model.put("fail_num", reportTemplateDto.getFailNum());
        model.put("pass_rate", reportTemplateDto.getPassRate());

        FileTemplateLoader fileTemplateLoader = new FileTemplateLoader(new File(System.getProperty("user.dir")+templatePath));
        freeMarkerConfigurer.getConfiguration().setTemplateLoader(fileTemplateLoader);

        Template template = freeMarkerConfigurer.getConfiguration().getTemplate(templateName);
        String content  = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        helper.setText(content,true);
        mailSender.send(message);
    }

     /**
     * 获取模板数据
     * @param batchNo
     */
    private ReportTemplateDto getTemplateData(String batchNo) {
        ReportInfoDto reportInfoDto = reportService.getReportResultByBatchNo(batchNo);
        TaskConfigDto taskConfigDto = taskService.getTaskConfig(reportInfoDto.getTaskId());

        ReportTemplateDto reportTemplateDto= new ReportTemplateDto();
        reportTemplateDto.setTaskName(taskConfigDto.getTaskName());
        reportTemplateDto.setBatchNo(reportInfoDto.getBatchNo());
        reportTemplateDto.setStartTime(reportInfoDto.getStartTime());
        reportTemplateDto.setEndTime(reportInfoDto.getEndTime());
        reportTemplateDto.setCaseNum(reportInfoDto.getTotalNum());
        reportTemplateDto.setPassNum(reportInfoDto.getSuccNum());
        reportTemplateDto.setFailNum(reportInfoDto.getFailNum());
        reportTemplateDto.setPassRate(reportInfoDto.getPassRate());
        return reportTemplateDto;
    }
}
