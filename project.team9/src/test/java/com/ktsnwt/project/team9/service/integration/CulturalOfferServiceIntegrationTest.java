package com.ktsnwt.project.team9.service.integration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityExistsException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.constants.CulturalOfferConstants;
import com.ktsnwt.project.team9.constants.FileServiceConstants;
import com.ktsnwt.project.team9.model.Admin;
import com.ktsnwt.project.team9.model.Category;
import com.ktsnwt.project.team9.model.CulturalOffer;
import com.ktsnwt.project.team9.model.Geolocation;
import com.ktsnwt.project.team9.services.implementations.CulturalOfferService;
import com.ktsnwt.project.team9.services.implementations.GeolocationService;

import javassist.NotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class CulturalOfferServiceIntegrationTest {

	@Autowired
	CulturalOfferService culturalOfferService;

	@Autowired
	GeolocationService geolocationService;

	@Test
	public void testGetAll_ShouldReturnAllCulturalOffers() {
		List<CulturalOffer> culturalOffers = (List<CulturalOffer>) culturalOfferService.getAll();

		assertEquals(20, culturalOffers.size());
	}

	@Test
	public void testFindAll_WithFirstPageable_ShouldReturnFirst10CulturalOffers() {
		Pageable pageable = PageRequest.of(0, 10);

		Page<CulturalOffer> culturalOffers = culturalOfferService.findAll(pageable);

		assertEquals(10, culturalOffers.getNumberOfElements());
	}

	@Test
	public void testFindAll_WithSecondPageable_ShouldReturnSecond10CulturalOffers() {
		Pageable pageable = PageRequest.of(1, 10);

		Page<CulturalOffer> culturalOffers = culturalOfferService.findAll(pageable);

		assertEquals(10, culturalOffers.getNumberOfElements());
	}

	@Test
	public void testFindAll_WithNonExistingPageable_ShouldReturnEmptyCollection() {
		Pageable pageable = PageRequest.of(2, 10);

		Page<CulturalOffer> culturalOffers = culturalOfferService.findAll(pageable);

		assertEquals(0, culturalOffers.getNumberOfElements());
	}

	@Test
	public void testGetById_WithExistingId_ShouldReturnCulturalOfferWithThatId() {
		CulturalOffer culturalOffer = culturalOfferService.getById(CulturalOfferConstants.ID1);

		assertEquals(CulturalOfferConstants.NAME1, culturalOffer.getName());
	}

	@Test
	public void testGetById_WithNonExistingId_ShouldReturnNull() {
		CulturalOffer culturalOffer = culturalOfferService.getById(55L);

		assertNull(culturalOffer);
	}

	@Test
	public void testGetByCategoryId_WithExistingCategoryId_ShouldReturnTwoGeolocations() {
		Pageable pageable = PageRequest.of(0, 10);

		Page<CulturalOffer> culturalOffers = culturalOfferService.getByCategoryId(CulturalOfferConstants.ID1, pageable);

		assertEquals(2, culturalOffers.getNumberOfElements());
	}

	@Test
	public void testGetByCategoryId_WithNonExistingCategoryId_ShouldReturnEmptyCollection() {
		Pageable pageable = PageRequest.of(0, 10);

		Page<CulturalOffer> culturalOffers = culturalOfferService.getByCategoryId(50L, pageable);

		assertEquals(0, culturalOffers.getNumberOfElements());
	}

	@Test
	public void testFindByCategoryIdAndNameContains_WithExistingCategoryIdAndContainsName_ShouldReturnOneCollection() {
		Pageable pageable = PageRequest.of(0, 10);

		Page<CulturalOffer> culturalOffers = culturalOfferService.findByCategoryIdAndNameContains(9L, "vol", pageable);

		assertEquals(1, culturalOffers.getNumberOfElements());
		assertEquals("Volujak", culturalOffers.getContent().get(0).getName());
	}

	@Test
	public void testFindByCategoryIdAndNameContains_WithNonExistingCategoryIdAndContainsName_ShouldReturnEmptyCollection() {
		Pageable pageable = PageRequest.of(0, 10);

		Page<CulturalOffer> culturalOffers = culturalOfferService.findByCategoryIdAndNameContains(15L, "vol", pageable);

		assertEquals(0, culturalOffers.getNumberOfElements());
	}

	@Test
	public void testFindByCategoryIdAndNameContains_WithExistingCategoryIdAndNotContainsName_ShouldReturnEmptyCollection() {
		Pageable pageable = PageRequest.of(0, 10);

		Page<CulturalOffer> culturalOffers = culturalOfferService.findByCategoryIdAndNameContains(9L, "dhgd", pageable);

		assertEquals(0, culturalOffers.getNumberOfElements());
	}
	
	@Test
	public void testFindByNameContains_WithContainsName_ShouldReturnTwoGeolocations() {
		Pageable pageable = PageRequest.of(0, 10);

		Page<CulturalOffer> culturalOffers = culturalOfferService.findByNameContains(CulturalOfferConstants.SUBSTRING_NAME, pageable);

		assertEquals(2, culturalOffers.getNumberOfElements());
	}
	
	@Test
	public void testFindByNameContains_WithNotContainsName_ShouldReturnEmptyCollection() {
		Pageable pageable = PageRequest.of(0, 10);

		Page<CulturalOffer> culturalOffers = culturalOfferService.findByNameContains("dhgd", pageable);

		assertEquals(0, culturalOffers.getNumberOfElements());
	}

	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testCreate_WithValidParameters_ShouldReturnCreatedCulturalOffer() throws Exception {
		int length = ((List<CulturalOffer>) culturalOfferService.getAll()).size();
		int lengthGeolocation = ((List<Geolocation>) geolocationService.getAll()).size();
		Path path = Paths.get("src/test/resources/uploadedImages/slika1.jpg");
		byte[] content = Files.readAllBytes(path);
		MockMultipartFile file = new MockMultipartFile("file", "newImage.jpg", MediaType.IMAGE_JPEG_VALUE, content);
		CulturalOffer culturalOffer = new CulturalOffer(CulturalOfferConstants.NEW_OFFER_NAME,
				CulturalOfferConstants.NEW_OFFER_DESCRIPTION, CulturalOfferConstants.NEW_OFFER_GEOLOCATION,
				CulturalOfferConstants.NEW_OFFER_CATEGORY, CulturalOfferConstants.NEW_OFFER_ADMIN);

		CulturalOffer newOffer = culturalOfferService.create(culturalOffer, file);
		Path newPath = Paths.get("src/main/resources/uploadedImages/New offer_newImage.jpg");
		File fileExist = new File(newPath.toString());

		assertEquals(CulturalOfferConstants.NEW_OFFER_NAME, newOffer.getName());
		assertEquals(CulturalOfferConstants.NEW_OFFER_DESCRIPTION, newOffer.getDescription());
		assertEquals(CulturalOfferConstants.NEW_OFFER_GEOLOCATION.getLocation(),
				newOffer.getGeolocation().getLocation());
		assertEquals(CulturalOfferConstants.NEW_OFFER_CATEGORY.getId(), newOffer.getCategory().getId());
		assertEquals(CulturalOfferConstants.NEW_OFFER_ADMIN.getId(), newOffer.getAdmin().getId());
		assertEquals(length + 1, ((List<CulturalOffer>) culturalOfferService.getAll()).size());
		assertEquals(lengthGeolocation + 1, ((List<Geolocation>) geolocationService.getAll()).size());
		assertTrue(fileExist.exists());

		Files.deleteIfExists(newPath);
	}

	@Test(expected = NotFoundException.class)
	public void testCreate_WithNonExistingCategoryId_ShouldThrowNotFoundException() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "newImage.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[0]);
		CulturalOffer culturalOffer = new CulturalOffer(CulturalOfferConstants.NEW_OFFER_NAME,
				CulturalOfferConstants.NEW_OFFER_DESCRIPTION, CulturalOfferConstants.NEW_OFFER_GEOLOCATION,
				CulturalOfferConstants.NON_EXISTING_OFFER_CATEGORY, CulturalOfferConstants.NEW_OFFER_ADMIN);

		culturalOfferService.create(culturalOffer, file);
	}

	@Test(expected = EntityExistsException.class)
	public void testCreate_WithExistingGeolocationLatAndLon_ShouldThrowEntityExistsException() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "newImage.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[0]);
		CulturalOffer culturalOffer = new CulturalOffer(CulturalOfferConstants.NEW_OFFER_NAME,
				CulturalOfferConstants.NEW_OFFER_DESCRIPTION, CulturalOfferConstants.GEOLOCATION1,
				CulturalOfferConstants.NEW_OFFER_CATEGORY, CulturalOfferConstants.NEW_OFFER_ADMIN);

		culturalOfferService.create(culturalOffer, file);
	}

	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testDelete_WithExistingId_ShouldReturnTrue() throws Exception {
		int length = ((List<CulturalOffer>) culturalOfferService.getAll()).size();
		int lengthGeolocation = ((List<Geolocation>) geolocationService.getAll()).size();
		CulturalOffer culturalOffer = culturalOfferService.getById(1L);
		Path pathComment = Paths.get("src/main/resources/uploadedImages/comment_slika6.jpg");
		byte[] contentCommentImage = Files.readAllBytes(pathComment);
		File fileExistCommentImage = new File(pathComment.toString());
		Path path = Paths.get(culturalOffer.getImage().getUrl());
		byte[] content = Files.readAllBytes(path);
		File fileExist = new File(path.toString());

		Boolean status = culturalOfferService.delete(1L);

		assertTrue(status);
		assertFalse(fileExist.exists());
		assertEquals(length - 1, ((List<CulturalOffer>) culturalOfferService.getAll()).size());
		assertEquals(lengthGeolocation - 1, ((List<Geolocation>) geolocationService.getAll()).size());

		OutputStream outputStream = new FileOutputStream(fileExist);
		outputStream.write(content);
		outputStream.close();

		OutputStream outputStreamComment = new FileOutputStream(fileExistCommentImage);
		outputStreamComment.write(contentCommentImage);
		outputStreamComment.close();
	}

	@Test(expected = NotFoundException.class)
	public void testDelete_WithNonExistingId_ShouldThrowNotFoundException() throws Exception {
		culturalOfferService.delete(55L);
	}

	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testUpdate_WithNewValidParameters_ShouldReturnUpdatedCulturalOffer() throws Exception {
		byte[] newImage = Files.readAllBytes(Paths.get(FileServiceConstants.NEW_IMAGE_UPLOAD));
		MockMultipartFile file = new MockMultipartFile("file", "image.jpg", MediaType.IMAGE_JPEG_VALUE, newImage);

		CulturalOffer culturalOffer = culturalOfferService.update(1L, CulturalOfferConstants.EXISTING_CULTURAL_OFFER,
				file);
		byte[] oldImageUpdated = Files.readAllBytes(Paths.get("src/main/resources/uploadedImages/slika1.jpg"));

		assertEquals(CulturalOfferConstants.NEW_OFFER_CATEGORY.getId(), culturalOffer.getCategory().getId());
		assertEquals(CulturalOfferConstants.NEW_OFFER_DESCRIPTION, culturalOffer.getDescription());
		assertEquals(CulturalOfferConstants.NEW_OFFER_GEOLOCATION.getLocation(),
				culturalOffer.getGeolocation().getLocation());
		assertTrue(Arrays.equals(newImage, oldImageUpdated));

	}

	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testUpdate_WithOutImage_ShouldReturnCulturalOfferWithSameImage() throws IOException, NotFoundException {
		byte[] oldImage = Files.readAllBytes(Paths.get("src/main/resources/uploadedImages/slika1.jpg"));
		MockMultipartFile file = new MockMultipartFile("file", "image.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[0]);

		culturalOfferService.update(1L, CulturalOfferConstants.EXISTING_CULTURAL_OFFER, file);
		byte[] oldImageUpdated = Files.readAllBytes(Paths.get("src/main/resources/uploadedImages/slika1.jpg"));

		assertTrue(Arrays.equals(oldImage, oldImageUpdated));
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void testUpdate_WithExistingGeolocation_ShouldThrowDataIntegrityViolationException()
			throws IOException, NotFoundException {
		MockMultipartFile file = new MockMultipartFile("file", "image.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[0]);
		CulturalOffer culturalOffer = new CulturalOffer("Manastir 1", CulturalOfferConstants.NEW_OFFER_DESCRIPTION,
				CulturalOfferConstants.GEOLOCATION1, CulturalOfferConstants.NEW_OFFER_CATEGORY, new Admin(1L));

		culturalOfferService.update(1L, culturalOffer, file);
	}

	@Test(expected = NotFoundException.class)
	public void testUpdate_WithNonExistingId_ShouldThrowNotFoundException() throws IOException, NotFoundException {
		MockMultipartFile file = new MockMultipartFile("file", "image.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[0]);
		CulturalOffer culturalOffer = new CulturalOffer("Manastir 1", CulturalOfferConstants.NEW_OFFER_DESCRIPTION,
				CulturalOfferConstants.NEW_OFFER_GEOLOCATION, CulturalOfferConstants.NEW_OFFER_CATEGORY, new Admin(1L));

		culturalOfferService.update(55L, culturalOffer, file);
	}

	@Test(expected = NotFoundException.class)
	public void testUpdate_WithNonExistingCategoryId_ShouldThrowNotFoundException()
			throws IOException, NotFoundException {
		MockMultipartFile file = new MockMultipartFile("file", "image.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[0]);
		CulturalOffer culturalOffer = new CulturalOffer("Manastir 1", CulturalOfferConstants.NEW_OFFER_DESCRIPTION,
				CulturalOfferConstants.NEW_OFFER_GEOLOCATION, new Category(55L), new Admin(1L));

		culturalOfferService.update(1L, culturalOffer, file);
	}
	
	@Test
	public void testGetSubscribedCulturalOffer_WithExistingUserId_ShouldReturnCollectionOfOffers() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<CulturalOffer> page = culturalOfferService.getSubscribedCulturalOffer(8L, pageable);
		assertEquals(3, page.getNumberOfElements());
	}
	
	@Test
	public void testGetSubscribedCulturalOffer_WithNonExistingUserId_ShouldReturnEmptyCollection() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<CulturalOffer> page = culturalOfferService.getSubscribedCulturalOffer(811L, pageable);
		assertEquals(0, page.getNumberOfElements());
	}

}
