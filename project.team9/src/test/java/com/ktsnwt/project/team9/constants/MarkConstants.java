package com.ktsnwt.project.team9.constants;

import com.ktsnwt.project.team9.model.CulturalOffer;
import com.ktsnwt.project.team9.model.RegisteredUser;

public class MarkConstants {
	
	public static final Long MARK_ID = 2L;
	public static final Long NON_EXISTING_MARK_ID = 26L;
	
	public static final int NUMBER_OF_ITEMS = 13;
	
	public static final Long GRADER_ID = 7L;
	public static final Long GRADER_ID2 = 2L;
	public static final Long NON_EXISTING_GRADER_ID = 66L;
	public static final RegisteredUser GRADER = new RegisteredUser(GRADER_ID);
	public static final RegisteredUser GRADER2 = new RegisteredUser(GRADER_ID2);
	public static final RegisteredUser NON_EXISTING_GRADER = new RegisteredUser(NON_EXISTING_GRADER_ID);
	
	public static final Long CULTURAL_OFFER_ID = 1L;
    public static final Long NON_EXISTING_CULTURAL_OFFER_ID = 26L;
    
    public static final CulturalOffer CULTURAL_OFFER = new CulturalOffer(CULTURAL_OFFER_ID);
    public static final CulturalOffer NON_EXISTING_CULTURAL_OFFER = new CulturalOffer(NON_EXISTING_CULTURAL_OFFER_ID);
    
    public static final int VALUE = 2;
    public static final int VALUE_UPDATED = 4;
    
    public static final String EMAIL = "email_adresa20@gmail.com";
    public static final String PASSWORD = "sifra123";
    
    public static final Long CULTURAL_OFFER_ID2 = 3L;
    public static final Long USER_ID = 7L;
    public static final Long USER_ID2 = 8L;
    
    public static final String EMAIL_ADMIN = "email_adresa1@gmail.com";
    
    public static final int VALUE_INVALID = 10;

}
