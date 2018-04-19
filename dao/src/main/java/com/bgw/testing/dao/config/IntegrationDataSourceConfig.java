package com.bgw.testing.dao.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = IntegrationDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "integrationSqlSessionFactory")
public class IntegrationDataSourceConfig {

    static final String PACKAGE ="com.bgw.testing.dao.integration.mapper";
    static final String MAPPER_LOCATION = "classpath:mapper/integration/*.xml";

    @Bean(name = "integrationDataSource")
    @ConfigurationProperties(prefix = "datasource.integration")
    @Primary
    public DataSource integrationDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "integrationTransactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(integrationDataSource());
    }

    @Bean(name = "integrationSqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("integrationDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
        return sessionFactoryBean.getObject();
    }

}
