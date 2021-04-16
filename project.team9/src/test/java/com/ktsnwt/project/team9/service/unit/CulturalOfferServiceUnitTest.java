package com.ktsnwt.project.team9.service.unit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import com.ktsnwt.project.team9.constants.CulturalOfferConstants;
import com.ktsnwt.project.team9.helper.implementations.FileService;
import com.ktsnwt.project.team9.model.CulturalOffer;
import com.ktsnwt.project.team9.model.Image;
import com.ktsnwt.project.team9.repositories.ICulturalOfferRepository;
import com.ktsnwt.project.team9.services.implementations.AdminService;
import com.ktsnwt.project.team9.services.implementations.CategoryService;
import com.ktsnwt.project.team9.services.implementations.CulturalOfferService;
import com.ktsnwt.project.team9.services.implementations.GeolocationService;
import com.ktsnwt.project.team9.services.implementations.ImageService;
import javassist.NotFoundException;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class CulturalOfferServiceUnitTest {

	@Autowired
	CulturalOfferService culturalOfferService;

	@MockBean
	ICulturalOfferRepository culturalOfferRepository;

	@MockBean
	FileService fileService;

	@MockBean
	GeolocationService geolocationService;

	@MockBean
	CategoryService categoryService;
	
	@MockBean
	ImageService imageService;
	
	@MockBean
	AdminService adminService;

	@Before
	public void setup() throws Exception {
		CulturalOffer culturalOffer = new CulturalOffer(CulturalOfferConstants.NAME1,
				CulturalOfferConstants.DESCRIPTION1, CulturalOfferConstants.GEOLOCATION1,
				CulturalOfferConstants.CATEGORY1, CulturalOfferConstants.ADMIN1);
		culturalOffer.setId(CulturalOfferConstants.ID1);
		Image image = new Image(CulturalOfferConstants.IMAGEURL);
		culturalOffer.setImage(image);
		culturalOffer.setActive(true);
		culturalOffer.setAverageMark(0);
		culturalOffer.setComments(new HashSet<>());
		culturalOffer.setMarks(new HashSet<>());
		culturalOffer.setNews(new HashSet<>());
		culturalOffer.setSubscribedUsers(new HashSet<>());

		CulturalOffer culturalOfferToUpdate = new CulturalOffer(CulturalOfferConstants.NAME1,
				CulturalOfferConstants.DESCRIPTION2, CulturalOfferConstants.GEOLOCATION2,
				CulturalOfferConstants.CATEGORY3, CulturalOfferConstants.ADMIN1);
		culturalOffer.setId(CulturalOfferConstants.ID1);
		culturalOfferToUpdate.setImage(image);
		culturalOfferToUpdate.setActive(true);
		culturalOfferToUpdate.setAverageMark(0);
		culturalOfferToUpdate.setComments(new HashSet<>());
		culturalOfferToUpdate.setMarks(new HashSet<>());
		culturalOfferToUpdate.setNews(new HashSet<>());
		culturalOfferToUpdate.setSubscribedUsers(new HashSet<>());

		MockMultipartFile file = new MockMultipartFile("file", "slika1.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[0]);

		given(culturalOfferRepository.findById(CulturalOfferConstants.ID1)).willReturn(Optional.of(culturalOffer));
		given(culturalOfferRepository.findById(CulturalOfferConstants.ID2)).willReturn(Optional.empty());
		given(culturalOfferRepository.save(Mockito.any(CulturalOffer.class))).willReturn(culturalOfferToUpdate);

		given(categoryService.getById(CulturalOfferConstants.ID1)).willReturn(CulturalOfferConstants.CATEGORY1);
		given(categoryService.getById(CulturalOfferConstants.CATEGORY2ID)).willReturn(null);
		given(categoryService.getById(CulturalOfferConstants.CATEGORY3ID)).willReturn(CulturalOfferConstants.CATEGORY3);

		given(geolocationService.findByLatAndLon(CulturalOfferConstants.LAT, CulturalOfferConstants.LON))
				.willReturn(CulturalOfferConstants.GEOLOCATION1);
		given(geolocationService.create(CulturalOfferConstants.GEOLOCATION2)).willReturn(CulturalOfferConstants.GEOLOCATION2);
		
		given(imageService.create(Mockito.any(Image.class))).willReturn(CulturalOfferConstants.IMAGE);

		doNothing().when(fileService).deleteImageFromFile(CulturalOfferConstants.IMAGEURL);
		doNothing().when(fileService).uploadNewImage(file, CulturalOfferConstants.IMAGEURL);
		given(fileService.saveImage(file, "slika1.jpg")).willReturn(CulturalOfferConstants.IMAGEURL);
		
		given(adminService.getById(CulturalOfferConstants.ADMIN1.getId())).willReturn(CulturalOfferConstants.ADMIN1);
	}

	@Test
	public void testGetById_WithExistingId_ShouldReturnCulturalOffer() {
		CulturalOffer culturalOffer = culturalOfferService.getById(CulturalOfferConstants.ID1);

		verify(culturalOfferRepository, times(1)).findById(CulturalOfferConstants.ID1);
		assertEquals(CulturalOfferConstants.NAME1, culturalOffer.getName());
	}

	@Test
	public void testGetById_WithNonExistingId_ShouldReturnNull() {
		CulturalOffer culturalOffer = culturalOfferService.getById(CulturalOfferConstants.ID2);

		verify(culturalOfferRepository, times(1)).findById(CulturalOfferConstants.ID2);
		assertNull(culturalOffer);
	}

	@Test
	public void testDelete_WithExistingId_ShouldReturnTrue() throws Exception {
		Boolean status = culturalOfferService.delete(CulturalOfferConstants.ID1);

		verify(culturalOfferRepository, times(1)).deleteById(CulturalOfferConstants.ID1);
		verify(fileService, times(1)).deleteImageFromFile(CulturalOfferConstants.IMAGEURL);
		assertTrue(status);
	}

	@Test(expected = Exception.class)
	public void testDelete_WithNonExistingId_ShouldThrowsException() throws Exception {
		culturalOfferService.delete(CulturalOfferConstants.ID2);
	}

	@Test(expected = NotFoundException.class)
	public void testUpdate_WithNonExistingId_ShouldThrowsNotFoundException() throws Exception {
		CulturalOffer culturalOffer = new CulturalOffer(CulturalOfferConstants.NAME1,
				CulturalOfferConstants.DESCRIPTION1, CulturalOfferConstants.GEOLOCATION1,
				CulturalOfferConstants.CATEGORY1, CulturalOfferConstants.ADMIN1);
		MockMultipartFile file = new MockMultipartFile("file", "slika1.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[0]);

		culturalOfferService.update(CulturalOfferConstants.ID2, culturalOffer, file);
	}

	@Test(expected = NotFoundException.class)
	public void testUpdate_WithNonExistingCategoryId_ShouldThrowsNotFoundException() throws Exception {
		CulturalOffer culturalOffer = new CulturalOffer(CulturalOfferConstants.NAME1,
				CulturalOfferConstants.DESCRIPTION1, CulturalOfferConstants.GEOLOCATION1,
				CulturalOfferConstants.CATEGORY1, CulturalOfferConstants.ADMIN1);
		MockMultipartFile file = new MockMultipartFile("file", "slika1.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[0]);

		culturalOfferService.update(CulturalOfferConstants.ID1, culturalOffer, file);
	}

	@Test
	public void testUpdate_WithValidPatameters_ShouldReturnUpdatedCulturalOffer() throws Exception {
		CulturalOffer culturalOffer = new CulturalOffer(CulturalOfferConstants.NAME1,
				CulturalOfferConstants.DESCRIPTION2, CulturalOfferConstants.GEOLOCATION2,
				CulturalOfferConstants.CATEGORY3, CulturalOfferConstants.ADMIN1);
		MockMultipartFile file = new MockMultipartFile("file", "slika1.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[0]);

		CulturalOffer updatedOffer = culturalOfferService.update(CulturalOfferConstants.ID1, culturalOffer, file);

		verify(categoryService, times(1)).getById(CulturalOfferConstants.CATEGORY3ID);
		verify(geolocationService, times(1)).findByLatAndLon(CulturalOfferConstants.LAT, CulturalOfferConstants.LON);
		verify(culturalOfferRepository, times(1)).findById(CulturalOfferConstants.ID1);
		verify(fileService, times(0)).uploadNewImage(file, CulturalOfferConstants.IMAGEURL);
		assertEquals(culturalOffer.getName(), updatedOffer.getName());
		assertEquals(culturalOffer.getCategory().getName(), updatedOffer.getCategory().getName());
		assertEquals(culturalOffer.getGeolocation().getLat(), updatedOffer.getGeolocation().getLat());
		assertEquals(culturalOffer.getGeolocation().getLon(), updatedOffer.getGeolocation().getLon());
		assertEquals(culturalOffer.getDescription(), updatedOffer.getDescription());
		assertTrue(updatedOffer.isActive());
	}

	@Test(expected = NotFoundException.class)
	public void testCreate_WithNonExistingCategoryId_ShouldThrowsNotFoundException() throws Exception {
		CulturalOffer culturalOffer = new CulturalOffer(CulturalOfferConstants.NAME1,
				CulturalOfferConstants.DESCRIPTION1, CulturalOfferConstants.GEOLOCATION1,
				CulturalOfferConstants.CATEGORY2, CulturalOfferConstants.ADMIN1);
		MockMultipartFile file = new MockMultipartFile("file", "slika1.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[0]);

		culturalOfferService.create(culturalOffer, file);
	}

	@Test
	public void testCreate_WithValidParameters_ShouldReturnCreatedCulturalOffer() throws Exception {
		CulturalOffer culturalOffer = new CulturalOffer(CulturalOfferConstants.NAME1,
				CulturalOfferConstants.DESCRIPTION2, CulturalOfferConstants.GEOLOCATION2,
				CulturalOfferConstants.CATEGORY3, CulturalOfferConstants.ADMIN1);
		Path path = Paths.get("src/test/resources/uploadedImages/slika1.jpg");
		byte[] content = Files.readAllBytes(path);
		MockMultipartFile file = new MockMultipartFile("file", "slika1.jpg", MediaType.IMAGE_JPEG_VALUE, content);
		
		CulturalOffer newCulturalOffer =  culturalOfferService.create(culturalOffer, file);
		
		verify(categoryService, times(1)).getById(CulturalOfferConstants.CATEGORY3ID);
		verify(geolocationService, times(1)).create(CulturalOfferConstants.GEOLOCATION2);
		verify(adminService, times(1)).getById(CulturalOfferConstants.ADMIN1.getId());
		verify(fileService, times(0)).saveImage(file, CulturalOfferConstants.IMAGEURL);
		verify(imageService, times(1)).create(Mockito.any(Image.class));
		assertEquals(culturalOffer.getName(), newCulturalOffer.getName());
		assertEquals(culturalOffer.getCategory().getName(), newCulturalOffer.getCategory().getName());
		assertEquals(culturalOffer.getGeolocation().getLat(), newCulturalOffer.getGeolocation().getLat());
		assertEquals(culturalOffer.getGeolocation().getLon(), newCulturalOffer.getGeolocation().getLon());
		assertEquals(culturalOffer.getDescription(), newCulturalOffer.getDescription());
		assertTrue(newCulturalOffer.isActive());
		
	}
}
