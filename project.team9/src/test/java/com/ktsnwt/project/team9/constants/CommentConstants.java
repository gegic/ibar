package com.ktsnwt.project.team9.constants;

import com.ktsnwt.project.team9.model.CulturalOffer;
import com.ktsnwt.project.team9.model.Image;
import com.ktsnwt.project.team9.model.RegisteredUser;

public class CommentConstants {

	public static final int PAGE = 0;
	public static final int PAGE_SIZE = 5;
	
	public static final String PATH = "src/test/resources/uploadedImages/";
	
	public static final Long CULTURAL_OFFER_ID = 1L;
	public static final Long CULTURAL_OFFER_ID2 = 2L;
	public static final Long NON_EXISTING_CULTURAL_OFFER_ID = 20L;
	
	public static final Long CULTURAL_OFFER_USER_ID = 4L;
	public static final Long CULTURAL_OFFER_USER_ID_NO_COMMENTS_FOR_APPROVING = 1L;
	public static final Long NON_EXISTING_CULTURAL_OFFER_USER_ID = 11L;
	
	public static final boolean APPROVED = true;
	public static final boolean DECLINED = false;
	
	public static final Long COMMENT_ID = 1L;
	public static final Long COMMENT_ID2 = 2L;
	public static final Long COMMENT_ID3 = 3L;
	public static final Long COMMENT_ID4 = 4L;
	public static final Long NON_EXISTING_COMMENT_ID = 11L;
	
	public static final int NUMBER_OF_ITEMS = 7;
	
	public static final RegisteredUser REG_USER = new RegisteredUser(11L);
	public static final Long USER_ID = 11L;
	public static final CulturalOffer CULTURAL_OFFER = new CulturalOffer(1L);
	public static final CulturalOffer CULTURAL_OFFER2 = new CulturalOffer(2L);
	public static final CulturalOffer NON_EXISTING_CULTURAL_OFFER = new CulturalOffer(NON_EXISTING_CULTURAL_OFFER_ID);
	public static final String TEXT = "tekst komentara";
	public static final long TIME = 1608560339402L;
	
	public static final String IMAGEURL = "src/test/resources/uploadedImages/slika2.jpg";
	public static final Image IMAGE = new Image(IMAGEURL);
	public static final String IMAGE_PATH = PATH + "comment" + USER_ID + TIME + "_slika2.jpg";
	public static final Image CREATED_IMG = new Image(IMAGE_PATH);

	public static final int NON_EXISTING_PAGE = 100;
	public static final Long CO_ID_NO_APPROVED_COMMENTS = 7L;
	public static final Long ADMIN_ID_WITH_NOT_APPROVED_COMMENTS = 3L;
	public static final Long COMMENT_ID_NO_IMG = 7L;
	public static final Long COMMENT_ID_TO_APPROVE = 7L;
	public static final Long COMMENT_ID_TO_DECLINE = 7L;
	public static final Long COMMENT_ID_TO_DECLINE_WITH_IMAGE = 6L;
	public static final Long CREATE_NO_CO_ID = 100L;
	
	public static final String USER_EMAIL = "email_adresa20@gmail.com";
	public static final String PASSWORD = "sifra123";
	public static final String ADMIN_EMAIL = "email_adresa1@gmail.com";
	public static final String ADMIN_EMAIL_WITH_NOT_APPROVED_COMMENTS = "email_adresa3@gmail.com";
	public static final Long NON_EXISTING_CO = 30L;
	public static final String IMAGE_PATH_TO_CREATE_COMMENT = "src/test/resources/uploadedImages/comment_slika6.jpg";
	public static final Long LOGGED_IN_USER_ID = 7L;
	public static final String NAME_OF_CULTURAL_OFFER_ID = "Manastir 1";
	public static final Long APPROVED_COMMENT_ID = 1L;
	public static final String ADMIN_EMAIL_THAT_SHOULD_UPDATE_COMMENT = "email_adresa4@gmail.com";
	public static final Long ADMIN_THAT_SHOULD_UPDATE_COMMENT_ID = 4L;
	public static final String ADMIN_EMAIL_THAT_SHOULD_UPDATE_COMMENT_WITH_IMG = "email_adresa3@gmail.com";
}

