package com.ktsnwt.project.team9.service.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.constants.CommentConstants;
import com.ktsnwt.project.team9.helper.implementations.FileService;
import com.ktsnwt.project.team9.model.Admin;
import com.ktsnwt.project.team9.model.Comment;
import com.ktsnwt.project.team9.model.CulturalOffer;
import com.ktsnwt.project.team9.model.Image;
import com.ktsnwt.project.team9.model.User;
import com.ktsnwt.project.team9.repositories.ICommentRepository;
import com.ktsnwt.project.team9.services.implementations.CommentService;
import com.ktsnwt.project.team9.services.implementations.CulturalOfferService;
import com.ktsnwt.project.team9.services.implementations.ImageService;
import com.ktsnwt.project.team9.services.implementations.RegisteredUserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class CommentServiceUnitTest {
	
	@Autowired
	CommentService commentService;
	
	@MockBean
	FileService fileService;
	
	@MockBean
	ImageService imageService;
	
	@MockBean
	CulturalOfferService culturalOfferService;
	
	@MockBean
	ICommentRepository commentRepository;
	
	@MockBean
	RegisteredUserService regService;
	
	@Before
	public void setup() throws Exception {
		Comment c = new Comment(CommentConstants.REG_USER, CommentConstants.CULTURAL_OFFER, CommentConstants.TEXT, CommentConstants.TIME);
		c.setApproved(CommentConstants.DECLINED);
		c.setImageUrl(CommentConstants.IMAGE);

		Comment savedc = new Comment(CommentConstants.REG_USER, CommentConstants.CULTURAL_OFFER, CommentConstants.TEXT, CommentConstants.TIME);
		savedc.setId(CommentConstants.COMMENT_ID);
		savedc.setApproved(CommentConstants.DECLINED);
		savedc.setImageUrl(CommentConstants.IMAGE);

		Comment approvedc = new Comment(CommentConstants.REG_USER, CommentConstants.CULTURAL_OFFER, CommentConstants.TEXT, CommentConstants.TIME);
		approvedc.setId(CommentConstants.COMMENT_ID);
		approvedc.setApproved(CommentConstants.APPROVED);
		approvedc.setImageUrl(CommentConstants.IMAGE);
		
		Comment c2 = new Comment(CommentConstants.REG_USER, CommentConstants.CULTURAL_OFFER2, CommentConstants.TEXT, CommentConstants.TIME);
		c2.setId(CommentConstants.COMMENT_ID2);
		c2.setApproved(CommentConstants.DECLINED);
		c2.setImageUrl(null);
	
		Comment c3 = new Comment(CommentConstants.REG_USER, CommentConstants.CULTURAL_OFFER, CommentConstants.TEXT, CommentConstants.TIME);
		c3.setApproved(CommentConstants.DECLINED);
		c3.setImageUrl(null);
		
		Comment savedc3 = new Comment(CommentConstants.REG_USER, CommentConstants.CULTURAL_OFFER, CommentConstants.TEXT, CommentConstants.TIME);
		savedc3.setId(CommentConstants.COMMENT_ID3);
		savedc3.setApproved(CommentConstants.DECLINED);
		savedc3.setImageUrl(null);
		
		CulturalOffer co = CommentConstants.CULTURAL_OFFER;
		co.setAdmin(new Admin(CommentConstants.ADMIN_ID_WITH_NOT_APPROVED_COMMENTS));
		Comment c4 = new Comment(CommentConstants.REG_USER, co, CommentConstants.TEXT, CommentConstants.TIME);
		c4.setId(CommentConstants.COMMENT_ID4);
		c4.setApproved(CommentConstants.APPROVED);
		c4.setImageUrl(null);
	
		Path path = Paths.get("src/test/resources/uploadedImages/slika2.jpg");
		byte[] content = Files.readAllBytes(path);
		MockMultipartFile file2 = new MockMultipartFile("file", "slika2.jpg", MediaType.IMAGE_JPEG_VALUE, content);
		User forApproveCorrect = new User(CommentConstants.ADMIN_ID_WITH_NOT_APPROVED_COMMENTS);
		Authentication auth = new Authentication() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getName() {
				return null;
			}
			
			@Override
			public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
				
			}
			
			@Override
			public boolean isAuthenticated() {
				return false;
			}
			
			@Override
			public Object getPrincipal() {
				return forApproveCorrect;
			}
			
			@Override
			public Object getDetails() {
				return null;
			}
			
			@Override
			public Object getCredentials() {
				return null;
			}
			
			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				return null;
			}
		};
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		given(commentRepository.save(c)).willReturn(savedc);
		given(commentRepository.save(approvedc)).willReturn(approvedc);
		given(commentRepository.save(c3)).willReturn(savedc3);
		
		given(commentRepository.findById(CommentConstants.COMMENT_ID)).willReturn(Optional.of(savedc));
		given(commentRepository.findById(CommentConstants.COMMENT_ID4)).willReturn(Optional.of(c4));
		given(commentRepository.findById(CommentConstants.COMMENT_ID3)).willReturn(Optional.of(savedc3));
		given(commentRepository.findById(CommentConstants.COMMENT_ID2)).willReturn(Optional.of(c2));
		given(commentRepository.findById(CommentConstants.NON_EXISTING_COMMENT_ID)).willReturn(Optional.empty());
		
		given(culturalOfferService.getById(CommentConstants.CULTURAL_OFFER_ID)).willReturn(CommentConstants.CULTURAL_OFFER);
		given(culturalOfferService.getById(CommentConstants.NON_EXISTING_CULTURAL_OFFER_ID)).willReturn(null);
		given(culturalOfferService.getById(CommentConstants.NON_EXISTING_CULTURAL_OFFER_ID)).willReturn(null);
		
		given(imageService.create(CommentConstants.IMAGE)).willReturn(CommentConstants.IMAGE);
		given(imageService.create(CommentConstants.CREATED_IMG)).willReturn(CommentConstants.CREATED_IMG);
		
		given(fileService.saveImage(file2, "comment" + CommentConstants.USER_ID + CommentConstants.TIME)).willReturn(CommentConstants.IMAGE_PATH);
		doNothing().when(fileService).deleteImageFromFile(Mockito.any());
		doNothing().when(commentRepository).delete(CommentConstants.COMMENT_ID);
		doNothing().when(commentRepository).delete(CommentConstants.COMMENT_ID2);
	}
	
	@Test
	public void testGetById_WithExistingId_ShouldReturnComment() {
		Comment c = commentService.getById(CommentConstants.COMMENT_ID);
		
		verify(commentRepository, times(1)).findById(CommentConstants.COMMENT_ID);
		assertEquals(CommentConstants.COMMENT_ID, c.getId());
	}
	
	@Test
	public void testGetById_WithNonExistingId_ShouldReturnNUll() {
		Comment c = commentService.getById(CommentConstants.NON_EXISTING_COMMENT_ID);
		
		verify(commentRepository, times(1)).findById(CommentConstants.NON_EXISTING_COMMENT_ID);
		assertEquals(null, c);
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testCreate_WithNonExistingCulturalId_ShouldThrowNoSuchElementException() throws Exception {
		Comment c = new Comment(CommentConstants.REG_USER, CommentConstants.NON_EXISTING_CULTURAL_OFFER, CommentConstants.TEXT, CommentConstants.TIME);
		MockMultipartFile file = new MockMultipartFile("file", "slika2.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[0]);
		
		commentService.create(c, file);
		verify(culturalOfferService, times(1)).getById(CommentConstants.NON_EXISTING_CULTURAL_OFFER_ID);
		verify(imageService, times(0)).create(Mockito.any(Image.class));
		verify(fileService, times(0)).saveImage(file, "slika2");
	}
	
	@Test
	public void testCreate_WithFileNull_ShouldCreateComment() throws Exception {
		Comment c = new Comment(CommentConstants.REG_USER, CommentConstants.CULTURAL_OFFER, CommentConstants.TEXT, CommentConstants.TIME); //koment 3
		c.setApproved(CommentConstants.DECLINED);
		c.setImageUrl(null);
		MockMultipartFile file = new MockMultipartFile("file", "slika2.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[0]);
		
		Comment newC = commentService.create(c, null);
		verify(culturalOfferService, times(1)).getById(CommentConstants.CULTURAL_OFFER_ID);
		verify(imageService, times(0)).create(Mockito.any(Image.class));
		verify(fileService, times(0)).saveImage(file, "slika2");
		assertFalse(c.isApproved());
		assertEquals(CommentConstants.USER_ID, newC.getAuthor().getId());
		assertEquals(CommentConstants.CULTURAL_OFFER_ID, newC.getCulturalOffer().getId());
		assertEquals(CommentConstants.TEXT, newC.getText());
		assertEquals(CommentConstants.TIME, newC.getDate());
		assertEquals(null, newC.getImageUrl());
	}
	
	@Test
	public void testCreate_WithAllValues_ShouldCreateComment() throws Exception {
		Comment c = new Comment(CommentConstants.REG_USER, CommentConstants.CULTURAL_OFFER, CommentConstants.TEXT, CommentConstants.TIME); //koment 1
		c.setApproved(CommentConstants.DECLINED);
		c.setImageUrl(CommentConstants.IMAGE);
		Path path = Paths.get("src/test/resources/uploadedImages/slika2.jpg");
		byte[] content = Files.readAllBytes(path);
		MockMultipartFile file = new MockMultipartFile("file", "slika2.jpg", MediaType.IMAGE_JPEG_VALUE, content);
		given(fileService.saveImage(file, "comment" + CommentConstants.USER_ID + CommentConstants.TIME)).willReturn(CommentConstants.IMAGE_PATH);
		
		commentService.create(c, file);
		verify(culturalOfferService, times(1)).getById(CommentConstants.CULTURAL_OFFER_ID);
		verify(fileService, times(1)).saveImage(file, "comment" + CommentConstants.USER_ID + CommentConstants.TIME);
		verify(imageService, times(1)).create(CommentConstants.CREATED_IMG);
		assertFalse(c.isApproved());
		assertEquals(CommentConstants.USER_ID, c.getAuthor().getId());
		assertEquals(CommentConstants.CULTURAL_OFFER_ID, c.getCulturalOffer().getId());
		assertEquals(CommentConstants.TEXT, c.getText());
		assertEquals(CommentConstants.TIME, c.getDate());
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testDelete_WithNonExistingId_ShouldThrowNoSuchElementException() throws Exception {
		commentService.delete(CommentConstants.NON_EXISTING_COMMENT_ID);
		
		verify(commentRepository, times(1)).findById(CommentConstants.NON_EXISTING_COMMENT_ID);
		verify(commentRepository, times(0)).delete(CommentConstants.NON_EXISTING_COMMENT_ID);
	}
	
	@Test
	public void testDelete_WithExistingIdAndImage_ShouldDeleteComment() throws Exception {
		boolean status = commentService.delete(CommentConstants.COMMENT_ID);
		
		verify(commentRepository, times(1)).findById(CommentConstants.COMMENT_ID);
		verify(fileService, times(1)).deleteImageFromFile(CommentConstants.IMAGEURL);
		verify(commentRepository, times(1)).delete(CommentConstants.COMMENT_ID);
		assertTrue(status);
	}
	
	@Test
	public void testDelete_WithExistingIdAndNoImage_ShouldDeleteComment() throws Exception {
		boolean status = commentService.delete(CommentConstants.COMMENT_ID2);
		
		verify(commentRepository, times(1)).findById(CommentConstants.COMMENT_ID2);
		verify(fileService, times(0)).deleteImageFromFile(CommentConstants.IMAGEURL);
		verify(commentRepository, times(1)).delete(CommentConstants.COMMENT_ID2);
		assertTrue(status);
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testApproveComment_WithNonExistingId_ShouldThrowNoSuchElementException() throws Exception {
		commentService.approveComment(CommentConstants.NON_EXISTING_COMMENT_ID, CommentConstants.DECLINED);
		
		
		verify(commentRepository, times(1)).findById(CommentConstants.NON_EXISTING_COMMENT_ID);
		verify(commentRepository, times(0)).save(Mockito.any(Comment.class));
		verify(commentRepository, times(0)).delete(CommentConstants.NON_EXISTING_COMMENT_ID);
	}
	
	@Test
	public void testApproveComment_WithExistingId_ShouldApproveComment() throws Exception {
		CulturalOffer co = CommentConstants.CULTURAL_OFFER;
		co.setAdmin(new Admin(CommentConstants.ADMIN_ID_WITH_NOT_APPROVED_COMMENTS));
		Comment c = new Comment(CommentConstants.REG_USER, co, CommentConstants.TEXT, CommentConstants.TIME); //koment 1
		c.setId(CommentConstants.COMMENT_ID);
		c.setApproved(CommentConstants.DECLINED);
		c.setImageUrl(CommentConstants.IMAGE);
		Comment c1 = new Comment(CommentConstants.REG_USER, CommentConstants.CULTURAL_OFFER, CommentConstants.TEXT, CommentConstants.TIME); //koment 1
		c1.setId(CommentConstants.COMMENT_ID);
		c1.setApproved(CommentConstants.APPROVED);
		c1.setImageUrl(CommentConstants.IMAGE);
		
		Comment approved = commentService.approveComment(CommentConstants.COMMENT_ID, CommentConstants.APPROVED);
	
		verify(commentRepository, times(1)).findById(CommentConstants.COMMENT_ID);
		verify(commentRepository, times(0)).delete(CommentConstants.COMMENT_ID);
		verify(commentRepository, times(1)).save(c1);
		assertTrue(approved.isApproved());
	}
	
	@Test
	public void testApproveComment_WithExistingId_ShouldDeclineComment() throws Exception {
		CulturalOffer co = CommentConstants.CULTURAL_OFFER2;
		co.setAdmin(new Admin(CommentConstants.ADMIN_ID_WITH_NOT_APPROVED_COMMENTS));
		Comment c2 = new Comment(CommentConstants.REG_USER, co, CommentConstants.TEXT, CommentConstants.TIME); // koment 2
		c2.setId(CommentConstants.COMMENT_ID2);
		c2.setApproved(CommentConstants.DECLINED);
		c2.setImageUrl(null);
		
		Comment declined = commentService.approveComment(CommentConstants.COMMENT_ID2, CommentConstants.DECLINED);
		
		verify(commentRepository, times(2)).findById(CommentConstants.COMMENT_ID2);
		verify(commentRepository, times(0)).save(c2);
		verify(commentRepository, times(1)).delete(CommentConstants.COMMENT_ID2);
		assertEquals(null, declined);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testApproveComment_WithAlreadyApprovedComment_ShouldThrowIllegalArgumentException() throws Exception {
		commentService.approveComment(CommentConstants.COMMENT_ID4, CommentConstants.DECLINED);
		
		verify(commentRepository, times(1)).findById(CommentConstants.COMMENT_ID4);
		verify(commentRepository, times(0)).save(Mockito.any(Comment.class));
		verify(commentRepository, times(0)).delete(CommentConstants.COMMENT_ID2);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testApproveComment_WithNotCorrectAdmin_ShouldThrowIllegalArgumentException() throws Exception {
		User user = new User(CommentConstants.ADMIN_ID_WITH_NOT_APPROVED_COMMENTS);
		Authentication auth = new Authentication() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getName() {
				return null;
			}
			
			@Override
			public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
				
			}
			
			@Override
			public boolean isAuthenticated() {
				return false;
			}
			
			@Override
			public Object getPrincipal() {
				return user;
			}
			
			@Override
			public Object getDetails() {
				return null;
			}
			
			@Override
			public Object getCredentials() {
				return null;
			}
			
			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				return null;
			}
		};
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		commentService.approveComment(CommentConstants.COMMENT_ID4, CommentConstants.DECLINED);
		
		verify(commentRepository, times(1)).findById(CommentConstants.COMMENT_ID4);
		verify(commentRepository, times(0)).save(Mockito.any(Comment.class));
		verify(commentRepository, times(0)).delete(CommentConstants.COMMENT_ID2);
	}

}
