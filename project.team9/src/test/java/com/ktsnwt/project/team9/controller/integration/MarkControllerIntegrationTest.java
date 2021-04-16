package com.ktsnwt.project.team9.controller.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.constants.MarkConstants;
import com.ktsnwt.project.team9.dto.MarkBookDTO;
import com.ktsnwt.project.team9.dto.UserLoginDTO;
import com.ktsnwt.project.team9.dto.response.UserTokenStateDTO;
import com.ktsnwt.project.team9.model.Mark;
import com.ktsnwt.project.team9.services.implementations.CulturalOfferService;
import com.ktsnwt.project.team9.services.implementations.MarkBookService;
import com.ktsnwt.project.team9.services.implementations.RegisteredUserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class MarkControllerIntegrationTest {

	@Autowired
	MarkBookService markService;
	
	@Autowired
	RegisteredUserService registeredUserService;
	
	@Autowired
	CulturalOfferService culturalOfferService;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	private String accessToken;
	
	public void login(String username, String password) {
		ResponseEntity<UserTokenStateDTO> responseEntity = restTemplate.postForEntity("/auth/login", new UserLoginDTO(username, password), UserTokenStateDTO.class); 
		accessToken = "Bearer " + responseEntity.getBody().getAccessToken(); 
	}
	
	@Test
	public void testGetMarkForCulturalOffer_WithLoginAsRegisteredUserAndExistingMark_ShouldReturnMark() {
		login(MarkConstants.EMAIL, MarkConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);

        ResponseEntity<MarkBookDTO> responseEntity = restTemplate.exchange("/api/marks/" + MarkConstants.CULTURAL_OFFER_ID, HttpMethod.GET, httpEntity, MarkBookDTO.class);

        MarkBookDTO mark = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MarkConstants.CULTURAL_OFFER_ID, mark.getCulturalOffer());
	}
	
	@Test
	public void testGetMarkForCulturalOffer_WithLoginAsRegisteredUserAndNonExistingMark_ShouldReturnNotFound() {
		login(MarkConstants.EMAIL, MarkConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);

        ResponseEntity<MarkBookDTO> responseEntity = restTemplate.exchange("/api/marks/" + MarkConstants.CULTURAL_OFFER_ID2, HttpMethod.GET, httpEntity, MarkBookDTO.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	public void testGetMarkForCulturalOffer_WithLoginAsAdminAndExistingMark_ShouldReturnBadRequest() {
		login(MarkConstants.EMAIL_ADMIN, MarkConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);

        ResponseEntity<MarkBookDTO> responseEntity = restTemplate.exchange("/api/marks/" + MarkConstants.CULTURAL_OFFER, HttpMethod.GET, httpEntity, MarkBookDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testCreateMark_WithLoginAsRegisteredUserAndAllCorrectValues_ShouldReturnMark() {
		login(MarkConstants.EMAIL, MarkConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);
        
        MarkBookDTO newMark = new MarkBookDTO(MarkConstants.VALUE, MarkConstants.CULTURAL_OFFER_ID);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(newMark, headers);

        ResponseEntity<MarkBookDTO> responseEntity = restTemplate.exchange("/api/marks/rate", HttpMethod.POST, httpEntity, MarkBookDTO.class);
        
        MarkBookDTO mark = responseEntity.getBody();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(MarkConstants.VALUE, mark.getValue());
	}
	
	@Test
	public void testCreateMark_WithLoginAsAdminAndAllCorrectValues_ShouldReturnForbidden() {
		login(MarkConstants.EMAIL_ADMIN, MarkConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);
        
        MarkBookDTO newMark = new MarkBookDTO(MarkConstants.VALUE, MarkConstants.CULTURAL_OFFER_ID);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(newMark, headers);

        ResponseEntity<MarkBookDTO> responseEntity = restTemplate.exchange("/api/marks/rate", HttpMethod.POST, httpEntity, MarkBookDTO.class);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
	}
	
	@Test
	public void testCreateMark_WithLoginAsRegusteredUserAndNonExistingCulturalOffer_ShouldReturnBadRequest() {
		login(MarkConstants.EMAIL, MarkConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);
        
        MarkBookDTO newMark = new MarkBookDTO(MarkConstants.VALUE, MarkConstants.NON_EXISTING_CULTURAL_OFFER_ID);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(newMark, headers);

        ResponseEntity<MarkBookDTO> responseEntity = restTemplate.exchange("/api/marks/rate", HttpMethod.POST, httpEntity, MarkBookDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
	
	@Test
	public void testCreateMark_WithLoginAsRegusteredUserAndInvalidValue_ShouldReturnBadRequest() {
		login(MarkConstants.EMAIL, MarkConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);
        
        MarkBookDTO newMark = new MarkBookDTO(MarkConstants.VALUE_INVALID, MarkConstants.CULTURAL_OFFER_ID);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(newMark, headers);

        ResponseEntity<MarkBookDTO> responseEntity = restTemplate.exchange("/api/marks/rate", HttpMethod.POST, httpEntity, MarkBookDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testUpdateMark_WithLoginAsRegisteredUserAndAllCorrectValues_ShouldReturnMark() {
		login(MarkConstants.EMAIL, MarkConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);
        
        Mark forUpdate = markService.findByUserIdAndCulturalOfferId(MarkConstants.USER_ID, MarkConstants.CULTURAL_OFFER_ID);
        MarkBookDTO updated = new MarkBookDTO(forUpdate.getId(), MarkConstants.VALUE_UPDATED, MarkConstants.CULTURAL_OFFER_ID);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(updated, headers);

        ResponseEntity<MarkBookDTO> responseEntity = restTemplate.exchange("/api/marks", HttpMethod.PUT, httpEntity, MarkBookDTO.class);
        
        MarkBookDTO mark = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MarkConstants.VALUE_UPDATED, mark.getValue());
	}
	
	@Test
	public void testUpdateMark_WithLoginAsAdminAndAllCorrectValues_ShouldReturnForbidden() {
		login(MarkConstants.EMAIL_ADMIN, MarkConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);
        
        Mark forUpdate = markService.findByUserIdAndCulturalOfferId(MarkConstants.USER_ID, MarkConstants.CULTURAL_OFFER_ID);
        MarkBookDTO updated = new MarkBookDTO(forUpdate.getId(), MarkConstants.VALUE_UPDATED, MarkConstants.CULTURAL_OFFER_ID);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(updated, headers);

        ResponseEntity<MarkBookDTO> responseEntity = restTemplate.exchange("/api/marks", HttpMethod.PUT, httpEntity, MarkBookDTO.class);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
	}
	
	@Test
	public void testUpdateMark_WithLoginAsRegisteredUserAndInvalidValue_ShouldReturnBadRequest() {
		login(MarkConstants.EMAIL, MarkConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);
        
        Mark forUpdate = markService.findByUserIdAndCulturalOfferId(MarkConstants.USER_ID, MarkConstants.CULTURAL_OFFER_ID);
        MarkBookDTO updated = new MarkBookDTO(forUpdate.getId(), MarkConstants.VALUE_INVALID, MarkConstants.CULTURAL_OFFER_ID);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(updated, headers);

        ResponseEntity<MarkBookDTO> responseEntity = restTemplate.exchange("/api/marks", HttpMethod.PUT, httpEntity, MarkBookDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
	
}
