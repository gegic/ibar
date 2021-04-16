package com.ktsnwt.project.team9.service.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import com.ktsnwt.project.team9.constants.CommentConstants;
import com.ktsnwt.project.team9.helper.implementations.FileService;
import com.ktsnwt.project.team9.model.Comment;
import com.ktsnwt.project.team9.model.CulturalOffer;
import com.ktsnwt.project.team9.model.Image;
import com.ktsnwt.project.team9.model.RegisteredUser;
import com.ktsnwt.project.team9.services.implementations.CommentService;
import com.ktsnwt.project.team9.services.implementations.ImageService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class CommentServiceIntegrationTest {

	@Autowired
	CommentService commentService;
	
	@Autowired
	ImageService imgService;
	
	@Autowired
	FileService fileService;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Test
	public void testGetAll_ShouldReturnAllCommentss() {
		List<Comment> comments = (List<Comment>) commentService.getAll();
		
		assertEquals(CommentConstants.NUMBER_OF_ITEMS, comments.size());
	}
	
	@Test
	public void testGetById_WithExistingId_ShouldReturnComment() {
		Comment comment = commentService.getById(CommentConstants.COMMENT_ID);
		
		assertNotNull(comment);
		assertEquals(CommentConstants.COMMENT_ID, comment.getId());
	}
	
	@Test
	public void testGetById_WithNonExistingId_ShouldReturnNull() {
		Comment admin = commentService.getById(CommentConstants.NON_EXISTING_COMMENT_ID);
		
		assertNull(admin);
	}
	
	@Test
	public void testFindAllByCOID_WithExistingPageAndExistingId_ShouldReturnCommentPage() {
		Pageable pageable = PageRequest.of(CommentConstants.PAGE, CommentConstants.PAGE_SIZE);
		Page<Comment> page = commentService.findAllByCOID(pageable, CommentConstants.CULTURAL_OFFER_ID);
		
		assertEquals(1, page.getNumberOfElements());
	}
	
	@Test
	public void testFindAllByCOID_WithNonExistingPageAndExistingId_ShouldReturnEmptyCommentPage() {
		Pageable pageable = PageRequest.of(CommentConstants.NON_EXISTING_PAGE, CommentConstants.PAGE_SIZE);
		Page<Comment> page = commentService.findAllByCOID(pageable, CommentConstants.CULTURAL_OFFER_ID);
		
		assertEquals(0, page.getNumberOfElements());
	}
	
	@Test
	public void testFindAllByCOID_WithNonExistingId_ShouldReturnEmptyCommentPage() {
		Pageable pageable = PageRequest.of(CommentConstants.PAGE, CommentConstants.PAGE_SIZE);
		Page<Comment> page = commentService.findAllByCOID(pageable, CommentConstants.NON_EXISTING_CULTURAL_OFFER_ID);
		
		assertEquals(0, page.getNumberOfElements());
	}
	
	@Test
	public void testFindAllByCOID_WithExistingIdButNotWithApprovedComments_ShouldReturnEmptyCommentPage() {
		Pageable pageable = PageRequest.of(CommentConstants.PAGE, CommentConstants.PAGE_SIZE);
		Page<Comment> page = commentService.findAllByCOID(pageable, CommentConstants.CO_ID_NO_APPROVED_COMMENTS);
		
		assertEquals(0, page.getNumberOfElements());
	}
	
	@Test
	public void testFindAllByNotApprovedByAdminId_WithAllCorrectValues_ShouldReturnCommentPage() {
		Pageable pageable = PageRequest.of(CommentConstants.PAGE, CommentConstants.PAGE_SIZE);
		Page<Comment> page = commentService.findAllByNotApprovedByAdminId(pageable, CommentConstants.ADMIN_ID_WITH_NOT_APPROVED_COMMENTS);
		
		assertEquals(2, page.getNumberOfElements());
	}
	
	@Test
	public void testFindAllByNotApprovedByAdminId_WithExistingIdButNonExistingPage_ShouldReturnEmptyCommentPage() {
		Pageable pageable = PageRequest.of(CommentConstants.NON_EXISTING_PAGE, CommentConstants.PAGE_SIZE);
		Page<Comment> page = commentService.findAllByNotApprovedByAdminId(pageable, CommentConstants.ADMIN_ID_WITH_NOT_APPROVED_COMMENTS);
		
		assertEquals(0, page.getNumberOfElements());
	}
	
	@Test
	public void testFindAllByNotApprovedByAdminId_WithNonExistingId_ShouldReturnEmptyCommentPage() {
		Pageable pageable = PageRequest.of(CommentConstants.PAGE, CommentConstants.PAGE_SIZE);
		Page<Comment> page = commentService.findAllByNotApprovedByAdminId(pageable, CommentConstants.NON_EXISTING_CULTURAL_OFFER_USER_ID);
		
		assertEquals(0, page.getNumberOfElements());
	}
	
	@Test
	public void testFindAllByNotApprovedByAdminId_WithExistingIdButHasNotCommentsToApprove_ShouldReturnEmptyCommentPage() {
		Pageable pageable = PageRequest.of(CommentConstants.PAGE, CommentConstants.PAGE_SIZE);
		Page<Comment> page = commentService.findAllByNotApprovedByAdminId(pageable, CommentConstants.CULTURAL_OFFER_USER_ID_NO_COMMENTS_FOR_APPROVING);
		
		assertEquals(0, page.getNumberOfElements());
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testDelete_WithExistingIdAndImage_ShouldReturnTrue() throws Exception {
		List<Comment> comments = (List<Comment>) commentService.getAll();
		int number = comments.size();
		Path path = Paths.get("src/main/resources/uploadedImages/comment_slika6.jpg");
		byte[] content = Files.readAllBytes(path);
		File fileExist = new File(path.toString());
		MultipartFile send = new MockMultipartFile("file", "slika6.jpg", MediaType.IMAGE_JPEG_VALUE, content);
		
		boolean status = commentService.delete(CommentConstants.COMMENT_ID);
		List<Comment> deleted = (List<Comment>) commentService.getAll();
		int afterDeleting = deleted.size();
		
		assertTrue(status);
		assertFalse(fileExist.exists());
		assertEquals(number - 1, afterDeleting);
		
		String img = fileService.saveImage(send, "comment");
		imgService.create(new Image(img));
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testDelete_WithExistingIdAndNoImage_ShouldReturnTrue() throws Exception {
		List<Comment> comments = (List<Comment>) commentService.getAll();
		int number = comments.size();
		
		boolean status = commentService.delete(CommentConstants.COMMENT_ID_NO_IMG);
		
		List<Comment> deleted = (List<Comment>) commentService.getAll();
		int afterDeleting = deleted.size();
		
		assertTrue(status);
		assertEquals(number - 1, afterDeleting);
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testDelete_WithNonExistingId_ShouldThrowNoSuchElementException() throws Exception {
		commentService.delete(CommentConstants.NON_EXISTING_COMMENT_ID);
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testApproveComment_WithAllCorrectValuesForApproving_ShouldReturnComment() throws Exception {
		Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(CommentConstants.ADMIN_EMAIL_THAT_SHOULD_UPDATE_COMMENT, CommentConstants.PASSWORD));
		SecurityContextHolder.getContext().setAuthentication(auth);
		Comment c = commentService.approveComment(CommentConstants.COMMENT_ID_TO_APPROVE, CommentConstants.APPROVED);
		
		assertNotNull(c);
		assertTrue(c.isApproved());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testApproveComment_WithAlreadyApprovedComment_ShouldThrowIllegalArgumentException() throws Exception {
		Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(CommentConstants.ADMIN_EMAIL, CommentConstants.PASSWORD));
		SecurityContextHolder.getContext().setAuthentication(auth);
		Comment c = commentService.approveComment(CommentConstants.COMMENT_ID, CommentConstants.APPROVED);
		
		assertNotNull(c);
		assertTrue(c.isApproved());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testApproveComment_WithNotCorrectLoggedAdmin_ShouldThrowIllegalArgumentException() throws Exception {
		Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(CommentConstants.ADMIN_EMAIL, CommentConstants.PASSWORD));
		SecurityContextHolder.getContext().setAuthentication(auth);
		Comment c = commentService.approveComment(CommentConstants.COMMENT_ID_TO_APPROVE, CommentConstants.APPROVED);
		
		assertNotNull(c);
		assertTrue(c.isApproved());
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testApproveComment_WithAllCorrectValuesForDecliningWithNoImage_ShouldDeleteComment() throws Exception {
		Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(CommentConstants.ADMIN_EMAIL_THAT_SHOULD_UPDATE_COMMENT, CommentConstants.PASSWORD));
		SecurityContextHolder.getContext().setAuthentication(auth);
		List<Comment> comments = (List<Comment>) commentService.getAll();
		int number = comments.size();
		
		commentService.approveComment(CommentConstants.COMMENT_ID_TO_DECLINE, CommentConstants.DECLINED);
		
		List<Comment> deleted = (List<Comment>) commentService.getAll();
		int afterDeleting = deleted.size();
		assertEquals(number - 1, afterDeleting);
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testApproveComment_WithAllCorrectValuesForDecliningWithImage_ShouldDeleteComment() throws Exception {
		Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(CommentConstants.ADMIN_EMAIL_THAT_SHOULD_UPDATE_COMMENT_WITH_IMG, CommentConstants.PASSWORD));
		SecurityContextHolder.getContext().setAuthentication(auth);
		List<Comment> comments = (List<Comment>) commentService.getAll();
		int number = comments.size();
		Path path = Paths.get("src/main/resources/uploadedImages/comment_slika6.jpg");
		byte[] content = Files.readAllBytes(path);
		File fileExist = new File(path.toString());
		MultipartFile send = new MockMultipartFile("file", "slika6.jpg", MediaType.IMAGE_JPEG_VALUE, content);
		
		commentService.approveComment(CommentConstants.COMMENT_ID_TO_DECLINE_WITH_IMAGE, CommentConstants.DECLINED);
		
		List<Comment> deleted = (List<Comment>) commentService.getAll();
		int afterDeleting = deleted.size();
		assertEquals(number - 1, afterDeleting);
		
		String img = fileService.saveImage(send, "comment");
		imgService.create(new Image(img));
		
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testApproveComment_WithNonExistingId_ShouldThrowNoSuchElementException() throws Exception {
		Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(CommentConstants.ADMIN_EMAIL_THAT_SHOULD_UPDATE_COMMENT_WITH_IMG, CommentConstants.PASSWORD));
		SecurityContextHolder.getContext().setAuthentication(auth);
		commentService.approveComment(CommentConstants.NON_EXISTING_COMMENT_ID, CommentConstants.APPROVED);
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testCreate_WithNonExistingCOId_ShouldThrowNoSuchElementException() throws Exception {
		Comment c = new Comment();
		c.setCulturalOffer(new CulturalOffer(CommentConstants.CREATE_NO_CO_ID));
		c.setText(CommentConstants.TEXT);
		
		commentService.create(c, null);
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testCreate_WithValidValuesAndNoImage_ShouldReturnComment() throws Exception {
		List<Comment> comments = (List<Comment>) commentService.getAll();
		int number = comments.size();
		RegisteredUser r = new RegisteredUser(CommentConstants.USER_ID);
		CulturalOffer co = new CulturalOffer(CommentConstants.CULTURAL_OFFER_ID);
		Comment c = new Comment(r, co, CommentConstants.TEXT, CommentConstants.TIME);
		
		Comment comment = commentService.create(c, null);
		
		List<Comment> created = (List<Comment>) commentService.getAll();
		int afterCreating = created.size();
		
		assertNotNull(comment);
		assertEquals(number + 1, afterCreating);
		assertFalse(comment.isApproved());
		assertNull(comment.getImageUrl());
		assertEquals(CommentConstants.TEXT, comment.getText());
		assertEquals(CommentConstants.TIME, comment.getDate());
		assertEquals(CommentConstants.USER_ID, comment.getAuthor().getId());
		assertEquals(CommentConstants.CULTURAL_OFFER_ID, comment.getCulturalOffer().getId());
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testCreate_WithValidValuesAndImage_ShouldReturnComment() throws Exception {
		List<Comment> comments = (List<Comment>) commentService.getAll();
		int number = comments.size();
		RegisteredUser r = new RegisteredUser(CommentConstants.USER_ID);
		CulturalOffer co = new CulturalOffer(CommentConstants.CULTURAL_OFFER_ID);
		Comment c = new Comment(r, co, CommentConstants.TEXT, CommentConstants.TIME);
		String img = "src/main/resources/uploadedImages";
		Path path = Paths.get("src/main/resources/uploadedImages/comment_slika6.jpg");
		byte[] content = Files.readAllBytes(path);
		File fileExist = new File(path.toString());
		MultipartFile send = new MockMultipartFile("file", "slika6.jpg", MediaType.IMAGE_JPEG_VALUE, content);
		
		Comment comment = commentService.create(c, send);
		
		List<Comment> created = (List<Comment>) commentService.getAll();
		int afterCreating = created.size();
		
		assertNotNull(comment);
		assertTrue(fileExist.exists());
		assertEquals(number + 1, afterCreating);
		assertFalse(comment.isApproved());
		assertNotNull(comment.getImageUrl());
		assertEquals(CommentConstants.TEXT, comment.getText());
		assertEquals(CommentConstants.TIME, comment.getDate());
		assertEquals(CommentConstants.USER_ID, comment.getAuthor().getId());
		assertEquals(CommentConstants.CULTURAL_OFFER_ID, comment.getCulturalOffer().getId());
		fileService.deleteImageFromFile(img+"/comment" + CommentConstants.USER_ID + CommentConstants.TIME + "_slika6.jpg");
	}
	
	
}
