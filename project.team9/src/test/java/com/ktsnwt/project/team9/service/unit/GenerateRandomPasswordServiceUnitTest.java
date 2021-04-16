package com.ktsnwt.project.team9.service.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.services.implementations.GenerateRandomPasswordService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class GenerateRandomPasswordServiceUnitTest {

	@Autowired
	GenerateRandomPasswordService grpService;
	
	@Before
	public void setup() throws Exception {

	}
	
	@Test
	public void testGenerateRandomPassword_ShouldReturnRandomPasword() {
		String password = grpService.generateRandomPassword();
		
		assertEquals(8, password.length());
	}
	
}
