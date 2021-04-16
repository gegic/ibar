package com.ktsnwt.project.team9.controller.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import com.ktsnwt.project.team9.constants.CulturalOfferConstants;
import com.ktsnwt.project.team9.dto.CulturalOfferDTO;
import com.ktsnwt.project.team9.dto.GeolocationDTO;
import com.ktsnwt.project.team9.dto.UserLoginDTO;
import com.ktsnwt.project.team9.dto.response.BookResDTO;
import com.ktsnwt.project.team9.dto.response.UserTokenStateDTO;
import com.ktsnwt.project.team9.helper.implementations.CustomPageImplementation;
import com.ktsnwt.project.team9.model.CulturalOffer;
import com.ktsnwt.project.team9.model.Geolocation;
import com.ktsnwt.project.team9.services.implementations.CulturalOfferService;
import com.ktsnwt.project.team9.services.implementations.GeolocationService;

import javassist.NotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class CulturalOfferControllerIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	CulturalOfferService culturalOfferService;

	@Autowired
	GeolocationService geolocationService;

	private String accessToken;

	public void login(String username, String password) {
		ResponseEntity<UserTokenStateDTO> responseEntity = restTemplate.postForEntity("/auth/login",
				new UserLoginDTO(username, password), UserTokenStateDTO.class);
		accessToken = "Bearer " + responseEntity.getBody().getAccessToken();
	}

	private HttpEntity<LinkedMultiValueMap<String, Object>> createFormData(CulturalOfferDTO culturalOffer,
			String path) {
		login("email_adresa1@gmail.com", "sifra123");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
		// this is how to add multipartfile
		parameters.add("file", new FileSystemResource(path));
		// this set content type for json object
		HttpHeaders partHeaders = new HttpHeaders();
		partHeaders.setContentType(MediaType.APPLICATION_JSON);
		// this represent dto like json and content type json
		HttpEntity<CulturalOfferDTO> dto = new HttpEntity<CulturalOfferDTO>(culturalOffer, partHeaders);
		parameters.add("culturalOfferDTO", dto);
		return new HttpEntity<LinkedMultiValueMap<String, Object>>(parameters, headers);
	}

	@Test
	public void testGetAllCulturalOffers_WithPageable_ShouldReturnFirst10CulturalOffers() {
		Pageable pageable = PageRequest.of(0, 10);
		int size = culturalOfferService.findAll(pageable).getNumberOfElements();
		ParameterizedTypeReference<CustomPageImplementation<BookResDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<BookResDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<BookResDTO>> responseEntity = restTemplate
				.exchange("/api/cultural-offers/by-page?page=0&size=10", HttpMethod.GET, null, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}

	@Test
	public void testGetAllCulturalOffers_WithNonExistingPageable_ShouldReturnEmptyCollection() {
		Pageable pageable = PageRequest.of(5, 10);
		int size = culturalOfferService.findAll(pageable).getNumberOfElements();
		ParameterizedTypeReference<CustomPageImplementation<BookResDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<BookResDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<BookResDTO>> responseEntity = restTemplate
				.exchange("/api/cultural-offers/by-page?page=5&size=10", HttpMethod.GET, null, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}

	@Test
	public void testGetCulturalOffersByCategoryId_WithExistingCategoryId_ShouldReturnCulturalOffersWithThatCategory() {
		Pageable pageable = PageRequest.of(0, 10);
		int size = culturalOfferService.getByCategoryId(5L, pageable).getNumberOfElements();
		ParameterizedTypeReference<CustomPageImplementation<BookResDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<BookResDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<BookResDTO>> responseEntity = restTemplate
				.exchange("/api/cultural-offers/category/5?page=0&size=10", HttpMethod.GET, null, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}

	@Test
	public void testGetCulturalOffersByCategoryId_WithNonExistingCategoryId_ShouldReturnEmptyCollection() {
		Pageable pageable = PageRequest.of(0, 10);
		int size = culturalOfferService.getByCategoryId(55L, pageable).getNumberOfElements();
		ParameterizedTypeReference<CustomPageImplementation<BookResDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<BookResDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<BookResDTO>> responseEntity = restTemplate
				.exchange("/api/cultural-offers/category/55?page=0&size=10", HttpMethod.GET, null, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}

	@Test
	public void testFindCulturalOfferByCategoryIdAndName_WithExistingCategoryIdAndContainsName_ShouldReturnCulturalOffersWithThatCategoryAndId() {
		Pageable pageable = PageRequest.of(0, 10);
		int size = culturalOfferService.findByCategoryIdAndNameContains(9L, "vol", pageable).getNumberOfElements();
		ParameterizedTypeReference<CustomPageImplementation<BookResDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<BookResDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<BookResDTO>> responseEntity = restTemplate.exchange(
				"/api/cultural-offers/category/9/find-by-name/vol?page=0&size=10", HttpMethod.GET, null, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}

	@Test
	public void testFindCulturalOfferByCategoryIdAndName_WithExistingCategoryIdAndNotContainsName_ShouldReturnEmptyCollection() {
		Pageable pageable = PageRequest.of(0, 10);
		int size = culturalOfferService.findByCategoryIdAndNameContains(9L, "gfdgdf", pageable).getNumberOfElements();
		ParameterizedTypeReference<CustomPageImplementation<BookResDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<BookResDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<BookResDTO>> responseEntity = restTemplate.exchange(
				"/api/cultural-offers/category/9/find-by-name/gfdgdf?page=0&size=10", HttpMethod.GET, null, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}

	@Test
	public void testFindCulturalOfferByCategoryIdAndName_WithNonExistingCategoryIdAndContainsName_ShouldReturnEmptyCollection() {
		Pageable pageable = PageRequest.of(0, 10);
		int size = culturalOfferService.findByCategoryIdAndNameContains(55L, "vol", pageable).getNumberOfElements();
		ParameterizedTypeReference<CustomPageImplementation<BookResDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<BookResDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<BookResDTO>> responseEntity = restTemplate.exchange(
				"/api/cultural-offers/category/55/find-by-name/vol?page=0&size=10", HttpMethod.GET, null, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}

	@Test
	public void testFindCulturalOfferByName_WithContainsName_ShouldReturnTwoCulturalOffers() {
		Pageable pageable = PageRequest.of(0, 10);
		int size = culturalOfferService.findByNameContains(CulturalOfferConstants.SUBSTRING_NAME, pageable)
				.getNumberOfElements();
		ParameterizedTypeReference<CustomPageImplementation<BookResDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<BookResDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<BookResDTO>> responseEntity = restTemplate
				.exchange("/api/cultural-offers/find-by-name/anast?page=0&size=10", HttpMethod.GET, null, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}

	@Test
	public void testFindCulturalOfferByName_WithNotContainsName_ShouldReturnEmptyCollection() {
		Pageable pageable = PageRequest.of(0, 10);
		int size = culturalOfferService.findByNameContains("ffbfx", pageable).getNumberOfElements();
		ParameterizedTypeReference<CustomPageImplementation<BookResDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<BookResDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<BookResDTO>> responseEntity = restTemplate
				.exchange("/api/cultural-offers/find-by-name/ffbfx?page=0&size=10", HttpMethod.GET, null, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}

	@Test
	public void testGetCulturalOffer_WithExistingId_ShouldReturnCulturalOffer() {
		ResponseEntity<BookResDTO> responseEntity = restTemplate.getForEntity("/api/cultural-offers/1",
				BookResDTO.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("Manastir 1", responseEntity.getBody().getName());
	}

	@Test
	public void testGetCulturalOffer_WithNonExistingId_ShouldReturnNotFound() {
		ResponseEntity<BookResDTO> responseEntity = restTemplate.getForEntity("/api/cultural-offers/55",
				BookResDTO.class);

		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testCreateCulturalOffer_WithValidParameters_ShouldReturnCreatedCulturalOffer() throws Exception {
		int length = ((List<CulturalOffer>) culturalOfferService.getAll()).size();
		int lengthGeolocation = ((List<Geolocation>) geolocationService.getAll()).size();
		Path newPath = Paths.get("src/main/resources/uploadedImages/New offer_newImage.jpg");
		File fileExist = new File(newPath.toString());
		CulturalOfferDTO culturalOffer = new CulturalOfferDTO(CulturalOfferConstants.NEW_OFFER_NAME,
				CulturalOfferConstants.NEW_OFFER_DESCRIPTION, CulturalOfferConstants.NEW_OFFER_GEOLOCATION_DTO,
				CulturalOfferConstants.NEW_OFFER_CATEGORY.getId(), CulturalOfferConstants.NEW_OFFER_ADMIN.getId());

		HttpEntity<LinkedMultiValueMap<String, Object>> entity = createFormData(culturalOffer,
				"src/test/resources/uploadedImages/newImage.jpg");

		ResponseEntity<BookResDTO> responseEntity = restTemplate.postForEntity("/api/cultural-offers", entity,
				BookResDTO.class);

		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals(CulturalOfferConstants.NEW_OFFER_NAME, responseEntity.getBody().getName());
		assertEquals(CulturalOfferConstants.NEW_OFFER_DESCRIPTION, responseEntity.getBody().getDescription());
		assertEquals(CulturalOfferConstants.NEW_OFFER_GEOLOCATION.getLocation(),
				responseEntity.getBody().getGeolocation().getLocation());
		assertEquals(CulturalOfferConstants.NEW_OFFER_CATEGORY.getId(), responseEntity.getBody().getCategory().getId());
		assertEquals(1L, responseEntity.getBody().getAdmin());
		assertEquals(length + 1, ((List<CulturalOffer>) culturalOfferService.getAll()).size());
		assertEquals(lengthGeolocation + 1, ((List<Geolocation>) geolocationService.getAll()).size());
		assertTrue(fileExist.exists());
	}

	@Test
	public void testCreateCulturalOffer_WithNonExistingCategoryId_ShouldReturnBadRequest() throws IOException {
		CulturalOfferDTO culturalOffer = new CulturalOfferDTO(CulturalOfferConstants.NEW_OFFER_NAME,
				CulturalOfferConstants.NEW_OFFER_DESCRIPTION, CulturalOfferConstants.NEW_OFFER_GEOLOCATION_DTO,
				CulturalOfferConstants.NON_EXISTING_OFFER_CATEGORY.getId(),
				CulturalOfferConstants.NEW_OFFER_ADMIN.getId());
		HttpEntity<LinkedMultiValueMap<String, Object>> entity = createFormData(culturalOffer,
				"src/test/resources/uploadedImages/newImage.jpg");

		ResponseEntity<BookResDTO> responseEntity = restTemplate.postForEntity("/api/cultural-offers", entity,
				BookResDTO.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}

	@Test
	public void testCreateCulturalOffer_WithExistingGeolocationLatAndLon_ShouldReturnBadRequest() throws IOException {
		CulturalOfferDTO culturalOffer = new CulturalOfferDTO(CulturalOfferConstants.NEW_OFFER_NAME,
				CulturalOfferConstants.NEW_OFFER_DESCRIPTION, CulturalOfferConstants.GEOLOCATION_EXIST_DTO,
				CulturalOfferConstants.NON_EXISTING_OFFER_CATEGORY.getId(),
				CulturalOfferConstants.NEW_OFFER_ADMIN.getId());
		HttpEntity<LinkedMultiValueMap<String, Object>> entity = createFormData(culturalOffer,
				"src/test/resources/uploadedImages/newImage.jpg");

		ResponseEntity<BookResDTO> responseEntity = restTemplate.postForEntity("/api/cultural-offers", entity,
				BookResDTO.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}

	@Test
	public void testCreateCulturalOffer_WithEmptyName_ShouldReturnBadRequest() throws IOException {
		CulturalOfferDTO culturalOffer = new CulturalOfferDTO("", CulturalOfferConstants.NEW_OFFER_DESCRIPTION,
				CulturalOfferConstants.GEOLOCATION_EXIST_DTO,
				CulturalOfferConstants.NON_EXISTING_OFFER_CATEGORY.getId(),
				CulturalOfferConstants.NEW_OFFER_ADMIN.getId());
		HttpEntity<LinkedMultiValueMap<String, Object>> entity = createFormData(culturalOffer,
				"src/test/resources/uploadedImages/newImage.jpg");

		ResponseEntity<BookResDTO> responseEntity = restTemplate.postForEntity("/api/cultural-offers", entity,
				BookResDTO.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}

	@Test
	public void testCreateCulturalOffer_WithInvalidGeolocation_ShouldReturnBadRequest() throws IOException {
		CulturalOfferDTO culturalOffer = new CulturalOfferDTO("", CulturalOfferConstants.NEW_OFFER_DESCRIPTION,
				new GeolocationDTO("gfdgfd46rg4r6", "", 0, 0),
				CulturalOfferConstants.NON_EXISTING_OFFER_CATEGORY.getId(),
				CulturalOfferConstants.NEW_OFFER_ADMIN.getId());
		HttpEntity<LinkedMultiValueMap<String, Object>> entity = createFormData(culturalOffer,
				"src/test/resources/uploadedImages/newImage.jpg");

		ResponseEntity<BookResDTO> responseEntity = restTemplate.postForEntity("/api/cultural-offers", entity,
				BookResDTO.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}

	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testUpdateCulturalOffer_WithValidParameters_ShouldReturnUpdatedCulturalOffer()
			throws IOException, NotFoundException {

		CulturalOffer culturalOffer = culturalOfferService.getById(1L);

		HttpEntity<LinkedMultiValueMap<String, Object>> entity = createFormData(
				new CulturalOfferDTO("New updated name", "New description",
						new GeolocationDTO("fgfdgf", "New updated location", 9, 9), 5L, 1L),
				"src/test/resources/uploadedImages/newImage.jpg");

		ResponseEntity<BookResDTO> responseEntity = restTemplate.exchange("/api/cultural-offers/1",
				HttpMethod.PUT, entity, BookResDTO.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(5L, responseEntity.getBody().getCategory().getId());
		assertEquals("New description", responseEntity.getBody().getDescription());
		assertEquals("New updated location", responseEntity.getBody().getGeolocation().getLocation());

		Path path = Paths.get("src/main/resources/uploadedImages/slika1.jpg");
		byte[] content = Files.readAllBytes(path);
		MockMultipartFile file = new MockMultipartFile("file", "slika1.jpg", MediaType.IMAGE_JPEG_VALUE, content);

		culturalOffer = culturalOfferService.update(1L, culturalOffer, file);
	}

	@Test
	public void testUpdateCulturalOffer_WithNonExistingId_ShouldReturnUpdatedCulturalOffer() throws IOException {
		HttpEntity<LinkedMultiValueMap<String, Object>> entity = createFormData(
				new CulturalOfferDTO("New updated name", "New description",
						new GeolocationDTO("fgfdgf", "New updated location", 9, 9), 5L, 1L),
				"src/test/resources/uploadedImages/newImage.jpg");

		ResponseEntity<BookResDTO> responseEntity = restTemplate.exchange("/api/cultural-offers/55",
				HttpMethod.PUT, entity, BookResDTO.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}

	@Test
	public void testUpdateCulturalOffer_WithExistingLatAndLon_ShouldReturnUpdatedCulturalOffer() throws IOException {
		HttpEntity<LinkedMultiValueMap<String, Object>> entity = createFormData(
				new CulturalOfferDTO("New updated name", "New description",
						CulturalOfferConstants.GEOLOCATION_EXIST_DTO, 5L, 1L),
				"src/test/resources/uploadedImages/newImage.jpg");

		ResponseEntity<BookResDTO> responseEntity = restTemplate.exchange("/api/cultural-offers/1",
				HttpMethod.PUT, entity, BookResDTO.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}

	@Test
	public void testUpdateCulturalOffer_WithNonExistingCategoryId_ShouldReturnUpdatedCulturalOffer()
			throws IOException {
		HttpEntity<LinkedMultiValueMap<String, Object>> entity = createFormData(
				new CulturalOfferDTO("New updated name", "New description",
						CulturalOfferConstants.GEOLOCATION_EXIST_DTO, 55L, 1L),
				"src/test/resources/uploadedImages/newImage.jpg");

		ResponseEntity<BookResDTO> responseEntity = restTemplate.exchange("/api/cultural-offers/1",
				HttpMethod.PUT, entity, BookResDTO.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}

	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testDeleteCulturalOffer_WithExistingId_ShouldReturnTrue() throws Exception {
		login("email_adresa1@gmail.com", "sifra123");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		int length = ((List<CulturalOffer>) culturalOfferService.getAll()).size();
		CulturalOffer culturalOffer = culturalOfferService.getById(20L);
		Path path = Paths.get("src/main/resources/uploadedImages/Reka_imageForDelete.jpg");
		byte[] content = Files.readAllBytes(path);
		MockMultipartFile file = new MockMultipartFile("file", "imageForDelete.jpg", MediaType.IMAGE_JPEG_VALUE,
				content);

		ResponseEntity<Boolean> responseEntity = restTemplate.exchange("/api/cultural-offers/20", HttpMethod.DELETE,
				new HttpEntity<Object>(headers), Boolean.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertTrue(responseEntity.getBody());
		assertEquals(length - 1, ((List<CulturalOffer>) culturalOfferService.getAll()).size());

		culturalOfferService.create(culturalOffer, file);
	}

	@Test
	public void testDeleteCulturalOffer_WithNonExistingId_ShouldReturnNotFound() throws Exception {
		login("email_adresa1@gmail.com", "sifra123");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		ResponseEntity<Boolean> responseEntity = restTemplate.exchange("/api/cultural-offers/55", HttpMethod.DELETE,
				new HttpEntity<Object>(headers), Boolean.class);

		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

	}
	
	@Test
	public void testGetSubscribedCulturalOffer_WithExistingUserId_ShouldReturnCollectionWithOffers() {
		login("email_adresa20@gmail.com", "sifra123");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		Pageable pageable = PageRequest.of(0, 10);
		int size = culturalOfferService.getSubscribedCulturalOffer(8L, pageable).getNumberOfElements();
		ParameterizedTypeReference<CustomPageImplementation<BookResDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<BookResDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<BookResDTO>> responseEntity = restTemplate
				.exchange("/api/cultural-offers/subscribed/8?page=0&size=10", HttpMethod.GET, new HttpEntity<Object>(headers), type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}
	
	@Test
	public void testGetSubscribedCulturalOffer_WithNonExistingUserId_ShouldReturnEmptyCollection() {
		login("email_adresa20@gmail.com", "sifra123");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		ParameterizedTypeReference<CustomPageImplementation<BookResDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<BookResDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<BookResDTO>> responseEntity = restTemplate
				.exchange("/api/cultural-offers/subscribed/800?page=0&size=10", HttpMethod.GET, new HttpEntity<Object>(headers), type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(0, responseEntity.getBody().getNumberOfElements());
	}

}
