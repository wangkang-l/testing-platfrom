package com.bgw.testing.server.service;

import com.bgw.testing.common.AppConst;
import com.bgw.testing.common.dto.MySqlInfoDto;
import com.bgw.testing.common.dto.StepResultDto;
import com.bgw.testing.server.util.BaseJsonUtils;
import com.bgw.testing.server.util.BaseStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MysqlStepService implements ApplicationContextAware {

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public StepResultDto executeMysqlRequest(MySqlInfoDto mySqlInfoDto) {
        String clsName = AppConst.DEFAULT_DAO_CLASS_PATH;

        //获取dao bean
        Object dao;
        try {
            dao = context.getBean(Class.forName(clsName));
        } catch (Throwable e) {
            return new StepResultDto(false, "无法获取" + clsName + "对应的dao对象", e);
        }

        //调用bean相应方法并返回执行结果
        try {
            Object target = getObjectFromCglibProxy(dao);
            Field field = target.getClass().getDeclaredField("template" + BaseStringUtils.initialCapital(mySqlInfoDto.getDbName()));
            field.setAccessible(true);

            log.info("sql:" + mySqlInfoDto.getSql());
            log.info("params:" + mySqlInfoDto.getParams());

            if (mySqlInfoDto.isQuery()) {
                if (mySqlInfoDto.isSingleResult()) {
                    Map<String, Object> dbRow = ((JdbcTemplate) field.get(target)).queryForMap(mySqlInfoDto.getSql(),
                            mySqlInfoDto.getParams().size() == 0 ? null : mySqlInfoDto.getParams().toArray());
                    return new StepResultDto(BaseJsonUtils.writeValue(dbRow));
                } else {
                    List<Map<String, Object>> dbRows = ((JdbcTemplate) field.get(target)).queryForList(mySqlInfoDto.getSql(),
                            mySqlInfoDto.getParams().size() == 0 ? null : mySqlInfoDto.getParams().toArray());
                    return new StepResultDto(BaseJsonUtils.writeValue(dbRows));
                }
            } else {
                ((JdbcTemplate) field.get(target)).update(mySqlInfoDto.getSql(),
                        mySqlInfoDto.getParams().size() == 0 ? null : mySqlInfoDto.getParams().toArray());
                return new StepResultDto("");
            }
        } catch (Throwable e) {
            return new StepResultDto(false, "执行dao方法异常:" + mySqlInfoDto, e);
        }
    }

    private static Object getObjectFromCglibProxy(Object proxy) {
        try {
            Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
            h.setAccessible(true);
            Object dynamicAdvisedInterceptor = h.get(proxy);
            Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
            advised.setAccessible(true);
            return ((AdvisedSupport)advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
        } catch (Throwable var4) {
            return null;
        }
    }
}
