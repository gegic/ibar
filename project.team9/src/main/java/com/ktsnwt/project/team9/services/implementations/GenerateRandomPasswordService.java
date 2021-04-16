package com.ktsnwt.project.team9.services.implementations;

import java.security.SecureRandom;

import org.springframework.stereotype.Service;

@Service
public class GenerateRandomPasswordService {

	public String generateRandomPassword() {
		final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		 
	    SecureRandom random = new SecureRandom();
	    StringBuilder sb = new StringBuilder();

	    for (int i = 0; i < 8; i++) {
	        int randomIndex = random.nextInt(chars.length());
	        sb.append(chars.charAt(randomIndex));
	    }

	    return sb.toString();
	}
	
}
