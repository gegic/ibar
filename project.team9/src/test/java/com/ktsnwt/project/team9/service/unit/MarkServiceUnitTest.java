package com.ktsnwt.project.team9.service.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.constants.MarkConstants;
import com.ktsnwt.project.team9.model.Mark;
import com.ktsnwt.project.team9.repositories.ICulturalOfferRepository;
import com.ktsnwt.project.team9.repositories.IMarkRepository;
import com.ktsnwt.project.team9.services.implementations.CulturalOfferService;
import com.ktsnwt.project.team9.services.implementations.MarkBookService;
import com.ktsnwt.project.team9.services.implementations.RegisteredUserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class MarkServiceUnitTest {
	
	@Autowired
	MarkBookService markService;

	@MockBean
	IMarkRepository markRepository;
	
	@MockBean
	ICulturalOfferRepository culturalOfferRepository;
	
	@MockBean
	private CulturalOfferService culturalOfferService;
	
	@MockBean
	private RegisteredUserService rUserService;

	@Before
	public void setup() throws Exception {
		Mark mark = new Mark(MarkConstants.MARK_ID, MarkConstants.VALUE, MarkConstants.GRADER, MarkConstants.CULTURAL_OFFER);
		Mark newMark = new Mark(MarkConstants.VALUE, MarkConstants.GRADER, MarkConstants.CULTURAL_OFFER);
		
		given(culturalOfferService.getById(MarkConstants.CULTURAL_OFFER_ID)).willReturn(MarkConstants.CULTURAL_OFFER);
		given(culturalOfferService.getById(MarkConstants.NON_EXISTING_CULTURAL_OFFER_ID)).willReturn(null);
		
		given(rUserService.getById(MarkConstants.GRADER_ID)).willReturn(MarkConstants.GRADER);
		given(rUserService.getById(MarkConstants.NON_EXISTING_GRADER_ID)).willReturn(null);

		given(markRepository.findById(MarkConstants.MARK_ID)).willReturn(Optional.of(mark));		
		given(markRepository.findById(MarkConstants.NON_EXISTING_MARK_ID)).willReturn(Optional.empty());
		
		given(markRepository.findByGraderAndCulturalOfferId(MarkConstants.GRADER_ID, MarkConstants.CULTURAL_OFFER_ID)).willReturn(mark);
		given(markRepository.findByCulturalOfferId(MarkConstants.CULTURAL_OFFER_ID)).willReturn(new ArrayList<Mark>());
		
		given(markRepository.save(newMark)).willReturn(mark);
		given(markRepository.save(mark)).willReturn(mark);
		given(culturalOfferRepository.save(Mockito.any())).willReturn(null);
	}
	
	@Test
	public void testGetById_WithExistingId_ShouldReturnMark() {
		Mark m = markService.getById(MarkConstants.MARK_ID);
		
		verify(markRepository, times(1)).findById(MarkConstants.MARK_ID);
		assertEquals(MarkConstants.MARK_ID, m.getId());
	}
	
	@Test
	public void testGetById_WithNonExistingId_ShouldReturnNull() {
		Mark m = markService.getById(MarkConstants.NON_EXISTING_MARK_ID);
		
		verify(markRepository, times(1)).findById(MarkConstants.NON_EXISTING_MARK_ID);
		assertNull(m);
	}
	
	@Test
	public void testFindByUserIdAndCulturalOfferId_WithExistingUserIdAndExistingCulturalOfferId_ShouldReturnMark() {
		Mark m = markService.findByUserIdAndCulturalOfferId(MarkConstants.GRADER_ID, MarkConstants.CULTURAL_OFFER_ID);
		
		verify(markRepository, times(1)).findByGraderAndCulturalOfferId(MarkConstants.GRADER_ID, MarkConstants.CULTURAL_OFFER_ID);
		assertEquals(MarkConstants.GRADER_ID, m.getGrader().getId());
		assertEquals(MarkConstants.CULTURAL_OFFER_ID, m.getCulturalOffer().getId());
	}
	
	@Test
	public void testCreate_WithExistingGraderIdAndExistingCulturalOfferId_ShouldCreateMark() throws Exception {
		Mark newMark = new Mark(MarkConstants.VALUE, MarkConstants.GRADER, MarkConstants.CULTURAL_OFFER);
		
		Mark m = markService.create(newMark);
		verify(culturalOfferService, times(1)).getById(MarkConstants.CULTURAL_OFFER_ID);
		verify(rUserService, times(1)).getById(MarkConstants.GRADER_ID);
		assertEquals(MarkConstants.GRADER_ID, m.getGrader().getId());
		assertEquals(MarkConstants.CULTURAL_OFFER_ID, m.getCulturalOffer().getId());
		assertEquals(MarkConstants.VALUE, m.getValue());
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testCreate_WithNonExistingGraderIdAndNonExistingCulturalOfferId_ShouldThrowNoSuchElementException() throws Exception {
		Mark newMark = new Mark(MarkConstants.VALUE, MarkConstants.NON_EXISTING_GRADER, MarkConstants.NON_EXISTING_CULTURAL_OFFER);
		
		markService.create(newMark);
		verify(culturalOfferService, times(0)).getById(MarkConstants.CULTURAL_OFFER_ID);
		verify(rUserService, times(1)).getById(MarkConstants.NON_EXISTING_GRADER_ID);
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testCreate_WithNonExistingGraderIdAndExistingCulturalOfferId_ShouldThrowNoSuchElementException() throws Exception {
		Mark newMark = new Mark(MarkConstants.VALUE, MarkConstants.NON_EXISTING_GRADER, MarkConstants.CULTURAL_OFFER);
		
		markService.create(newMark);
		verify(culturalOfferService, times(0)).getById(MarkConstants.CULTURAL_OFFER_ID);
		verify(rUserService, times(1)).getById(MarkConstants.NON_EXISTING_GRADER_ID);
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testCreate_WithExistingGraderIdAndNonExistingCulturalOfferId_ShouldThrowNoSuchElementException() throws Exception {
		Mark newMark = new Mark(MarkConstants.VALUE, MarkConstants.GRADER, MarkConstants.NON_EXISTING_CULTURAL_OFFER);
		
		markService.create(newMark);
		verify(rUserService, times(0)).getById(MarkConstants.NON_EXISTING_GRADER_ID);
		verify(culturalOfferService, times(1)).getById(MarkConstants.CULTURAL_OFFER_ID);
	}
	
	@Test
	public void testUpdate_WithExistingMarkAndExistingGraderIdAndExistingCulturalOfferId_ShouldUpdateMark() throws Exception {
		Mark updatedMark = new Mark(MarkConstants.MARK_ID, MarkConstants.VALUE_UPDATED, MarkConstants.GRADER, MarkConstants.CULTURAL_OFFER);
		
		Mark m = markService.update(MarkConstants.GRADER_ID, updatedMark);
		verify(markRepository, times(1)).findByGraderAndCulturalOfferId(MarkConstants.GRADER_ID, MarkConstants.CULTURAL_OFFER_ID);
		verify(culturalOfferService, times(1)).getById(MarkConstants.CULTURAL_OFFER_ID);
		verify(rUserService, times(1)).getById(MarkConstants.GRADER_ID);
		assertEquals(MarkConstants.GRADER_ID, m.getGrader().getId());
		assertEquals(MarkConstants.CULTURAL_OFFER_ID, m.getCulturalOffer().getId());
		assertEquals(MarkConstants.VALUE_UPDATED, m.getValue());
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testUpdate_WithNonExistingMarkAndExistingGraderIdAndExistingCulturalOfferId_ShouldThrowNoSuchElementException() throws Exception {
		Mark updatedMark = new Mark(MarkConstants.MARK_ID, MarkConstants.VALUE_UPDATED, MarkConstants.GRADER2, MarkConstants.CULTURAL_OFFER);
		
		markService.update(MarkConstants.GRADER_ID2, updatedMark);
		verify(markService, times(1)).findByUserIdAndCulturalOfferId(MarkConstants.GRADER_ID, MarkConstants.CULTURAL_OFFER_ID);
		verify(culturalOfferService, times(0)).getById(MarkConstants.CULTURAL_OFFER_ID);
		verify(rUserService, times(0)).getById(MarkConstants.GRADER_ID);
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testUpdate_WithNonExistingMarkAndNonExistingGraderIdAndExistingCulturalOfferId_ShouldThrowNoSuchElementException() throws Exception {
		Mark updatedMark = new Mark(MarkConstants.MARK_ID, MarkConstants.VALUE_UPDATED, MarkConstants.NON_EXISTING_GRADER, MarkConstants.CULTURAL_OFFER);
		
		markService.update(MarkConstants.NON_EXISTING_GRADER_ID, updatedMark);
		verify(markService, times(1)).findByUserIdAndCulturalOfferId(MarkConstants.NON_EXISTING_GRADER_ID, MarkConstants.CULTURAL_OFFER_ID);
		verify(culturalOfferService, times(0)).getById(MarkConstants.CULTURAL_OFFER_ID);
		verify(rUserService, times(0)).getById(MarkConstants.NON_EXISTING_GRADER_ID);
	}
	
}
