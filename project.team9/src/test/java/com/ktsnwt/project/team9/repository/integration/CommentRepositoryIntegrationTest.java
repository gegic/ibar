package com.ktsnwt.project.team9.repository.integration;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.constants.CommentConstants;
import com.ktsnwt.project.team9.model.Comment;
import com.ktsnwt.project.team9.repositories.ICommentRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class CommentRepositoryIntegrationTest {
	
	@Autowired
	ICommentRepository commentRepository;
	
	@Test
	public void testFindByCulturalOfferIdAndApproved_WithExistingCulturalOfferIdAndApprovedTrue_ShouldReturnCommentPage() {
		Pageable pageable = PageRequest.of(CommentConstants.PAGE, CommentConstants.PAGE_SIZE);
		Page<Comment> commentPage = commentRepository.findByCulturalOfferIdAndApproved(CommentConstants.CULTURAL_OFFER_ID, CommentConstants.APPROVED, pageable);
		
		assertEquals(1, commentPage.getTotalElements());
	}
	
	@Test
	public void testFindByCulturalOfferIdAndApproved_WithExistingCulturalOfferIdAndApprovedFalse_ShouldReturnCommentPageWithEmptyContent() {
		Pageable pageable = PageRequest.of(CommentConstants.PAGE, CommentConstants.PAGE_SIZE);
		Page<Comment> commentPage = commentRepository.findByCulturalOfferIdAndApproved(CommentConstants.CULTURAL_OFFER_ID, CommentConstants.DECLINED, pageable);
		
		assertEquals(0, commentPage.getTotalElements());
	}
	
	@Test
	public void testFindByCulturalOfferIdAndApproved_WithNonExistingCulturalOfferId_ShouldReturnCommentPageWithEmptyContent() {
		Pageable pageable = PageRequest.of(CommentConstants.PAGE, CommentConstants.PAGE_SIZE);
		Page<Comment> commentPage = commentRepository.findByCulturalOfferIdAndApproved(CommentConstants.NON_EXISTING_CULTURAL_OFFER_ID, CommentConstants.APPROVED, pageable);
		
		assertEquals(0, commentPage.getTotalElements());
	}
	
	@Test
	public void testFindByApprovedFalseAndCulturalOfferUserId_WithExistingCulturalOfferUserId_ShouldReturnCommentPage() {
		Pageable pageable = PageRequest.of(CommentConstants.PAGE, CommentConstants.PAGE_SIZE);
		Page<Comment> commentPage = commentRepository.findByApprovedFalseAndCulturalOfferUserId(CommentConstants.CULTURAL_OFFER_USER_ID, pageable);
		
		assertEquals(1, commentPage.getTotalElements());
	}
	
	@Test
	public void testFindByApprovedFalseAndCulturalOfferUserId_WithExistingCulturalOfferUserIdWithNoCommentsForApproving_ShouldReturnCommentPageWithNoContent() {
		Pageable pageable = PageRequest.of(CommentConstants.PAGE, CommentConstants.PAGE_SIZE);
		Page<Comment> commentPage = commentRepository.findByApprovedFalseAndCulturalOfferUserId(CommentConstants.CULTURAL_OFFER_USER_ID_NO_COMMENTS_FOR_APPROVING, pageable);
		
		assertEquals(0, commentPage.getTotalElements());
	}
	
	@Test
	public void testFindByApprovedFalseAndCulturalOfferUserId_WithNonExistingCulturalOfferUserId_ShouldReturnCommentPageWithEmptyContent() {
		Pageable pageable = PageRequest.of(CommentConstants.PAGE, CommentConstants.PAGE_SIZE);
		Page<Comment> commentPage = commentRepository.findByApprovedFalseAndCulturalOfferUserId(CommentConstants.NON_EXISTING_CULTURAL_OFFER_USER_ID, pageable);
		
		assertEquals(0, commentPage.getTotalElements());
	}
	
	@Test
	public void testDelete_WithExistingId_ShouldDeleteComment() {
		Optional<Comment> c = commentRepository.findById(CommentConstants.COMMENT_ID);
		commentRepository.delete(CommentConstants.COMMENT_ID);
		List<Comment> comments = commentRepository.findAll();
		int newSize = comments.size();
		
		assertEquals(CommentConstants.NUMBER_OF_ITEMS - 1, newSize);
		
		commentRepository.save(c.get());
		assertEquals(CommentConstants.NUMBER_OF_ITEMS, commentRepository.findAll().size());
	}
	
	@Test
	public void testDelete_WithNonExistingId_ShouldDeleteNothing() {
		commentRepository.delete(CommentConstants.NON_EXISTING_COMMENT_ID);
		List<Comment> comments = commentRepository.findAll();
		int newSize = comments.size();
		
		assertEquals(CommentConstants.NUMBER_OF_ITEMS, newSize);
	}

}
