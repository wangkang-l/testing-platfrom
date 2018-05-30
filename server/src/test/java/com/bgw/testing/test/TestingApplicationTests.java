package com.bgw.testing.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestingApplicationTests.class)
public class TestingApplicationTests {

//	@Autowired
//	@Qualifier(value = "redisTemplate11")
//	private RedisTemplate<String, Object> redisTemplate11;

	@Test
	public void test() {
//		ValueOperations<String, Object> operations = redisTemplate11.opsForValue();
//		operations.set("group_asdaxad", "hahahaha");
		System.out.println("ss");
	}

}
