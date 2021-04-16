package com.ktsnwt.project.team9.service.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.NoSuchElementException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.constants.MarkConstants;
import com.ktsnwt.project.team9.model.Mark;
import com.ktsnwt.project.team9.services.implementations.MarkBookService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class MarkServiceIntegrationTest {

	@Autowired
	MarkBookService markService;
	
	@Test
	public void testGetById_WithExistingId_ShouldReturnMark() {
		Mark mark = markService.getById(MarkConstants.MARK_ID);
		
		assertNotNull(mark);
		assertEquals(MarkConstants.MARK_ID, mark.getId());
	}
	
	@Test
	public void testGetById_WithNonExistingId_ShouldReturnNull() {
		Mark mark = markService.getById(MarkConstants.NON_EXISTING_MARK_ID);
		
		assertNull(mark);
	}
	
	@Test
	public void testfindByUserIdAndCulturalOfferId_WithAllCorrectValues_ShouldReturnMark() {
		Mark mark = markService.findByUserIdAndCulturalOfferId(MarkConstants.GRADER_ID, MarkConstants.CULTURAL_OFFER_ID);
		
		assertNotNull(mark);
		assertEquals(MarkConstants.GRADER_ID, mark.getGrader().getId());
		assertEquals(MarkConstants.CULTURAL_OFFER_ID, mark.getCulturalOffer().getId());
	}
	
	@Test
	public void testfindByUserIdAndCulturalOfferId_WithNonExistingUserIdAndExistingCulturalOfferId_ShouldReturnMark() {
		Mark mark = markService.findByUserIdAndCulturalOfferId(MarkConstants.NON_EXISTING_GRADER_ID, MarkConstants.CULTURAL_OFFER_ID);
		
		assertNull(mark);
	}
	
	@Test
	public void testfindByUserIdAndCulturalOfferId_WithExistingUserIdAndNonExistingCulturalOfferId_ShouldReturnMark() {
		Mark mark = markService.findByUserIdAndCulturalOfferId(MarkConstants.GRADER_ID, MarkConstants.NON_EXISTING_CULTURAL_OFFER_ID);
		
		assertNull(mark);
	}
	
	@Test
	public void testCreate_WithAllCorrectValues_ShouldCreateMark() throws Exception {
		Mark newMark = new Mark(MarkConstants.VALUE, MarkConstants.GRADER, MarkConstants.CULTURAL_OFFER);
		Mark mark = markService.create(newMark);
		
		assertNotNull(mark);
		assertEquals(MarkConstants.VALUE, mark.getValue());
		assertEquals(MarkConstants.GRADER_ID, mark.getGrader().getId());
		assertEquals(MarkConstants.CULTURAL_OFFER_ID, mark.getCulturalOffer().getId());
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testCreate_WithNonExistingUserIdAndExistingCulturalOfferId_ShouldThrowNoSuchElementException() throws Exception {
		Mark newMark = new Mark(MarkConstants.VALUE, MarkConstants.NON_EXISTING_GRADER, MarkConstants.CULTURAL_OFFER);
		Mark mark = markService.create(newMark);
		
		assertNull(mark);
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testCreate_WithExistingUserIdAndNonExistingCulturalOfferId_ShouldThrowNoSuchElementException() throws Exception {
		Mark newMark = new Mark(MarkConstants.VALUE, MarkConstants.GRADER, MarkConstants.NON_EXISTING_CULTURAL_OFFER);
		Mark mark = markService.create(newMark);
		
		assertNull(mark);
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testCreate_WithNonExistingUserIdAndNonExistingCulturalOfferId_ShouldThrowNoSuchElementException() throws Exception {
		Mark newMark = new Mark(MarkConstants.VALUE, MarkConstants.NON_EXISTING_GRADER, MarkConstants.NON_EXISTING_CULTURAL_OFFER);
		Mark mark = markService.create(newMark);
		
		assertNull(mark);
	}
	
	@Test
	public void testUpdate_WithAllCorrectValues_ShouldUpdateMark() throws Exception {
		Mark newMark = new Mark(MarkConstants.MARK_ID, MarkConstants.VALUE_UPDATED, MarkConstants.GRADER, MarkConstants.CULTURAL_OFFER);
		Mark mark = markService.update(MarkConstants.GRADER_ID, newMark);
		
		assertNotNull(mark);
		assertEquals(MarkConstants.VALUE_UPDATED, mark.getValue());
		assertEquals(MarkConstants.GRADER_ID, mark.getGrader().getId());
		assertEquals(MarkConstants.CULTURAL_OFFER_ID, mark.getCulturalOffer().getId());
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testUpdate_WithNonExistingUserIdAndExistingCulturalOfferId_ShouldThrowNoSuchElementException() throws Exception {
		Mark newMark = new Mark(MarkConstants.VALUE, MarkConstants.NON_EXISTING_GRADER, MarkConstants.CULTURAL_OFFER);
		Mark mark = markService.update(MarkConstants.NON_EXISTING_GRADER_ID, newMark);
		
		assertNull(mark);
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testUpdate_WithExistingUserIdAndNonExistingCulturalOfferId_ShouldThrowNoSuchElementException() throws Exception {
		Mark newMark = new Mark(MarkConstants.VALUE, MarkConstants.GRADER, MarkConstants.NON_EXISTING_CULTURAL_OFFER);
		Mark mark = markService.update(MarkConstants.GRADER_ID, newMark);
		
		assertNull(mark);
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testUpdate_WithNonExistingUserIdAndNonExistingCulturalOfferId_ShouldThrowNoSuchElementException() throws Exception {
		Mark newMark = new Mark(MarkConstants.VALUE, MarkConstants.NON_EXISTING_GRADER, MarkConstants.NON_EXISTING_CULTURAL_OFFER);
		Mark mark = markService.update(MarkConstants.NON_EXISTING_GRADER_ID, newMark);
		
		assertNull(mark);
	}
	
}
