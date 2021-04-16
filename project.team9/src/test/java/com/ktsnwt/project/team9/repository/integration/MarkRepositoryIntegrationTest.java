package com.ktsnwt.project.team9.repository.integration;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.constants.MarkConstants;
import com.ktsnwt.project.team9.model.Mark;
import com.ktsnwt.project.team9.repositories.IMarkRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class MarkRepositoryIntegrationTest {

	@Autowired
	IMarkRepository markRepository;
	
	@Test
	public void testFindByGraderAndCulturalOfferId_WithExistingGraderAndCulturalOfferId_ShouldReturnMark() {
		Mark mark = markRepository.findByGraderAndCulturalOfferId(MarkConstants.GRADER_ID, MarkConstants.CULTURAL_OFFER_ID);
		
		assertEquals(MarkConstants.GRADER_ID, mark.getGrader().getId());
		assertEquals(MarkConstants.CULTURAL_OFFER_ID, mark.getCulturalOffer().getId());
	}
	
	@Test
	public void testFindByGraderAndCulturalOfferId_WithExistingGraderAndNonExistingCulturalOfferId_ShouldReturnNull() {
		Mark mark = markRepository.findByGraderAndCulturalOfferId(MarkConstants.GRADER_ID, MarkConstants.NON_EXISTING_CULTURAL_OFFER_ID);
		
		assertEquals(null, mark);
	}
	
	@Test
	public void testFindByGraderAndCulturalOfferId_WithNonExistingGraderAndCulturalOfferId_ShouldReturnNull() {
		Mark mark = markRepository.findByGraderAndCulturalOfferId(MarkConstants.NON_EXISTING_GRADER_ID, MarkConstants.CULTURAL_OFFER_ID);
		
		assertEquals(null, mark);
	}
	
	@Test
	public void testFindByGraderAndCulturalOfferId_WithNonExistingGraderAndNonExistingCulturalOfferId_ShouldReturnNull() {
		Mark mark = markRepository.findByGraderAndCulturalOfferId(MarkConstants.NON_EXISTING_GRADER_ID, MarkConstants.NON_EXISTING_CULTURAL_OFFER_ID);
		
		assertEquals(null, mark);
	}
}
