package com.ktsnwt.project.team9.e2e.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.e2e.pages.HomePage;
import com.ktsnwt.project.team9.e2e.pages.LoginPage;
import com.ktsnwt.project.team9.e2e.pages.ReviewCulturalOfferPage;
import com.ktsnwt.project.team9.model.Mark;
import com.ktsnwt.project.team9.services.implementations.MarkBookService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource("classpath:test.properties")
public class MarksE2ETest {

	@Autowired
	private MarkBookService markService;
	
	public static final String BASE_URL = "https://localhost:4200";

	private WebDriver driver;
	
	private HomePage homePage;

	private LoginPage signInPage;
	
	private ReviewCulturalOfferPage reviewCulturalOfferPage;
	
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
		reviewCulturalOfferPage = PageFactory.initElements(driver, ReviewCulturalOfferPage.class);
	}
	
	private void loginSetUpAsRegisteredUser() {
		driver.navigate().to(BASE_URL + "/auth/sign-in");

		signInPage.ensureIsDisplayedEmail();

		signInPage.getEmail().sendKeys("email_adresa21@gmail.com");
		signInPage.getPassword().sendKeys("sifra123");

		signInPage.getSignIn().click();
	}
	
	private void loginSetUpAsRegisteredUserAlreadyRated() {
		driver.navigate().to(BASE_URL + "/auth/sign-in");

		signInPage.ensureIsDisplayedEmail();

		signInPage.getEmail().sendKeys("email_adresa20@gmail.com");
		signInPage.getPassword().sendKeys("sifra123");

		signInPage.getSignIn().click();
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void Mark_WithValidParams_ShouldSuccess() throws InterruptedException {
		loginSetUpAsRegisteredUser();
		int size = ((List<Mark>) markService.getAll()).size();
		
		homePage.ensureIsDisplayedCulturalOfferNavigation();
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.ensureIsDisplayedMore1Button();
		homePage.getMore1().click();

		reviewCulturalOfferPage.ensureIsDisplayedStar();
		assertEquals("https://localhost:4200/cultural-offers/1", driver.getCurrentUrl());
		reviewCulturalOfferPage.getStar().click();
		
		this.pause(5000);
		assertEquals(size + 1, ((List<Mark>) markService.getAll()).size());
		
		driver.close();
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void MarkUpdate_WithValidParams_ShouldSuccess() throws InterruptedException {
		loginSetUpAsRegisteredUserAlreadyRated();
		int size = ((List<Mark>) markService.getAll()).size();
		
		homePage.ensureIsDisplayedCulturalOfferNavigation();
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.ensureIsDisplayedMore1Button();
		homePage.getMore1().click();
		
		reviewCulturalOfferPage.ensureIsDisplayedStar();
		assertEquals("https://localhost:4200/cultural-offers/1", driver.getCurrentUrl());
		reviewCulturalOfferPage.getStar2().click();
		
		this.pause(5000);
		assertEquals(size, ((List<Mark>) markService.getAll()).size());
		
		driver.close();
	}
	
	public void pause(Integer milliseconds){
	    try {
	        TimeUnit.MILLISECONDS.sleep(milliseconds);
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	}
}
