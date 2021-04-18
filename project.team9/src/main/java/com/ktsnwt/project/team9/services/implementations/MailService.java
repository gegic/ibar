package com.ktsnwt.project.team9.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {

	@Autowired
	private JavaMailSender emailSender;

	@Async
	public void sendMail(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();

		message.setFrom("noreply.kts.l9@gmail.com");
		message.setTo("milan_marinkovic98@hotmail.com");
		message.setSubject(subject);
		message.setText(text);

		emailSender.send(message);
	}
}
