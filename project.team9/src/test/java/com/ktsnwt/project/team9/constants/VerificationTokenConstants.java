package com.ktsnwt.project.team9.constants;

import java.util.HashSet;

import com.ktsnwt.project.team9.model.Comment;
import com.ktsnwt.project.team9.model.CulturalOffer;
import com.ktsnwt.project.team9.model.Mark;
import com.ktsnwt.project.team9.model.RegisteredUser;

public class VerificationTokenConstants {
	
	public static final String TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzcHJpbmctc2VjdXJpdHktZXhhbXBsZSIsInN1YiI6ImVtYWlsQGdtYWlsLmNvbSIsImF1ZCI6IndlYiIsImlhdCI6MTYwNzU2MTQzMywiZXhwIjoxNjA3NTYzMjMzfQ.hnAdWpJFNsBprJwlRKFqVYf4sn3Te5ry7lllLVBp1fq3jzjWOLwXDpgQV5MDVErJM9MGsj4fBK5a-XZARrnHsg";
	public static final String NON_EXISTING_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzcHJpbmctc2VjdXJpdHktZXhhbXBsZSIsInN1YiI6ImVtYWlsQGdtYWlsLmNvbSIsImF";
	public static final String NEW_TOKEN = "khdfjheyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzcHJpbmctc2VjdXJpdHktZXhhbXBsZSIsInN1YiI6ImVtYWlsQGdtYWlsLmNvbSIsImF";
	
	public static final Long ID = 1L;
	
	public static final String EMAIL = "email_adresa21@gmail.com";
	public static final String NON_EXISTING_EMAIL = "email_adresa66@gmail.com";
	public static final Long USER_ID = 9L;
	public static final String USERNAME = "user 21";
	public static final String PASSWORD = "password";
	public static final String FIRST_NAME = "user21";
	public static final String LAST_NAME = "user21";
	
	public static final RegisteredUser USER = new RegisteredUser(USER_ID, USERNAME, EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, new HashSet<Mark>(), new HashSet<Comment>(), new HashSet<CulturalOffer>(), true);
	public static final RegisteredUser NON_EXISTING_USER = new RegisteredUser(USER_ID, USERNAME, NON_EXISTING_EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, new HashSet<Mark>(), new HashSet<Comment>(), new HashSet<CulturalOffer>(), true);

}
