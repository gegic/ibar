package com.ktsnwt.project.team9.repository.integration;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.constants.CulturalOfferConstants;
import com.ktsnwt.project.team9.model.CulturalOffer;
import com.ktsnwt.project.team9.repositories.ICulturalOfferRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class CulturalOfferRepositoryIntegrationTest {

	@Autowired
	ICulturalOfferRepository culturalOfferRepository;

	@Test
	public void testFindByName_WithExistingName_ShouldReturnCulturalOffer() {
		CulturalOffer culturalOffer = culturalOfferRepository.findByName(CulturalOfferConstants.NAME1);

		assertEquals(CulturalOfferConstants.NAME1, culturalOffer.getName());
	}

	@Test
	public void testFindByName_WithNonExistingName_ShouldReturnNull() {
		CulturalOffer culturalOffer = culturalOfferRepository.findByName(CulturalOfferConstants.NAME2);

		assertNull(culturalOffer);
	}

	@Test
	public void testGetByCategoryId_WithExistingCategoryId_ShouldReturnCulturalOfferPage() {
		Pageable pageable = PageRequest.of(CulturalOfferConstants.PAGE, CulturalOfferConstants.PAGE_SIZE);
		Page<CulturalOffer> culturalOfferPage = culturalOfferRepository.getByCategoryId(CulturalOfferConstants.ID1, pageable);

		assertEquals(2, culturalOfferPage.getTotalElements());
	}

	@Test
	public void testGetByCategoryId_WithNonExistingCategoryId_ShouldReturnCulturalOfferPageWithEmptyContent() {
		Pageable pageable = PageRequest.of(CulturalOfferConstants.PAGE, CulturalOfferConstants.PAGE_SIZE);
		Page<CulturalOffer> culturalOfferPage = culturalOfferRepository.getByCategoryId(CulturalOfferConstants.NON_EXISTING_ID, pageable);

		assertEquals(0, culturalOfferPage.getTotalElements());
	}

	@Test
	public void testFindByCategoryIdAndNameContainingIgnoreCase_WithExistingCategoryIdAndExistingName_ShouldReturnCulturalOfferPage() {
		Pageable pageable = PageRequest.of(CulturalOfferConstants.PAGE, CulturalOfferConstants.PAGE_SIZE);
		Page<CulturalOffer> culturalOfferPage = culturalOfferRepository.findByCategoryIdAndNameContainingIgnoreCase(CulturalOfferConstants.ID1, CulturalOfferConstants.SUBSTRING_NAME, pageable);

		assertEquals(2, culturalOfferPage.getTotalElements());
	}

	@Test
	public void testFindByCategoryIdAndNameContainingIgnoreCase_WithNonExistingName_ShouldReturnCulturalOfferPageWithEmptyContent() {
		Pageable pageable = PageRequest.of(CulturalOfferConstants.PAGE, CulturalOfferConstants.PAGE_SIZE);
		Page<CulturalOffer> culturalOfferPage = culturalOfferRepository.findByCategoryIdAndNameContainingIgnoreCase(CulturalOfferConstants.ID1, CulturalOfferConstants.NAME2, pageable);

		assertEquals(0, culturalOfferPage.getTotalElements());
	}
	
	@Test
	public void testFindByCategoryIdAndNameContainingIgnoreCase_WithNonExistingCategoryId_ShouldReturnCulturalOfferPageWithEmptyContent() {
		Pageable pageable = PageRequest.of(CulturalOfferConstants.PAGE, CulturalOfferConstants.PAGE_SIZE);
		Page<CulturalOffer> culturalOfferPage = culturalOfferRepository.findByCategoryIdAndNameContainingIgnoreCase(CulturalOfferConstants.NON_EXISTING_ID, CulturalOfferConstants.SUBSTRING_NAME, pageable);

		assertEquals(0, culturalOfferPage.getTotalElements());
	}
	
	@Test
	public void testFindByNameContainingIgnoreCase_WithExistingName_ShouldReturnCulturalOfferPage() {
		Pageable pageable = PageRequest.of(CulturalOfferConstants.PAGE, CulturalOfferConstants.PAGE_SIZE);
		Page<CulturalOffer> culturalOfferPage = culturalOfferRepository.findByNameContainingIgnoreCase(CulturalOfferConstants.SUBSTRING_NAME, pageable);

		assertEquals(2, culturalOfferPage.getTotalElements());
	}
	
	@Test
	public void testFindByNameContainingIgnoreCase_WithNonExistingName_ShouldReturnCulturalOfferPageWithEmptyContent() {
		Pageable pageable = PageRequest.of(CulturalOfferConstants.PAGE, CulturalOfferConstants.PAGE_SIZE);
		Page<CulturalOffer> culturalOfferPage = culturalOfferRepository.findByNameContainingIgnoreCase(CulturalOfferConstants.NAME2, pageable);

		assertEquals(0, culturalOfferPage.getTotalElements());
	}
	
	@Test
	public void testFindBySubscribedUserId_WithExistingUserId_ShouldReturnCollectionWithOffers() {

		Pageable pageable = PageRequest.of(CulturalOfferConstants.PAGE, CulturalOfferConstants.PAGE_SIZE);
		Page<CulturalOffer> coPage = culturalOfferRepository.findBySubscribedUsersId(8L, pageable);
		
		assertEquals(3, coPage.getNumberOfElements());
	}
	
	@Test
	public void testFindBySubscribedUserId_WithNonExistingUserId_ShouldReturnCollectionWithOffers() {

		Pageable pageable = PageRequest.of(CulturalOfferConstants.PAGE, CulturalOfferConstants.PAGE_SIZE);
		Page<CulturalOffer> coPage = culturalOfferRepository.findBySubscribedUsersId(899L, pageable);
		
		assertEquals(0, coPage.getNumberOfElements());
	}

}
