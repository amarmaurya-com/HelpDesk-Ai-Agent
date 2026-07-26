package com.codes.Help_desk_backend;

import com.codes.Help_desk_backend.service.AiServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HelpDeskBackendApplicationTests {

	@Autowired
	private AiServiceImpl aiService;

	@Test
	void contextLoads() {
		System.out.println(aiService.getResponseFromAssistant("hii", "123@test.com"));
	}

}
