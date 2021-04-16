package com.ktsnwt.project.team9.controller.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class ImageControllerIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Before
	public void setUp() throws IOException {
		Path target = Paths.get("src/main/resources/uploadedImages/imageRNG.jpg");
	    Path source = Paths.get("src/main/resources/uploadedImages/slika1.jpg");
	    if(!(target.toFile().exists()))
	    	Files.copy(source, target, StandardCopyOption.COPY_ATTRIBUTES );
	}
	
	
	@Test
	public void testUpload_ShouldReturnPath() throws IOException {
		LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
	    parameters.add("file", new FileSystemResource("src/test/resources/uploadedImages/newImage.jpg"));

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

	    HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<LinkedMultiValueMap<String, Object>>(parameters, headers);

	    ResponseEntity<String> response = restTemplate.exchange("/api/images/upload", HttpMethod.POST, entity, String.class, "");

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotEquals("", response.getBody());
		
	}
	
	@Test
	public void testDeleteImage_ShouldDeleteImage(){

	    ResponseEntity<String> response = restTemplate.exchange("/api/images/delete", HttpMethod.DELETE, new HttpEntity<Object>("imageRNG.jpg"), String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void testGetImage_WithExistingPath_ShouldReturnImage() {
		String path = "src/main/resources/uploadedImages/imageRNG.jpg";
		ResponseEntity<String> response = restTemplate.exchange("/api/images", HttpMethod.POST, new HttpEntity<Object>(path), String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotEquals(response.getBody(), "");
	}
	
	@Test
	public void testGetImage_WithNonExistingPath_ShouldReturnImage() {
		String path = "src/main/resources/uploadedImages/nepostojeci.jpg";
		ResponseEntity<String> response = restTemplate.exchange("/api/images", HttpMethod.POST, new HttpEntity<Object>(path), String.class);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	
}
