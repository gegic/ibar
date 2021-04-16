package com.ktsnwt.project.team9.controller.integration;

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
import com.ktsnwt.project.team9.constants.GeolocationConstants;
import com.ktsnwt.project.team9.dto.GeolocationDTO;
import com.ktsnwt.project.team9.dto.UserLoginDTO;
import com.ktsnwt.project.team9.dto.response.UserTokenStateDTO;
import com.ktsnwt.project.team9.helper.implementations.CustomPageImplementation;
import com.ktsnwt.project.team9.model.Geolocation;
import com.ktsnwt.project.team9.services.implementations.GeolocationService;
import javassist.NotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class GeolocationControllerIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private GeolocationService geolocationService;

	private String accessToken;

	public void login(String username, String password) {
		ResponseEntity<UserTokenStateDTO> responseEntity = restTemplate.postForEntity("/auth/login",
				new UserLoginDTO(username, password), UserTokenStateDTO.class);
		accessToken = "Bearer " + responseEntity.getBody().getAccessToken();
	}

	@Test
	public void testGetAllGeolocations_ShouldReturnAllGeolocations() {
		login("email_adresa1@gmail.com", "sifra123");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		int size = ((List<Geolocation>) geolocationService.getAll()).size();

		ResponseEntity<GeolocationDTO[]> responseEntity = restTemplate.exchange("/api/geolocations", HttpMethod.GET,
				httpEntity, GeolocationDTO[].class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(size, responseEntity.getBody().length);
	}

	@Test
	public void testGetAllGeolocations_WithPageable_ShouldReturnFirst10Geolocations() {
		login("email_adresa1@gmail.com", "sifra123");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		Pageable pageable = PageRequest.of(0, 10);
		int size = geolocationService.getAll(pageable).getNumberOfElements();
		ParameterizedTypeReference<CustomPageImplementation<GeolocationDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<GeolocationDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<GeolocationDTO>> responseEntity = restTemplate
				.exchange("/api/geolocations/by-page?page=0&size=10", HttpMethod.GET, httpEntity, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}

	@Test
	public void testGetAllGeolocations_WithNonExistingPageable_ShouldReturnEmptyCollection() {
		login("email_adresa1@gmail.com", "sifra123");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		Pageable pageable = PageRequest.of(3, 10);
		int size = geolocationService.getAll(pageable).getNumberOfElements();
		ParameterizedTypeReference<CustomPageImplementation<GeolocationDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<GeolocationDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<GeolocationDTO>> responseEntity = restTemplate
				.exchange("/api/geolocations/by-page?page=3&size=10", HttpMethod.GET, httpEntity, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}

	@Test
	public void testGetGeolocation_WithExistingId_ShouldReturnGeolocationDTO() throws NotFoundException {
		login("email_adresa1@gmail.com", "sifra123");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<GeolocationDTO> responseEntity = restTemplate.exchange("/api/geolocations/1", HttpMethod.GET,
				httpEntity, GeolocationDTO.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("Neka lokacija 1", responseEntity.getBody().getLocation());
	}

	@Test
	public void testGetGeolocation_WithNonExistingId_ShouldReturnNotFound() {
		login("email_adresa1@gmail.com", "sifra123");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<GeolocationDTO> responseEntity = restTemplate.exchange("/api/geolocations/55", HttpMethod.GET,
				httpEntity, GeolocationDTO.class);

		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testCreateGeolocation_WithValidParameters_ShouldReturnCreatedGeolocation() throws NotFoundException {
		login("email_adresa1@gmail.com", "sifra123");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		int length = ((List<Geolocation>) geolocationService.getAll()).size();
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(GeolocationConstants.GEOLOCATIONNEW, headers);

		ResponseEntity<GeolocationDTO> responseEntity = restTemplate.postForEntity("/api/geolocations", httpEntity,
				GeolocationDTO.class);

		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals(GeolocationConstants.GEOLOCATIONNEW.getLocation(), responseEntity.getBody().getLocation());
		assertEquals(length + 1, ((List<Geolocation>) geolocationService.getAll()).size());
	}

	@Test
	public void testCreateGeolocation_WithExistingLatAndLon_ShouldReturnBadRequest() throws NotFoundException {
		login("email_adresa1@gmail.com", "sifra123");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(GeolocationConstants.GEOLOCATION1, headers);

		ResponseEntity<GeolocationDTO> responseEntity = restTemplate.postForEntity("/api/geolocations", httpEntity,
				GeolocationDTO.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}

	@Test
	public void testCreateGeolocation_WithEmptyLocation_ShouldReturnBadRequest() throws NotFoundException {
		login("email_adresa1@gmail.com", "sifra123");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(new GeolocationDTO("", "", 5, 5), headers);

		ResponseEntity<GeolocationDTO> responseEntity = restTemplate.postForEntity("/api/geolocations", httpEntity,
				GeolocationDTO.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}

	@Test
	public void testCreateGeolocation_WithNullLocation_ShouldReturnBadRequest() throws NotFoundException {
		login("email_adresa1@gmail.com", "sifra123");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(new GeolocationDTO("", null, 5, 5), headers);

		ResponseEntity<GeolocationDTO> responseEntity = restTemplate.postForEntity("/api/geolocations", httpEntity,
				GeolocationDTO.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}

	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testDeleteGeolocation_WithExistingId_ShouldReturnTrue() throws NotFoundException {
		login("email_adresa1@gmail.com", "sifra123");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		int length = ((List<Geolocation>) geolocationService.getAll()).size();

		ResponseEntity<Boolean> responseEntity = restTemplate.exchange(
				"/api/geolocations/" + GeolocationConstants.IDFORDELETE, HttpMethod.DELETE,
				new HttpEntity<Object>(headers), Boolean.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertTrue(responseEntity.getBody());
		assertEquals(length - 1, ((List<Geolocation>) geolocationService.getAll()).size());
	}

	@Test
	public void testDeleteGeolocation_WithNonExistingId_ShouldReturnNotFound() throws NotFoundException {
		login("email_adresa1@gmail.com", "sifra123");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		ResponseEntity<Boolean> responseEntity = restTemplate.exchange("/api/geolocations/55", HttpMethod.DELETE,
				new HttpEntity<Object>(headers), Boolean.class);

		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
}
