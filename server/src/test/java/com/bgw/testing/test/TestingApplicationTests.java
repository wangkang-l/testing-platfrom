package com.bgw.testing.test;

import com.bgw.testing.server.TestingApplication;
import com.bgw.testing.server.service.IntegrationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestingApplication.class)
public class TestingApplicationTests {

	@Test
	public void contextLoads() {
		System.out.println(1==1);
//		System.out.println(integrationService.getUrl());
	}

}
