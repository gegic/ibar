package com.ktsnwt.project.team9.e2e.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.e2e.pages.NewsReviewPage;
import com.ktsnwt.project.team9.e2e.pages.NewsViewPage;
import com.ktsnwt.project.team9.e2e.pages.ReviewCulturalOfferPage;
import com.ktsnwt.project.team9.e2e.pages.DashboardPage;
import com.ktsnwt.project.team9.e2e.pages.HomePage;
import com.ktsnwt.project.team9.e2e.pages.LoginPage;
import com.ktsnwt.project.team9.e2e.pages.MyNewsPage;
import com.ktsnwt.project.team9.model.News;
import com.ktsnwt.project.team9.services.implementations.CulturalOfferService;
import com.ktsnwt.project.team9.services.implementations.NewsService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource("classpath:test.properties")
public class NewsE2ETest {
	@Autowired
	NewsService newsService;
	
	@Autowired
	CulturalOfferService coService;
	
	private WebDriver driver;
	
	public static final String BASE_URL = "https://localhost:4200";
	
	public static final String[] ADMIN_CREDS = {"email_adresa1@gmail.com","sifra123"};
	
	public static final String[] USER_CREDS = {"email_adresa21@gmail.com","sifra123"};
	
	public static final String pathToImages = "src\\test\\resources\\uploadedImages\\slika1.jpg";
	public static final String pathToImages2 = "src\\test\\resources\\uploadedImages\\slika2.jpg";
	
	public static String myPath = "";
	public static String myPath2 = "";
	
	private HomePage homePage;
	
	private LoginPage signInPage;
	
	private NewsReviewPage newsPage;
	
	private ReviewCulturalOfferPage reviewCOPage;
	
	private MyNewsPage myNewsPage;
	
	private NewsViewPage newsViewPage;
	
	private DashboardPage dashboardPage;
	
	@Before
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
		
		ChromeOptions handlingSSL = new ChromeOptions();
		handlingSSL.setAcceptInsecureCerts(true);
		
		driver = new ChromeDriver(handlingSSL);
		
		driver.navigate().to(BASE_URL);

		driver.manage().window().maximize();

		homePage = PageFactory.initElements(driver, HomePage.class);
		signInPage = PageFactory.initElements(driver, LoginPage.class);
		reviewCOPage = PageFactory.initElements(driver, ReviewCulturalOfferPage.class);
		newsPage = PageFactory.initElements(driver, NewsReviewPage.class);
		myNewsPage = PageFactory.initElements(driver, MyNewsPage.class);
		newsViewPage = PageFactory.initElements(driver, NewsViewPage.class);
		dashboardPage = PageFactory.initElements(driver, DashboardPage.class);
		
		try {
			myPath = (Paths.get((new File(".")).getCanonicalPath(),pathToImages)).toString();
			myPath2 = (Paths.get((new File(".")).getCanonicalPath(),pathToImages2)).toString();
		} catch (IOException e) {
			myPath = "";
		}
	}
	
	private void signInSetUp(String[] creds) {
		driver.navigate().to(BASE_URL + "/auth/sign-in");

		signInPage.ensureIsDisplayedEmail();

		signInPage.getEmail().sendKeys(creds[0]);
		signInPage.getPassword().sendKeys(creds[1]);

		signInPage.getSignIn().click();
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void deleteNews_ShouldSuccess() {
		signInSetUp(ADMIN_CREDS);
		
		int size = ((List<News>) newsService.getAll()).size();

		homePage.ensureIsDisplayedFestivalsTab();
		homePage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getSearch().sendKeys("festival 100");
		
		homePage.getFestivalsTab().click();
		
		homePage.ensureIsDisplayedMoreButton();

		pause(2000);
		homePage.getMore().click();

		reviewCOPage.ensureIsDisplayedName();

		assertEquals("https://localhost:4200/cultural-offers/3", driver.getCurrentUrl());
		
		reviewCOPage.getNewsButton().click();
		
		newsPage.ensureIsDisplayedCulturalOfferButton();
		
		assertEquals("https://localhost:4200/news/cultural-offer/3", driver.getCurrentUrl());
		
		newsPage.getRemoveButton().click();
		
		newsPage.ensureIsDisplayedOKRemoveButton();

		newsPage.getOkButton().click();

		newsPage.ensureIsDisplayedMessage();
		
		assertEquals("You have successfully deleted news!",
				newsPage.getMessage().getText());
		assertEquals(size - 1, ((List<News>) newsService.getAll()).size());
		driver.close();
	}
	
	@Test
	public void deleteNews_Cancel_ShouldSuccess() {
		signInSetUp(ADMIN_CREDS);
		
		int size = ((List<News>) newsService.getAll()).size();

		homePage.ensureIsDisplayedFestivalsTab();
		homePage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getSearch().sendKeys("festival 100");
		
		homePage.getFestivalsTab().click();
		
		homePage.ensureIsDisplayedMoreButton();
		
		pause(2000);
		
		homePage.getMore().click();

		reviewCOPage.ensureIsDisplayedName();

		assertEquals("https://localhost:4200/cultural-offers/3", driver.getCurrentUrl());
		
		reviewCOPage.getNewsButton().click();
		
		newsPage.ensureIsDisplayedCulturalOfferButton();
		
		assertEquals("https://localhost:4200/news/cultural-offer/3", driver.getCurrentUrl());
		
		newsPage.getRemoveButton().click();
		
		newsPage.ensureIsDisplayedOKRemoveButton();

		newsPage.getNoButton().click();
		
		assertEquals(size, ((List<News>) newsService.getAll()).size());
		driver.close();
	}
	
	public void navigateToCulturalOffer_ShouldSuccess() {
		homePage.ensureIsDisplayedFestivalsTab();
		
		homePage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getSearch().sendKeys("festival 100");
		
		homePage.getFestivalsTab().click();
		
		homePage.ensureIsDisplayedMoreButton();

		pause(2000);
		
		homePage.getMore().click();

		reviewCOPage.ensureIsDisplayedName();

		assertEquals("https://localhost:4200/cultural-offers/3", driver.getCurrentUrl());
		
		reviewCOPage.getNewsButton().click();
		
		newsPage.ensureIsDisplayedCulturalOfferButton();
		
		assertEquals("https://localhost:4200/news/cultural-offer/3", driver.getCurrentUrl());
		
		newsPage.getCulturalOfferButton().click();
		
		assertEquals("https://localhost:4200/cultural-offer/3", driver.getCurrentUrl());
		
		driver.close();
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void createNews_WithValidParams_ShouldSuccess() {
		signInSetUp(ADMIN_CREDS);
		int size = ((List<News>) newsService.getAll()).size();

		homePage.ensureIsDisplayedFestivalsTab();
		homePage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getSearch().sendKeys("festival 100");
		
		homePage.getFestivalsTab().click();
		
		homePage.ensureIsDisplayedMoreButton();

		pause(2000);
		
		homePage.getMore().click();

		reviewCOPage.ensureIsDisplayedName();

		assertEquals("https://localhost:4200/cultural-offers/3", driver.getCurrentUrl());
		
		reviewCOPage.getNewsButton().click();
		
		newsPage.ensureIsDisplayedCulturalOfferButton();
		
		assertEquals("https://localhost:4200/news/cultural-offer/3", driver.getCurrentUrl());
		
		newsPage.getAddButton().click();
		newsPage.ensureIsDisplayedNewsContent();
		
		newsPage.getNewsTitle().sendKeys("New title");
		newsPage.getNewsContent().sendKeys("New content");
		newsPage.getNewsImages().sendKeys(myPath);
		pause(1000);
		newsPage.getAddOrUpdateBtn().click();
		
		newsPage.ensureIsDisplayedMessage();
		
		
		List<WebElement> data = driver.findElements(By.xpath("//*[contains(text(),'New content')]"));
		assertEquals("News added successfully",
				newsPage.getMessage().getText());
		assertEquals(size + 1, ((List<News>) newsService.getAll()).size());
		assertEquals(data.size(), 1);
		
		driver.close();
	}
	
	@Test
	public void createNews_WithValidParamsAndClose_ShouldSuccess() {
		signInSetUp(ADMIN_CREDS);
		int size = ((List<News>) newsService.getAll()).size();

		homePage.ensureIsDisplayedFestivalsTab();
		homePage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getSearch().sendKeys("festival 100");
		
		homePage.getFestivalsTab().click();
		
		homePage.ensureIsDisplayedMoreButton();

		pause(2000);
		
		homePage.getMore().click();

		reviewCOPage.ensureIsDisplayedName();

		assertEquals("https://localhost:4200/cultural-offers/3", driver.getCurrentUrl());
		
		reviewCOPage.getNewsButton().click();
		
		newsPage.ensureIsDisplayedCulturalOfferButton();
		
		assertEquals("https://localhost:4200/news/cultural-offer/3", driver.getCurrentUrl());
		
		newsPage.getAddButton().click();
		newsPage.ensureIsDisplayedNewsContent();

		newsPage.getNewsTitle().sendKeys("New title");
		newsPage.getNewsContent().sendKeys("New content");
		newsPage.getNewsImages().sendKeys(myPath);
		pause(1000);
		newsPage.getCloseBtn().click();
		
		assertEquals(size, ((List<News>) newsService.getAll()).size());
		
		driver.close();
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void updateNews_WithValidParams_ShouldSuccess() {
		signInSetUp(ADMIN_CREDS);
		int size = ((List<News>) newsService.getAll()).size();

		homePage.ensureIsDisplayedFestivalsTab();
		homePage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getSearch().sendKeys("festival 100");
		
		homePage.getFestivalsTab().click();
		
		homePage.ensureIsDisplayedMoreButton();

		pause(2000);
		
		homePage.getMore().click();

		reviewCOPage.ensureIsDisplayedName();

		assertEquals("https://localhost:4200/cultural-offers/3", driver.getCurrentUrl());
		
		reviewCOPage.getNewsButton().click();
		
		newsPage.ensureIsDisplayedCulturalOfferButton();
		
		assertEquals("https://localhost:4200/news/cultural-offer/3", driver.getCurrentUrl());
		
		newsPage.getUpdateButton().click();
		newsPage.ensureIsDisplayedNewsContent();
		
		newsPage.getNewsTitle().clear();
		newsPage.getNewsTitle().sendKeys("New title");
		newsPage.getNewsContent().clear();
		newsPage.getNewsContent().sendKeys("New content");
		newsPage.getNewsImages().sendKeys(myPath);
		pause(1000);
		newsPage.getAddOrUpdateBtn().click();
		

		newsPage.ensureIsDisplayedMessage();
		
		List<WebElement> data = driver.findElements(By.xpath("//*[contains(text(),'New content')]"));
		
		assertEquals(data.size(), 1);
		assertEquals("News updated successfully",
				newsPage.getMessage().getText());
		assertEquals(size, ((List<News>) newsService.getAll()).size());
		
		driver.close();
	}
	
	@Test
	public void updateNews_WithValidParamsAndClose_ShouldSuccess() {
		signInSetUp(ADMIN_CREDS);
		int size = ((List<News>) newsService.getAll()).size();

		homePage.ensureIsDisplayedFestivalsTab();
		homePage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getSearch().sendKeys("festival 100");
		
		homePage.getFestivalsTab().click();
		
		homePage.ensureIsDisplayedMoreButton();

		pause(2000);
		
		homePage.getMore().click();

		reviewCOPage.ensureIsDisplayedName();

		assertEquals("https://localhost:4200/cultural-offers/3", driver.getCurrentUrl());
		
		reviewCOPage.getNewsButton().click();
		
		newsPage.ensureIsDisplayedCulturalOfferButton();
		
		assertEquals("https://localhost:4200/news/cultural-offer/3", driver.getCurrentUrl());
		
		newsPage.getUpdateButton().click();
		newsPage.ensureIsDisplayedNewsContent();
		
		newsPage.getNewsContent().sendKeys("New content");
		newsPage.getNewsImages().sendKeys(myPath);
		pause(1000);
		newsPage.getCloseBtn().click();
		
		assertEquals(size, ((List<News>) newsService.getAll()).size());
		
		driver.close();
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void updateNews_WithSameParams_ShouldSuccess() {
		signInSetUp(ADMIN_CREDS);
		int size = ((List<News>) newsService.getAll()).size();

		homePage.ensureIsDisplayedFestivalsTab();
		homePage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getSearch().sendKeys("festival 100");
		
		homePage.getFestivalsTab().click();
		
		homePage.ensureIsDisplayedMoreButton();

		pause(2000);
		
		homePage.getMore().click();

		reviewCOPage.ensureIsDisplayedName();

		assertEquals("https://localhost:4200/cultural-offers/3", driver.getCurrentUrl());
		
		reviewCOPage.getNewsButton().click();
		
		newsPage.ensureIsDisplayedCulturalOfferButton();
		
		assertEquals("https://localhost:4200/news/cultural-offer/3", driver.getCurrentUrl());
		
		newsPage.getUpdateButton().click();
		newsPage.ensureIsDisplayedNewsContent();
		pause(1000);
		newsPage.getAddOrUpdateBtn().click();
		
		newsPage.ensureIsDisplayedMessage();
		
		assertEquals("News updated successfully",
				newsPage.getMessage().getText());
		assertEquals(size, ((List<News>) newsService.getAll()).size());
		
		driver.close();
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void updateNews_ClearImages_ShouldSuccess() {
		signInSetUp(ADMIN_CREDS);
		int size = ((List<News>) newsService.getAll()).size();

		homePage.ensureIsDisplayedFestivalsTab();
		homePage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getSearch().sendKeys("festival 100");
		
		homePage.getFestivalsTab().click();
		
		homePage.ensureIsDisplayedMoreButton();
		
		pause(2000);
		
		homePage.getMore().click();

		reviewCOPage.ensureIsDisplayedName();

		assertEquals("https://localhost:4200/cultural-offers/3", driver.getCurrentUrl());
		
		reviewCOPage.getNewsButton().click();
		
		newsPage.ensureIsDisplayedCulturalOfferButton();
		
		assertEquals("https://localhost:4200/news/cultural-offer/3", driver.getCurrentUrl());
		
		newsPage.getUpdateButton().click();
		newsPage.ensureIsDisplayedNewsContent();
		pause(1000);

		newsPage.getNewsImages().sendKeys(myPath);
		newsPage.getNewsImages().sendKeys(myPath2);
		pause(1000);

		List<WebElement> x_button = driver.findElements(By.className("x-button"));
		while(x_button.size() != 0) {
			x_button.get(0).click();
			pause(1000);
			x_button = driver.findElements(By.className("x-button"));
		}
		
		pause(1000);
		newsPage.getAddOrUpdateBtn().click();
		
		newsPage.ensureIsDisplayedMessage();
		
		assertEquals("News updated successfully",
				newsPage.getMessage().getText());
		assertEquals(size, ((List<News>) newsService.getAll()).size());
		
		driver.close();
	}
	
	
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void subscribeToNews_ShouldSuccess() {
		signInSetUp(USER_CREDS);
		Pageable pageable = PageRequest.of(0, 20);
		int size = (newsService.getSubscribedNews(8L, pageable)).getContent().size();
		
		homePage.ensureIsDisplayedFestivalsTab();
		
		homePage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getSearch().sendKeys("festival 100");
		
		homePage.getFestivalsTab().click();
		
		homePage.ensureIsDisplayedMoreButton();

		pause(2000);
		
		homePage.getMore().click();

		reviewCOPage.ensureIsDisplayedName();

		assertEquals("https://localhost:4200/cultural-offers/3", driver.getCurrentUrl());
		
		reviewCOPage.ensureIsDisplayedSubscribeButton();
		
		reviewCOPage.getSubscribeButton().click();
		
		reviewCOPage.ensureIsDisplayedUnsubscribeButton();
		
		pause(2000);
		int newSize = (newsService.getSubscribedNews(8L, pageable)).getContent().size();
		
		assertTrue(size < newSize);
		driver.close();
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void unsubscribeToNews_ShouldSuccess() {
		signInSetUp(USER_CREDS);
		Pageable pageable = PageRequest.of(0, 20);
		
		homePage.ensureIsDisplayedFestivalsTab();
		
		homePage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getSearch().sendKeys("festival 100");
		
		homePage.getFestivalsTab().click();
		
		homePage.ensureIsDisplayedMoreButton();

		pause(2000);
		
		homePage.getMore().click();

		reviewCOPage.ensureIsDisplayedName();

		assertEquals("https://localhost:4200/cultural-offers/3", driver.getCurrentUrl());
		
		reviewCOPage.ensureIsDisplayedSubscribeButton();
		
		reviewCOPage.getSubscribeButton().click();
		
		reviewCOPage.ensureIsDisplayedUnsubscribeButton();
		pause(2000);
		int size = (newsService.getSubscribedNews(8L, pageable)).getContent().size();
		
		reviewCOPage.getUnsubscribeButton().click();
		
		reviewCOPage.ensureIsDisplayedSubscribeButton();
		
		pause(2000);
		int newSize = (newsService.getSubscribedNews(8L, pageable)).getContent().size();
		
		assertTrue(size > newSize);
		driver.close();
	}
	
	@Test
	public void navigateToMyNews_WithChangeTab_ShouldSuccess() {
		signInSetUp(USER_CREDS);
		homePage.ensureIsDisplayedFestivalsTab();
		
		homePage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getSearch().sendKeys("festival 100");
		
		homePage.getFestivalsTab().click();
		
		homePage.ensureIsDisplayedMoreButton();

		pause(2000);
		
		homePage.getMore().click();

		reviewCOPage.ensureIsDisplayedName();

		assertEquals("https://localhost:4200/cultural-offers/3", driver.getCurrentUrl());
		
		reviewCOPage.ensureIsDisplayedSubscribeButton();
		
		reviewCOPage.getSubscribeButton().click();
		
		driver.navigate().to(BASE_URL);
		
		homePage.ensureIsDisplayedSearch();
		
		homePage.getMyNewsPage().click();

		myNewsPage.ensureIsDisplayedCulturalOfferButton();
		
		assertEquals("https://localhost:4200/news/my-news", driver.getCurrentUrl());
		
		myNewsPage.getFestivalTab().click();
		pause(2000);
		
		driver.close();
	}
	
	@Test
	public void navigateToMyNews_WithNavigateToCO_ShouldSuccess() {
		signInSetUp(USER_CREDS);
		homePage.ensureIsDisplayedFestivalsTab();
		
		homePage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getSearch().sendKeys("festival 100");
		
		homePage.getFestivalsTab().click();
		
		homePage.ensureIsDisplayedMoreButton();

		pause(2000);
		
		homePage.getMore().click();

		reviewCOPage.ensureIsDisplayedName();

		assertEquals("https://localhost:4200/cultural-offers/3", driver.getCurrentUrl());
		
		reviewCOPage.ensureIsDisplayedSubscribeButton();
		
		reviewCOPage.getSubscribeButton().click();
		
		driver.navigate().to(BASE_URL);
		
		homePage.ensureIsDisplayedSearch();
		
		homePage.getMyNewsPage().click();

		myNewsPage.ensureIsDisplayedCulturalOfferButton();
		
		assertEquals("https://localhost:4200/news/my-news", driver.getCurrentUrl());
		
		myNewsPage.getCulturalOfferButton().click();
		pause(2000);

		assertEquals("https://localhost:4200/cultural-offers/1", driver.getCurrentUrl());
		
		
		driver.close();
	}
	
	@Test
	public void navigateToNewsDetail_WithBackNavigationToCulturalOffer_ShouldSuccess() {
		homePage.ensureIsDisplayedFestivalsTab();
		homePage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getSearch().sendKeys("festival 100");
		
		homePage.getFestivalsTab().click();
		
		homePage.ensureIsDisplayedMoreButton();

		pause(2000);
		homePage.getMore().click();

		reviewCOPage.ensureIsDisplayedName();

		assertEquals("https://localhost:4200/cultural-offers/3", driver.getCurrentUrl());
		
		reviewCOPage.getNewsButton().click();
		
		newsPage.ensureIsDisplayedCulturalOfferButton();
		
		assertEquals("https://localhost:4200/news/cultural-offer/3", driver.getCurrentUrl());
		
		newsPage.ensureIsDisplayedMoreBtn();
		
		newsPage.getMoreButton().click();
		
		newsViewPage.ensureIsDisplayedCulturalOfferButton();
		
		newsViewPage.getCulturalOfferButton().click();;
		
		assertEquals("https://localhost:4200/cultural-offers/3", driver.getCurrentUrl());
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void unsubscribeFromDashboardPage_ShouldSuccess() {
		signInSetUp(USER_CREDS);
		Pageable pageable = PageRequest.of(0, 10);
		int size = coService.getSubscribedCulturalOffer(8L, pageable).getNumberOfElements();
		homePage.ensureIsDisplayedFestivalsTab();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getDashboardPage().click();

		dashboardPage.ensureIsDisplayedUnsubscribeButton();

		assertEquals("https://localhost:4200/news/my-offers", driver.getCurrentUrl());
		
		dashboardPage.getUnsubscribeButton().click();
		
		pause(2000);

		int newSize = coService.getSubscribedCulturalOffer(8L, pageable).getNumberOfElements();
		
		assertTrue(size > newSize);
	}
	
	@Test
	public void navigateToOffer_ShouldSuccess() {
		signInSetUp(USER_CREDS);
		homePage.ensureIsDisplayedFestivalsTab();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getDashboardPage().click();

		dashboardPage.ensureIsDisplayedUnsubscribeButton();

		assertEquals("https://localhost:4200/news/my-offers", driver.getCurrentUrl());
		
		dashboardPage.getCulturalOfferButton().click();
		
		pause(2000);
		reviewCOPage.ensureIsDisplayedNewsButton();
		
		assertEquals("https://localhost:4200/cultural-offers/1", driver.getCurrentUrl());
	}
	
	@After
	public void quit() {
		driver.quit();
	}
	
	public void pause(Integer milliseconds){
	    try {
	        TimeUnit.MILLISECONDS.sleep(milliseconds);
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	}

}
