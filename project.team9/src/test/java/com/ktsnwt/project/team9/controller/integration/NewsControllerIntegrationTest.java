package com.ktsnwt.project.team9.controller.integration;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.dto.NewsDTO;
import com.ktsnwt.project.team9.dto.UserLoginDTO;
import com.ktsnwt.project.team9.dto.response.UserTokenStateDTO;
import com.ktsnwt.project.team9.helper.implementations.CustomPageImplementation;
import com.ktsnwt.project.team9.model.News;
import com.ktsnwt.project.team9.services.implementations.NewsService;
import javassist.NotFoundException;

import static com.ktsnwt.project.team9.constants.NewsConstants.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class NewsControllerIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private NewsService newsService;


//    @Value("${trust.store.password}")
//    private String keyStorePassword;
//	@Value("${trust.store}")
//    private Resource keyStore;
//	
//	RestTemplate restTemplate() throws Exception {
//	    javax.net.ssl.SSLContext sslContext = new SSLContextBuilder()
//	      .loadTrustMaterial(keyStore.getURL(), keyStorePassword.toCharArray())
//	      .build();
//	    SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
//	    HttpClient httpClient = HttpClients.custom()
//	      .setSSLSocketFactory(socketFactory)
//	      .build();
//	    HttpComponentsClientHttpRequestFactory factory = 
//	      new HttpComponentsClientHttpRequestFactory(httpClient);
//	    return new RestTemplate(factory);
//	}
//	
//	
//	
//	public NewsControllerIntegrationTest() throws Exception {
//		restTemplate = restTemplate();
//	}
//	
	
	  private String accessToken;
	  
	  public void login(String username, String password) {
		  ResponseEntity<UserTokenStateDTO> responseEntity =
		  restTemplate.postForEntity("/auth/login", new UserLoginDTO(username,
		  password), UserTokenStateDTO.class); accessToken = "Bearer " +
		  responseEntity.getBody().getAccessToken(); 
	  }
	 
	
	@Test
	public void testGetAllNews_ShouldReturnAllNews() {
		int size = ((List<News>) newsService.getAll()).size();

		ResponseEntity<NewsDTO[]> responseEntity = restTemplate.getForEntity("/api/news",
				NewsDTO[].class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(size, responseEntity.getBody().length);
	}

	@Test
	public void testGetAllNews_WithPageable_ShouldReturnFirst5News() {
		Pageable pageable = PageRequest.of(PAGE_NO, PAGE_SIZE);
		int size = newsService.findAll(pageable).getNumberOfElements();
		ParameterizedTypeReference<CustomPageImplementation<NewsDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<NewsDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<NewsDTO>> responseEntity = restTemplate
				.exchange("/api/news/by-page?page=" + PAGE_NO + "&size=" + PAGE_SIZE, HttpMethod.GET, null, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(PAGE_NO ,responseEntity.getBody().getNumber());
		//Postavljen je size iz service-a jer ne znamo velicinu stranice koje ce se vratiti
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}

	@Test
	public void testGetAllNews_WithNonExistingPageable_ShouldReturnEmptyCollection() {
		Pageable pageable = PageRequest.of(NON_EXISTING_PAGE_NO, PAGE_SIZE);
		int size = newsService.findAll(pageable).getNumberOfElements();
		
		ParameterizedTypeReference<CustomPageImplementation<NewsDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<NewsDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<NewsDTO>> responseEntity = restTemplate
				.exchange("/api/news/by-page?page=" + NON_EXISTING_PAGE_NO + "&size=" + PAGE_SIZE, HttpMethod.GET, null, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}

	@Test
	public void testGetNews_WithExistingId_ShouldReturnNewsDTO() throws NotFoundException {
		ResponseEntity<NewsDTO> responseEntity = restTemplate.getForEntity("/api/news/1",
				NewsDTO.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(FIRST_NEWS_DATE, responseEntity.getBody().getDate());
	}

	@Test
	public void testGetNews_WithNonExistingId_ShouldReturnNotFound() {
		ResponseEntity<NewsDTO> responseEntity = restTemplate.getForEntity("/api/news/10000",
				NewsDTO.class);

		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	@Test
	public void testCreateNews_WithValidParameters_ShouldReturnCreatedNews() throws Exception {
		int beforeSize = ((List<News>) newsService.getAll()).size();

		login("email_adresa1@gmail.com", "sifra123");
        HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		
		ResponseEntity<NewsDTO> responseEntity = restTemplate.postForEntity("/api/news",
				new HttpEntity<>(NEWS_FOR_CREATE, headers),
				NewsDTO.class);

		
		NewsDTO news = responseEntity.getBody();

		List<News> newNews = (List<News>) newsService.getAll();
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertNotNull(news);
		assertEquals(NEWS_FOR_CREATE.getTitle(), newNews.get(newNews.size()-1).getTitle());
		assertEquals(NEWS_FOR_CREATE.getContent(), newNews.get(newNews.size()-1).getContent());
		assertEquals(beforeSize + 1, newNews.size());
	}

	@Test
	public void testCreateNews_WithEmptyContent_ShouldReturnBadRequest() throws NotFoundException {
		
		login("email_adresa1@gmail.com", "sifra123");
        HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		
		ResponseEntity<NewsDTO> responseEntity = restTemplate.postForEntity("/api/news",
				new HttpEntity<>(new NewsDTO(), headers),
				NewsDTO.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}

	@Test
	public void testCreateNews_WithNegativeDate_ShouldReturnBadRequest() throws NotFoundException {
		
		login("email_adresa1@gmail.com", "sifra123");
        HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		
		ResponseEntity<NewsDTO> responseEntity = restTemplate.postForEntity("/api/news",
				new HttpEntity<>(new NewsDTO(null,"ASDF",-1223L, "Title 1",false, null, 1L), headers),
				NewsDTO.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}

	@Test
	public void testCreateNews_WithNotExistingCulturalOfferID_ShouldReturnBadRequest() throws NotFoundException {
		
		login("email_adresa1@gmail.com", "sifra123");
        HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		
		ResponseEntity<NewsDTO> responseEntity = restTemplate.postForEntity("/api/news",
				new HttpEntity<>(new NewsDTO(null,"ASDF",1223L, "Title 1",false, null, NON_EXISTING_CULTURAL_OFFER_ID), headers),
				NewsDTO.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}

	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testUpdateNews_WithValidParameters_ShouldReturnUpdatedNews() {
		
		login("email_adresa1@gmail.com", "sifra123");
        HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		
		ResponseEntity<NewsDTO> responseEntity =
                restTemplate.exchange("/api/news/"+ NEWS_ID, HttpMethod.PUT,
                        new HttpEntity<NewsDTO>(NEWS_FOR_UPDATE, headers),
                        NewsDTO.class);
		
		NewsDTO news = responseEntity.getBody();
		
		News baseNews =newsService.getById(NEWS_ID);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(news);
		//Proveri da li je promenio kontekst
		assertEquals(NEWS_ID, news.getID());

		assertEquals(NEWS_FOR_UPDATE.getTitle(), news.getTitle());
		assertEquals(NEWS_FOR_UPDATE.getContent(), news.getContent());
		//Proveri da li je promenio kontekst u bazi
		assertEquals(NEWS_ID, baseNews.getId());
		assertEquals(NEWS_FOR_UPDATE.getContent(), baseNews.getContent());
		assertEquals(NEWS_FOR_UPDATE.getTitle(), baseNews.getTitle());
		
		
	}
	
	@Test
	public void testUpdateNews_WithEmptyContent_ShouldReturnBadRequest() {
		
		login("email_adresa1@gmail.com", "sifra123");
        HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		
		ResponseEntity<NewsDTO> responseEntity =
                restTemplate.exchange("/api/news/"+ NEWS_ID, HttpMethod.PUT,
                        new HttpEntity<NewsDTO>(new NewsDTO(), headers),
                        NewsDTO.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
	
	@Test
	public void testUpdateNews_WithNonExistingId_ShouldReturnBadRequest() {
		
		login("email_adresa1@gmail.com", "sifra123");
        HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		
		ResponseEntity<NewsDTO> responseEntity =
                restTemplate.exchange("/api/news/"+ NON_EXIST_NEWS_ID, HttpMethod.PUT,
                        new HttpEntity<NewsDTO>(new NewsDTO(), headers),
                        NewsDTO.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
	
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testDeleteNews_WithExistingId_ShouldReturnTrue() throws NotFoundException {
		
		login("email_adresa1@gmail.com", "sifra123");
        HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		
		int length = ((List<News>) newsService.getAll()).size();

		ResponseEntity<String> responseEntity = restTemplate.exchange(
				"/api/news/" + NEWS_ID, HttpMethod.DELETE,
				new HttpEntity<Object>(null, headers), String.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertTrue(Boolean.parseBoolean(responseEntity.getBody()));
		assertEquals(length - 1, ((List<News>) newsService.getAll()).size());
	}

	@Test
	public void testDeleteNews_WithNonExistingId_ShouldReturnNotFound() throws NotFoundException {
		
		login("email_adresa1@gmail.com", "sifra123");
        HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		
		ResponseEntity<String> responseEntity = restTemplate.exchange("/api/news/" + NON_EXIST_NEWS_ID, HttpMethod.DELETE,
				new HttpEntity<Object>(null, headers), String.class);

		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	
	@Test
	public void testSubscribe_WithExistingId_ShouldReturnTrue() {
		
		login("email_adresa20@gmail.com", "sifra123");
        HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		
		
		ResponseEntity<String> responseEntity = restTemplate.exchange("/api/news/subscribe/" +
					EXISTING_REGISTERED_USER_ID + "/" + CULTURAL_OFFER_ID_NOT_SUBSCRIBED
				, HttpMethod.PUT,
				new HttpEntity<Object>(null, headers), String.class);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertTrue(Boolean.parseBoolean(responseEntity.getBody()));
	}
	
	@Test
	public void testSubscribe_AllreadySubscribed_ShouldReturnFalse() {
		
		login("email_adresa20@gmail.com", "sifra123");
        HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		
		
		ResponseEntity<String> responseEntity = restTemplate.exchange("/api/news/subscribe/" +
					EXISTING_REGISTERED_USER_ID + "/" + EXISTING_CULTURAL_OFFER_ID
				, HttpMethod.PUT,
				new HttpEntity<Object>(null, headers), String.class);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertFalse(Boolean.parseBoolean(responseEntity.getBody()));
	}
	
	@Test
	public void testSubscribe_WithNotExistingUserId_ShouldReturnNotFound() {
		
		login("email_adresa20@gmail.com", "sifra123");
        HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		
		ResponseEntity<String> responseEntity = restTemplate.exchange("/api/news/subscribe/" + NON_EXISTING_REGISTERED_USER_ID + "/" + EXISTING_CULTURAL_OFFER_ID
				, HttpMethod.PUT,
				new HttpEntity<Object>(null, headers), String.class);
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	public void testSubscribe_WithNotExistingCOId_ShouldReturnNotFound() {
		
		login("email_adresa20@gmail.com", "sifra123");
        HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		
		ResponseEntity<String> responseEntity = restTemplate.exchange("/api/news/subscribe/" + EXISTING_REGISTERED_USER_ID + "/" + NON_EXISTING_CULTURAL_OFFER_ID
				, HttpMethod.PUT,
				new HttpEntity<Object>(null, headers), String.class);
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	public void testUnsubscribe_WithExistingId_ShouldReturnTrue() {
		
		login("email_adresa20@gmail.com", "sifra123");
        HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		
		ResponseEntity<String> responseEntity = restTemplate.exchange("/api/news/unsubscribe/" + EXISTING_REGISTERED_USER_ID + "/" + EXISTING_CULTURAL_OFFER_ID
				, HttpMethod.PUT,
				new HttpEntity<Object>(null, headers), String.class);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertTrue(Boolean.parseBoolean(responseEntity.getBody()));
	}
	
	@Test
	public void testUnsubscribe_WithNotExistingUserId_ShouldReturnNotFound() {
		
		login("email_adresa20@gmail.com", "sifra123");
        HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		
		ResponseEntity<String> responseEntity = restTemplate.exchange("/api/news/unsubscribe/" + NON_EXISTING_REGISTERED_USER_ID + "/" + EXISTING_CULTURAL_OFFER_ID
				, HttpMethod.PUT,
				new HttpEntity<Object>(null, headers), String.class);
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	public void testUnsubscribe_WithNotExistingCOId_ShouldReturnNotFound() {
		
		login("email_adresa20@gmail.com", "sifra123");
        HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		
		
		ResponseEntity<String> responseEntity = restTemplate.exchange("/api/news/unsubscribe/" + EXISTING_REGISTERED_USER_ID + "/" + NON_EXISTING_CULTURAL_OFFER_ID
				, HttpMethod.PUT,
				new HttpEntity<Object>(null, headers), String.class);
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	
	@Test
	public void testGetSubscribedNews_WithExistingUserId_ShouldReturnFirst5News(){
		
		login("email_adresa20@gmail.com", "sifra123");
        HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		
		
		Pageable pageable = PageRequest.of(PAGE_NO, PAGE_SIZE);
		
		int size = newsService.getSubscribedNews(EXISTING_REGISTERED_USER_ID, pageable).getNumberOfElements();
		ParameterizedTypeReference<CustomPageImplementation<NewsDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<NewsDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<NewsDTO>> responseEntity = restTemplate
				.exchange("/api/news/subscribed-news/" + EXISTING_REGISTERED_USER_ID + "?page=" + PAGE_NO + "&size=" + PAGE_SIZE, HttpMethod.GET,
						new HttpEntity<>(null,headers), type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(PAGE_NO ,responseEntity.getBody().getNumber());
		//Postavljen je size iz service-a jer ne znamo velicinu stranice koje ce se vratiti
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}
	
	@Test
	public void testGetSubscribedNews_WithNonExistingUserId_ShouldReturnEmptyCollection(){

		
		login("email_adresa20@gmail.com", "sifra123");
        HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		
		
		ParameterizedTypeReference<CustomPageImplementation<NewsDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<NewsDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<NewsDTO>> responseEntity = restTemplate
				.exchange("/api/news/subscribed-news/" + NON_EXISTING_REGISTERED_USER_ID + "?page=" + PAGE_NO + "&size=" + PAGE_SIZE, HttpMethod.GET, 
						new HttpEntity<>(null,headers), type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(PAGE_NO ,responseEntity.getBody().getNumber());
		assertEquals(0, responseEntity.getBody().getNumberOfElements());
	}
	
	@Test
	public void testGetAllNewsByCulturalOffer_WithExistingCOIDPageable_ShouldReturnFirst5News() {
		Pageable pageable = PageRequest.of(PAGE_NO, PAGE_SIZE);
		int size = newsService.findAllByCulturalOffer(EXISTING_CULTURAL_OFFER_ID, pageable).getNumberOfElements();
		ParameterizedTypeReference<CustomPageImplementation<NewsDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<NewsDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<NewsDTO>> responseEntity = restTemplate
				.exchange("/api/news/"+ EXISTING_CULTURAL_OFFER_ID +"/by-page?page=" + PAGE_NO + "&size=" + PAGE_SIZE, HttpMethod.GET, null, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(PAGE_NO ,responseEntity.getBody().getNumber());
		//Postavljen je size iz service-a jer ne znamo velicinu stranice koje ce se vratiti
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}

	@Test
	public void testGetAllNewsByCulturalOffer_WithNonExistingPageable_ShouldReturnEmptyCollection() {
		Pageable pageable = PageRequest.of(NON_EXISTING_PAGE_NO, PAGE_SIZE);
		int size = newsService.findAllByCulturalOffer(EXISTING_CULTURAL_OFFER_ID, pageable).getNumberOfElements();
		
		ParameterizedTypeReference<CustomPageImplementation<NewsDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<NewsDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<NewsDTO>> responseEntity = restTemplate
				.exchange("/api/news/"+ EXISTING_CULTURAL_OFFER_ID +"/by-page?page=" + NON_EXISTING_PAGE_NO + "&size=" + PAGE_SIZE, HttpMethod.GET, null, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}
	
	@Test
	public void testGetAllNewsByCulturalOffer_WithNonExistingCOID_ShouldReturnEmptyCollection() {
		Pageable pageable = PageRequest.of(NON_EXISTING_PAGE_NO, PAGE_SIZE);
		int size = newsService.findAllByCulturalOffer(NON_EXISTING_CULTURAL_OFFER_ID, pageable).getNumberOfElements();
		
		ParameterizedTypeReference<CustomPageImplementation<NewsDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<NewsDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<NewsDTO>> responseEntity = restTemplate
				.exchange("/api/news/"+ NON_EXISTING_CULTURAL_OFFER_ID +"/by-page?page=" + NON_EXISTING_PAGE_NO + "&size=" + PAGE_SIZE, HttpMethod.GET, null, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}
	
	@Test
	public void testGetSubscribedNews_WithExistingCategorId_ShouldReturnFirst5News(){
		
		login("email_adresa20@gmail.com", "sifra123");
        HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		
		
		Pageable pageable = PageRequest.of(PAGE_NO, PAGE_SIZE);
		
		int size = newsService.getSubscribedNews(EXISTING_REGISTERED_USER_ID, EXISTING_CATEGORY_ID, pageable).getNumberOfElements();
		ParameterizedTypeReference<CustomPageImplementation<NewsDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<NewsDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<NewsDTO>> responseEntity = restTemplate
				.exchange("/api/news/subscribed-news/" + EXISTING_REGISTERED_USER_ID + "/"+ EXISTING_CATEGORY_ID + "/?page=" + PAGE_NO + "&size=" + PAGE_SIZE, HttpMethod.GET,
						new HttpEntity<>(null,headers), type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(PAGE_NO ,responseEntity.getBody().getNumber());
		//Postavljen je size iz service-a jer ne znamo velicinu stranice koje ce se vratiti
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}
	
	@Test
	public void testGetSubscribedNews_WithNonExistingCategoryId_ShouldReturnEmptyCollection(){

		
		login("email_adresa20@gmail.com", "sifra123");
        HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		
		
		ParameterizedTypeReference<CustomPageImplementation<NewsDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<NewsDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<NewsDTO>> responseEntity = restTemplate
				.exchange("/api/news/subscribed-news/" + EXISTING_REGISTERED_USER_ID +
						"/"+ NON_EXISTING_CATEGORY_ID +"/?page=" + PAGE_NO + "&size=" + PAGE_SIZE, HttpMethod.GET, 
						new HttpEntity<>(null,headers), type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(PAGE_NO ,responseEntity.getBody().getNumber());
		assertEquals(0, responseEntity.getBody().getNumberOfElements());
	}
	
}




