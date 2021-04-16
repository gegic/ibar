package com.ktsnwt.project.team9.service.integration;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.dto.ImageDTO;
import com.ktsnwt.project.team9.helper.implementations.ImageMapper;
import com.ktsnwt.project.team9.model.Image;
import com.ktsnwt.project.team9.services.implementations.ImageService;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class ImageServiceIntegrationTest {

	private static final Object IMAGE_URL = "src/main/resources/uploadedImages/slika1.jpg";

	private static final ImageDTO IMAGE_FOR_CREATE = new ImageDTO("RNG.jpg");

	@Autowired
	private ImageService imageService;
	
	@Autowired
	private ImageMapper imageMapper;
	
	@Test
	public void testGetAll_ShouldReturnAllImage() {
		List<Image> image = (List<Image>) imageService.getAll();

		assertEquals(43, image.size());
	}


	@Test
	public void testGetById_WithExistingId_ShouldReturnImageWithThatId() {
		Image image = imageService.getById(1L);

		assertEquals(IMAGE_URL, image.getUrl());
	}

	@Test
	public void testGetById_WithNonExistingId_ShouldReturnNull() {
		Image image = imageService.getById(200L);
		
		assertNull(image);
	}

	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testCreate_WithValidParameters_ShouldReturnCreatedImage() throws Exception {
		int length = ((List<Image>) imageService.getAll()).size();
		
		Image imageBefore = imageMapper.toEntity(IMAGE_FOR_CREATE);
		
		Image imageAfter = imageService.create(imageBefore);
		
		assertEquals(imageBefore.getUrl(), imageAfter.getUrl());
		assertEquals(length + 1, imageService.getAll().size());
	}
	

	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testDelete_WithExistingId_ShouldReturnTrue() throws Exception {
		int length = ((List<Image>) imageService.getAll()).size();
		
		Boolean status = imageService.delete(40L);

		assertTrue(status);
		assertEquals(length - 1, ((List<Image>) imageService.getAll()).size());
	}

	@Test(expected = Exception.class)
	public void testDelete_WithNonExistingId_ShouldThrowException() throws Exception {
		imageService.delete(200L);
	}
	
	@Test(expected = Exception.class)
	public void testDelete_ViolatesIntegrty_ShouldThrowException() throws Exception {
		imageService.delete(1L);
	}


	

	

}
