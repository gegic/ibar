package com.ktsnwt.project.team9.service.unit;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;


import javax.mail.MessagingException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.constants.MailConstants;
import com.ktsnwt.project.team9.services.implementations.MailService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class MailServiceUnitTest {

	@Autowired
	MailService mailService;
	
	@MockBean
	JavaMailSender emailSender;
	
	@Test
	public void testSendEmail_WithAllValues_ShouldSendMail() throws MessagingException {
		SimpleMailMessage message = new SimpleMailMessage(); 
		message.setFrom("noreply.kts.l9@gmail.com");
        message.setTo("aleksa.goljovic4@gmail.com"); 
        message.setSubject(MailConstants.SUBJECT); 
        message.setText(MailConstants.TEXT);
        doNothing().when(emailSender).send(message);
		mailService.sendMail(MailConstants.TO, MailConstants.SUBJECT, MailConstants.TEXT);
		
		verify(emailSender, timeout(1000).times(1)).send(Mockito.any(SimpleMailMessage.class));
	}
}
