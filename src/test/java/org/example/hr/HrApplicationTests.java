package org.example.hr;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest // 加载Spring Boot应用上下文进行测试
class HrApplicationTests {

	@Test
	void contextLoads() {
		// 这个测试方法用于验证Spring Boot应用上下文是否能成功加载
		// 如果应用配置有问题，这个测试会失败
		System.out.println("Spring Boot context loaded successfully for tests!");
	}

}