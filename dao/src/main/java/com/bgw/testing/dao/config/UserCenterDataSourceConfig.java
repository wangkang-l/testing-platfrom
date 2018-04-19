package com.bgw.testing.dao.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = UserCenterDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "usercenterSqlSessionFactory")
public class UserCenterDataSourceConfig {

    static final String PACKAGE ="com.bgw.testing.dao.usercenter.mapper";
    static final String MAPPER_LOCATION = "classpath:mapper/usercenter/*.xml";

    @Bean(name = "usercenterDataSource")
    @ConfigurationProperties(prefix = "datasource.usercenter")
    public DataSource usercenterDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "usercenterTransactionManager")
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(usercenterDataSource());
    }

    @Bean(name = "usercenterSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("usercenterDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
        return sessionFactoryBean.getObject();
    }

}
