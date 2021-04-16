package com.ktsnwt.project.team9.service.unit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import com.ktsnwt.project.team9.constants.CulturalOfferConstants;
import com.ktsnwt.project.team9.constants.RegisteredUserConstants;
import com.ktsnwt.project.team9.helper.implementations.NewsMapper;
import com.ktsnwt.project.team9.model.CulturalOffer;
import com.ktsnwt.project.team9.model.RegisteredUser;
import com.ktsnwt.project.team9.services.implementations.CulturalOfferService;
import com.ktsnwt.project.team9.services.implementations.NewsService;
import com.ktsnwt.project.team9.services.implementations.RegisteredUserService;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.util.HashSet;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class NewsServiceUnitTest {

	@Autowired
	NewsService newsService;
	
	@MockBean
	CulturalOfferService coService;
	
	@MockBean
	RegisteredUserService userService;
	
	@Autowired
	NewsMapper newsMapper;

	@Before
	public void setup() throws Exception {
	
		CulturalOffer culturalOffer1 = new CulturalOffer(CulturalOfferConstants.NAME1,
				CulturalOfferConstants.DESCRIPTION1, CulturalOfferConstants.GEOLOCATION1,
				CulturalOfferConstants.CATEGORY1, CulturalOfferConstants.ADMIN1);
		culturalOffer1.setId(CulturalOfferConstants.ID1);
		culturalOffer1.setActive(true);
		culturalOffer1.setAverageMark(0);
		culturalOffer1.setComments(new HashSet<>());
		culturalOffer1.setMarks(new HashSet<>());
		culturalOffer1.setNews(new HashSet<>());
		culturalOffer1.setSubscribedUsers(new HashSet<>());

		CulturalOffer culturalOffer2 = new CulturalOffer(CulturalOfferConstants.NAME1,
				CulturalOfferConstants.DESCRIPTION2, CulturalOfferConstants.GEOLOCATION2,
				CulturalOfferConstants.CATEGORY3, CulturalOfferConstants.ADMIN1);
		culturalOffer2.setId(CulturalOfferConstants.ID2);
		culturalOffer2.setActive(true);
		culturalOffer2.setAverageMark(0);
		culturalOffer2.setComments(new HashSet<>());
		culturalOffer2.setMarks(new HashSet<>());
		culturalOffer2.setNews(new HashSet<>());
		culturalOffer2.setSubscribedUsers(new HashSet<>());
		
		
		RegisteredUser user1 = new RegisteredUser(RegisteredUserConstants.USERNAME, RegisteredUserConstants.EMAIL, RegisteredUserConstants.PASSWORD, RegisteredUserConstants.FIRSTNAME, RegisteredUserConstants.LASTNAME);
		user1.setId(RegisteredUserConstants.USER_ID);
		user1.setSubscribed(new HashSet<>());
		RegisteredUser user2 = new RegisteredUser(RegisteredUserConstants.EXISTING_USERNAME, RegisteredUserConstants.NEW_EMAIL, RegisteredUserConstants.PASSWORD, RegisteredUserConstants.FIRSTNAME, RegisteredUserConstants.LASTNAME);
		user2.setId(RegisteredUserConstants.USER_ID + 1);
		user2.setSubscribed(new HashSet<>());
		RegisteredUser user3 = new RegisteredUser(RegisteredUserConstants.NEW_USERNAME, RegisteredUserConstants.EXISTING_EMAIL, RegisteredUserConstants.PASSWORD, RegisteredUserConstants.FIRSTNAME, RegisteredUserConstants.LASTNAME);
		user3.setId(RegisteredUserConstants.USER_ID + 2);
		user3.setSubscribed(new HashSet<>());
		
		user1.getSubscribed().add(culturalOffer2);
		user2.getSubscribed().add(culturalOffer1);
		user2.getSubscribed().add(culturalOffer2);
		user3.getSubscribed().add(culturalOffer1);
		
		culturalOffer1.getSubscribedUsers().add(user2);
		culturalOffer1.getSubscribedUsers().add(user3);
		culturalOffer2.getSubscribedUsers().add(user1);
		culturalOffer2.getSubscribedUsers().add(user2);
		
		
		given(coService.getById(CulturalOfferConstants.ID1)).willReturn(culturalOffer1);
		given(coService.getById(CulturalOfferConstants.ID2)).willReturn(culturalOffer2);
		given(coService.getById(CulturalOfferConstants.ID1 + 2)).willReturn(null);
		

		given(userService.getById(RegisteredUserConstants.USER_ID)).willReturn(user1);
		given(userService.getById(RegisteredUserConstants.USER_ID + 1)).willReturn(user2);
		given(userService.getById(RegisteredUserConstants.USER_ID + 2)).willReturn(user3);
		given(userService.getById(RegisteredUserConstants.USER_ID + 3)).willReturn(null);
	}

	
	@Test
	public void testSubscribe_WithValidParametars_ShouldReturnTrue() throws Exception {
		Boolean response = newsService.subscribe(RegisteredUserConstants.USER_ID, CulturalOfferConstants.ID1);
		verify(userService, times(1)).getById(RegisteredUserConstants.USER_ID);
		verify(coService, times(1)).getById(CulturalOfferConstants.ID1);
		assertTrue(response);
		
	}
	
	@Test
	public void testSubscribe_WhenAlreadySubscribed_ShouldReturnFalse() throws Exception {
		Boolean response = newsService.subscribe(RegisteredUserConstants.USER_ID + 1, CulturalOfferConstants.ID1);
		verify(userService, times(1)).getById(RegisteredUserConstants.USER_ID + 1);
		verify(coService, times(1)).getById(CulturalOfferConstants.ID1);
		assertFalse(response);
		
	}
	
	@Test(expected = Exception.class)
	public void testSubscribe_WithNonExistingUserID_ShouldThrowException() throws Exception {
		newsService.subscribe(RegisteredUserConstants.USER_ID + 3, CulturalOfferConstants.ID1);
		verify(userService, times(1)).getById(RegisteredUserConstants.USER_ID + 3);
	}
	
	
	@Test
	public void testUnsubscribe_WithValidParametars_ShouldReturnTrue() throws Exception {
		Boolean response = newsService.unsubscribe(RegisteredUserConstants.USER_ID+1, CulturalOfferConstants.ID1);
		verify(userService, times(1)).getById(RegisteredUserConstants.USER_ID + 1);
		verify(coService, times(1)).getById(CulturalOfferConstants.ID1);
		assertTrue(response);
	}
	
	@Test
	public void testUnsubscribe_WithInvalidParametars_ShouldReturnFalse() throws Exception {
		Boolean response = newsService.unsubscribe(RegisteredUserConstants.USER_ID, CulturalOfferConstants.ID1);
		verify(userService, times(1)).getById(RegisteredUserConstants.USER_ID);
		verify(coService, times(1)).getById(CulturalOfferConstants.ID1);
		assertFalse(response);
	}
	
	@Test(expected = Exception.class)
	public void testUnsubscribe_WithNonExistingUserID_ShouldThrowException() throws Exception {
		newsService.unsubscribe(RegisteredUserConstants.USER_ID + 3, CulturalOfferConstants.ID1);
		verify(userService, times(1)).getById(RegisteredUserConstants.USER_ID + 3);
	}
	
	@Test
	public void testGetSubscribedNews_WithValidParams_ShouldReturn3News() {
		
	}
	
	
}
