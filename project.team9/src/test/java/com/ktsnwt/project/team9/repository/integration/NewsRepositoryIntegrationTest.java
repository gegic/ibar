package com.ktsnwt.project.team9.repository.integration;

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

import com.ktsnwt.project.team9.constants.NewsConstants;
import com.ktsnwt.project.team9.model.News;
import com.ktsnwt.project.team9.repositories.INewsRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class NewsRepositoryIntegrationTest {

	@Autowired
	INewsRepository newsRepository;

	@Test
	public void testFindSubscribedNews_WithExistingUserID_ShouldReturnNewsPage() {
		Pageable pageable = PageRequest.of(NewsConstants.PAGE_NO, NewsConstants.PAGE_SIZE);
		
		Page<News> newsPage = newsRepository.findSubscribedNews(NewsConstants.EXISTING_REGISTERED_USER_ID, pageable);

		assertEquals(5, newsPage.getTotalElements() );
	}
	
	@Test
	public void testFindSubscribedNews_WithNotExistingUserID_ShouldReturnNewsPageWithEmptyContent() {
		Pageable pageable = PageRequest.of(NewsConstants.PAGE_NO, NewsConstants.PAGE_SIZE);
		
		Page<News> newsPage = newsRepository.findSubscribedNews(NewsConstants.NON_EXISTING_REGISTERED_USER_ID, pageable);
		assertEquals(0, newsPage.getTotalElements() );
	}
	
	@Test
	public void testFindSubscribedNews_WithExistingCategoryID_ShouldReturnNewsPage() {
		Pageable pageable = PageRequest.of(NewsConstants.PAGE_NO, NewsConstants.PAGE_SIZE);
		
		Page<News> newsPage = newsRepository.findSubscribedNews(NewsConstants.EXISTING_REGISTERED_USER_ID
				, NewsConstants.EXISTING_CATEGORY_ID, pageable);

		assertEquals(4, newsPage.getTotalElements() );
	}
	
	@Test
	public void testFindSubscribedNews_WithNotExistingCatetegoryID_ShouldReturnNewsPageWithEmptyContent() {
		Pageable pageable = PageRequest.of(NewsConstants.PAGE_NO, NewsConstants.PAGE_SIZE);
		
		Page<News> newsPage = newsRepository.findSubscribedNews(NewsConstants.EXISTING_REGISTERED_USER_ID,
				NewsConstants.NON_EXISTING_CATEGORY_ID ,pageable);
		assertEquals(0, newsPage.getTotalElements() );
	}
	
	@Test
	public void testFindByCulturalOfferId_WithExistingCOID_ShouldReturnNewsPage() {
		Pageable pageable = PageRequest.of(NewsConstants.PAGE_NO, NewsConstants.PAGE_SIZE);
		
		Page<News> newsPage = newsRepository.findByCulturalOfferId(NewsConstants.EXISTING_CULTURAL_OFFER_ID, pageable);

		assertEquals(3, newsPage.getTotalElements() );
	}
	
	@Test
	public void testFindByCulturalOfferId_WithNotExistingCOID_ShouldReturnNewsPageWithEmptyContent() {
		Pageable pageable = PageRequest.of(NewsConstants.PAGE_NO, NewsConstants.PAGE_SIZE);
		
		Page<News> newsPage = newsRepository.findByCulturalOfferId(NewsConstants.NON_EXISTING_CULTURAL_OFFER_ID, pageable);
		assertEquals(0, newsPage.getTotalElements());
	}
	
	
	

}
