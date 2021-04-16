package com.ktsnwt.project.team9.service.integration;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.helper.implementations.NewsMapper;
import com.ktsnwt.project.team9.model.News;
import com.ktsnwt.project.team9.model.Image;
import com.ktsnwt.project.team9.services.implementations.NewsService;
import com.ktsnwt.project.team9.services.implementations.ImageService;

import static com.ktsnwt.project.team9.constants.NewsConstants.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class NewsServiceIntegrationTest {

	@Autowired
	private NewsService newsService;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private NewsMapper newsMapper;
	
	@Test
	public void testGetAll_ShouldReturnAllNews() {
		List<News> news = (List<News>) newsService.getAll();

		assertEquals(TOTAL_NO, news.size());
	}

	@Test
	public void testFindAll_WithFirstPageable_ShouldReturnFirst5News() {
		Pageable pageable = PageRequest.of(PAGE_NO, PAGE_SIZE);

		Page<News> news = newsService.findAll(pageable);

		assertEquals(5, news.getNumberOfElements());
	}

	@Test
	public void testFindAll_WithSecondPageable_ShouldReturnSecond5News() {
		Pageable pageable = PageRequest.of(PAGE_NO+1, PAGE_SIZE);

		Page<News> news = newsService.findAll(pageable);

		assertEquals(5, news.getNumberOfElements());
	}

	@Test
	public void testFindAll_WithNonExistingPageable_ShouldReturnEmptyCollection() {
		Pageable pageable = PageRequest.of(1000, PAGE_SIZE);

		Page<News> news = newsService.findAll(pageable);

		assertEquals(0, news.getNumberOfElements());
	}

	@Test
	public void testGetById_WithExistingId_ShouldReturnNewsWithThatId() {
		News news = newsService.getById(NEWS_ID);

		assertEquals(NEWS_NO_1.getDate(), news.getDate());
		assertEquals(NEWS_NO_1.getContent(), news.getContent());
		assertEquals(NEWS_NO_1.getCulturalOfferID(), news.getCulturalOffer().getId());
		
		
	}

	@Test
	public void testGetById_WithNonExistingId_ShouldReturnNull() {
		News news = newsService.getById(NON_EXIST_NEWS_ID);
		assertNull(news);
	}

	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testCreate_WithValidParameters_ShouldReturnCreatedNews() throws Exception {
		int length = ((List<News>) newsService.getAll()).size();
		int lengthImages = ((List<Image>) imageService.getAll()).size();
		
		News newsBefore = newsMapper.toEntity(NEWS_FOR_CREATE);
		
		News newsAfter = newsService.create(newsBefore);
		
		assertEquals(newsBefore.getContent(), newsAfter.getContent());
		assertEquals(newsBefore.getTitle(), newsAfter.getTitle());
		assertEquals(newsBefore.getCulturalOffer().getId(), newsAfter.getCulturalOffer().getId());
		assertEquals(IMAGES.size(), newsAfter.getImages().size());
		assertEquals(newsBefore.getDate(), newsAfter.getDate());
		assertEquals(length + 1, newsService.getAll().size());
		assertEquals(lengthImages + IMAGES.size(), imageService.getAll().size());
	}

	@Test(expected = Exception.class)
	public void testCreate_WithNonExistingCulturalOfferId_ShouldThrowException() throws Exception {
		News newsBefore = newsMapper.toEntity(NEWS_FOR_CREATE_NON_EXIST_ID);
		newsService.create(newsBefore);
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testUpdate_WithNewValidParameters_ShouldReturnUpdatedNews() throws Exception {
		
		int lengthImages = ((List<Image>) imageService.getAll()).size();
		
		News newsBefore = newsMapper.toEntity(NEWS_FOR_UPDATE);
		News newsAfter = newsService.update(NEWS_ID, newsBefore);
		
		assertEquals(newsBefore.getContent(), newsAfter.getContent());
		assertEquals(newsBefore.getCulturalOffer().getId(), newsAfter.getCulturalOffer().getId());
		assertEquals(IMAGES.size(), newsAfter.getImages().size());
		assertEquals(newsBefore.getDate(), newsAfter.getDate());
		assertEquals(newsBefore.getTitle(), newsAfter.getTitle());
		assertEquals(lengthImages + IMAGES.size(), imageService.getAll().size());

	}

	@Test(expected = Exception.class)
	public void testUpdate_WithNonExistingId_ShouldThrowException() throws Exception {
		News newsBefore = newsMapper.toEntity(NEWS_FOR_UPDATE);
		newsService.update(NON_EXIST_NEWS_ID, newsBefore);
	}
	
	@Test(expected = Exception.class)
	public void testUpdate_WithNonExistingCulturalOfferId_ShouldThrowException() throws Exception {
		News newsBefore = newsMapper.toEntity(NEWS_FOR_CREATE_NON_EXIST_ID);
		newsService.update(NEWS_ID, newsBefore);
	}
	

	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testDelete_WithExistingId_ShouldReturnTrue() throws Exception {
		int length = ((List<News>) newsService.getAll()).size();
		int lengthImages = ((List<Image>) imageService.getAll()).size();
		
		News news = newsService.getById(NEWS_ID);
		int imageSize = news.getImages().size();
		Boolean status = newsService.delete(NEWS_ID);

		assertTrue(status);
		assertEquals(length - 1, ((List<News>) newsService.getAll()).size());
		assertEquals(lengthImages - imageSize, imageService.getAll().size());
	}

	@Test(expected = Exception.class)
	public void testDelete_WithNonExistingId_ShouldThrowException() throws Exception {
		newsService.delete(NON_EXIST_NEWS_ID);
	}
	
	@Test
	public void testFindAllByCulturalOffer_WithFirstPageable_ShouldReturnFirst5News() {
		Pageable pageable = PageRequest.of(PAGE_NO, PAGE_SIZE);

		Page<News> news = newsService.findAllByCulturalOffer(EXISTING_CULTURAL_OFFER_ID, pageable);

		assertEquals(3, news.getNumberOfElements());
	}

	@Test
	public void testFindAllByCulturalOffer_WithNonExistingPageable_ShouldReturnEmptyCollection() {
		Pageable pageable = PageRequest.of(1000, PAGE_SIZE);

		Page<News> news = newsService.findAllByCulturalOffer(EXISTING_CULTURAL_OFFER_ID, pageable);

		assertEquals(0, news.getNumberOfElements());
	}
	
	@Test
	public void testFindAllByCulturalOffer_WithNonExistingCOID_ShouldReturnEmptyCollection() {
		Pageable pageable = PageRequest.of(PAGE_NO, PAGE_SIZE);

		Page<News> news = newsService.findAllByCulturalOffer(NON_EXISTING_CULTURAL_OFFER_ID, pageable);

		assertEquals(0, news.getNumberOfElements());
	}

	@Test
	public void testGetSubscribedNews_WithExistingPageable_ShouldReturnFirst5News() {
		Pageable pageable = PageRequest.of(PAGE_NO, PAGE_SIZE);
		
		Page<News> news = newsService.getSubscribedNews(EXISTING_REGISTERED_USER_ID, pageable);
		
		assertEquals(5, news.getNumberOfElements());
	}
	
	@Test
	public void testGetSubscribedNews_WithNonExistingPageable_ShouldReturnEmptyCollection() {
		Pageable pageable = PageRequest.of(1000, PAGE_SIZE);

		Page<News> news = newsService.getSubscribedNews(EXISTING_REGISTERED_USER_ID, pageable);

		assertEquals(0, news.getNumberOfElements());
	}
	
	@Test
	public void testGetSubscribedNews_WithNonExistingUserId_ShouldReturnEmptyCollection() {
		Pageable pageable = PageRequest.of(PAGE_NO, PAGE_SIZE);

		Page<News> news = newsService.getSubscribedNews(NON_EXISTING_REGISTERED_USER_ID, pageable);

		assertEquals(0, news.getNumberOfElements());
	}
	
	@Test
	public void testGetSubscribedNews_WithExistingCategoryId_ShouldReturnFirst5News() {
		Pageable pageable = PageRequest.of(PAGE_NO, PAGE_SIZE);

		Page<News> news = newsService.getSubscribedNews(EXISTING_REGISTERED_USER_ID, EXISTING_CATEGORY_ID, pageable);

		assertEquals(4, news.getNumberOfElements());
	}
	
	@Test
	public void testGetSubscribedNews_WithNonExistingCategoryId_ShouldReturnEmptyCollection() {
		Pageable pageable = PageRequest.of(PAGE_NO, PAGE_SIZE);

		Page<News> news = newsService.getSubscribedNews(EXISTING_REGISTERED_USER_ID, NON_EXISTING_CATEGORY_ID, pageable);

		assertEquals(0, news.getNumberOfElements());
	}

	

}
