package com.ktsnwt.project.team9.constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ktsnwt.project.team9.model.Authority;
import com.ktsnwt.project.team9.model.CulturalOffer;

public class AdminConstants {

	public static final int PAGE = 0;
	public static final int NON_EXISTING_PAGE = 10;
	public static final int PAGE_SIZE = 5;
	
	public static final int NUMBER_OF_ITEMS = 6;
	
	public static final String PART = "%email%";
	public static final String NON_EXISTING_PART = "%jelena%";
	
	public static final Long ADMIN_ID = 1L;
	public static final Long NEW_ADMIN_ID = 8L;
	public static final Long ADMIN_CO_ID = 3L;
	public static final Long ADMIN_NO_CO_ID = 6L;
	public static final Long NON_EXISTING_ADMIN_ID = 33L;
	
	public static final String USERNAME = "admin 111";
	public static final String EMAIL = "email_adresa1@gmail.com";
	public static final String FIRSTNAME = "admin";
	public static final String LASTNAME = "admin";
	
	public static final String EXISTING_USERNAME = "admin 1";
	public static final String EXISTING_EMAIL = "email_adresa1@gmail.com";
	
	public static final String NEW_USERNAME = "jelenac11";
	public static final String NEW_EMAIL = "jelenacupac99@gmail.com";
	
	public static final List<Authority> AUTHORITIES = new ArrayList<Authority>(){
		private static final long serialVersionUID = 1L;
		{add(new Authority(1L,"ROLE_ADMIN"));}
	};
	public static final String ROLE = "ROLE_ADMIN";
	public static final String GPASSWORD = "asdghjk";
	public static final String EPASSWORD = "asdghjkafvsbgdnhf,bgghjukilofgthyjuik";
	public static final Set<CulturalOffer> CO = new HashSet<CulturalOffer>() {
		private static final long serialVersionUID = 1L;
		{
			add(new CulturalOffer(1L));
		}
	};
	
	public static final String PASSWORD = "sifra123";
	public static final String SEARCH_VALUE = "email";
	public static final String NON_EXISTING_SEARCH_VALUE = "jelena";
}
