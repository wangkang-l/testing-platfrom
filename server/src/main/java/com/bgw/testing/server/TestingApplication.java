package com.bgw.testing.server;

import com.bgw.testing.server.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;

@EnableAsync
@EnableScheduling
@ComponentScan("com.bgw.testing")
@EnableTransactionManagement
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class TestingApplication {

	@Autowired
	private DataCacheService dataCacheService;
	@Autowired
	private CaseService caseService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private VariableService variableService;


	public static void main(String[] args) {
		SpringApplication.run(TestingApplication.class, args);
	}

	@PostConstruct
	public void initServer() {
		dataCacheService.clearCaseInfo();
		caseService.initAllCaseInfo();
		groupService.initGroupInfo();
		variableService.initVariable();
	}

}
