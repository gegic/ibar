package com.ktsnwt.project.team9.e2e.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.FileSystems;
import java.util.List;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.e2e.pages.CommentRequestsPage;
import com.ktsnwt.project.team9.e2e.pages.HomePage;
import com.ktsnwt.project.team9.e2e.pages.LoginPage;
import com.ktsnwt.project.team9.e2e.pages.ReviewCulturalOfferPage;
import com.ktsnwt.project.team9.model.Comment;
import com.ktsnwt.project.team9.services.implementations.CommentService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource("classpath:test.properties")
public class CommentE2ETest {

	@Autowired
	CommentService commentService;

	public static final String BASE_URL = "https://localhost:4200";

	private WebDriver driver;

	private HomePage homePage;

	private LoginPage signInPage;

	private ReviewCulturalOfferPage reviewCulturalOfferPage;

	private CommentRequestsPage commentRequestsPage;

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
		commentRequestsPage = PageFactory.initElements(driver, CommentRequestsPage.class);
	}

	private void loginSetUp() {
		driver.navigate().to(BASE_URL + "/auth/sign-in");

		signInPage.ensureIsDisplayedEmail();

		signInPage.getEmail().sendKeys("email_adresa4@gmail.com");
		signInPage.getPassword().sendKeys("sifra123");

		signInPage.getSignIn().click();
	}

	private void loginSetUpAsRegisteredUser() {
		driver.navigate().to(BASE_URL + "/auth/sign-in");

		signInPage.ensureIsDisplayedEmail();

		signInPage.getEmail().sendKeys("email_adresa21@gmail.com");
		signInPage.getPassword().sendKeys("sifra123");

		signInPage.getSignIn().click();
	}

	@Test
	public void commentReview_WithAdminSignedIn_ShouldSuccess() {
		loginSetUp();

		Pageable pageable = PageRequest.of(0, 5);
		Page<Comment> page = commentService.findAllByCOID(pageable, 1L);

		homePage.ensureIsDisplayedCulturalOfferNavigation();
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());

		homePage.ensureIsDisplayedMore1Button();
		homePage.getMore1().click();

		reviewCulturalOfferPage.ensureIsDisplayedReviewBtn();
		assertEquals("https://localhost:4200/cultural-offers/1", driver.getCurrentUrl());

		reviewCulturalOfferPage.getReviewBtn().click();
		reviewCulturalOfferPage.ensureIsDisplayedComment();

		assertEquals("https://localhost:4200/cultural-offers/1/comments", driver.getCurrentUrl());
		List<WebElement> comments = driver.findElements(By.tagName("app-comment"));
		assertEquals(page.getNumberOfElements(), comments.size());

		driver.close();
	}

	@Test
	public void commentReview_WithRegUserSignedIn_ShouldSuccess() {
		loginSetUpAsRegisteredUser();

		Pageable pageable = PageRequest.of(0, 5);
		Page<Comment> page = commentService.findAllByCOID(pageable, 1L);

		homePage.ensureIsDisplayedCulturalOfferNavigation();
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());

		homePage.ensureIsDisplayedMore1Button();
		homePage.getMore1().click();

		reviewCulturalOfferPage.ensureIsDisplayedReviewBtn();
		assertEquals("https://localhost:4200/cultural-offers/1", driver.getCurrentUrl());

		reviewCulturalOfferPage.getReviewBtn().click();
		reviewCulturalOfferPage.ensureIsDisplayedComment();

		assertEquals("https://localhost:4200/cultural-offers/1/comments", driver.getCurrentUrl());
		List<WebElement> comments = driver.findElements(By.tagName("app-comment"));
		assertEquals(page.getNumberOfElements(), comments.size());

		driver.close();
	}

	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void addComment_WithText_ShouldSuccess() {
		loginSetUpAsRegisteredUser();

		int size = ((List<Comment>) commentService.getAll()).size();

		homePage.ensureIsDisplayedCulturalOfferNavigation();
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());

		homePage.ensureIsDisplayedMore1Button();
		homePage.getMore1().click();

		reviewCulturalOfferPage.ensureIsDisplayedAddCommentBtn();
		assertEquals("https://localhost:4200/cultural-offers/1", driver.getCurrentUrl());

		reviewCulturalOfferPage.getAddCommentBtn().click();
		reviewCulturalOfferPage.ensureIsDisplayedCommentText();
		reviewCulturalOfferPage.ensureIsDisplayedNewCommentBtn();

		reviewCulturalOfferPage.getCommentText().sendKeys("novi komentar");
		reviewCulturalOfferPage.getNewCommentBtn().click();

		reviewCulturalOfferPage.ensureIsDisplayedMessage();

		assertEquals("Your comment is sent to administrator for approval",
				reviewCulturalOfferPage.getMessage().getText());
		assertEquals(size + 1, ((List<Comment>) commentService.getAll()).size());

		driver.close();
	}

	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void addComment_WithImage_ShouldSuccess() {
		loginSetUpAsRegisteredUser();

		int size = ((List<Comment>) commentService.getAll()).size();

		homePage.ensureIsDisplayedCulturalOfferNavigation();
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());

		homePage.ensureIsDisplayedMore1Button();
		homePage.getMore1().click();

		reviewCulturalOfferPage.ensureIsDisplayedAddCommentBtn();
		assertEquals("https://localhost:4200/cultural-offers/1", driver.getCurrentUrl());

		reviewCulturalOfferPage.getAddCommentBtn().click();
		reviewCulturalOfferPage.ensureIsDisplayedNewCommentBtn();
		String absolutePath = FileSystems.getDefault().getPath("src/test/resources/uploadedImages/comment_slika6.jpg")
				.normalize().toAbsolutePath().toString();
		reviewCulturalOfferPage.getCommentImg().sendKeys(absolutePath);
		reviewCulturalOfferPage.getNewCommentBtn().click();

		reviewCulturalOfferPage.ensureIsDisplayedMessage();

		assertEquals("Your comment is sent to administrator for approval",
				reviewCulturalOfferPage.getMessage().getText());
		assertEquals(size + 1, ((List<Comment>) commentService.getAll()).size());

		driver.close();
	}

	@Test
	public void addComment_WithNoTextAndNoImg_ShouldFail() {
		loginSetUpAsRegisteredUser();

		int size = ((List<Comment>) commentService.getAll()).size();

		homePage.ensureIsDisplayedCulturalOfferNavigation();
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());

		homePage.ensureIsDisplayedMore1Button();
		homePage.getMore1().click();

		reviewCulturalOfferPage.ensureIsDisplayedAddCommentBtn();
		assertEquals("https://localhost:4200/cultural-offers/1", driver.getCurrentUrl());

		reviewCulturalOfferPage.getAddCommentBtn().click();
		reviewCulturalOfferPage.ensureIsDisplayedNewCommentBtn();

		reviewCulturalOfferPage.getNewCommentBtn().click();

		reviewCulturalOfferPage.ensureIsDisplayedEmptyComment();
		
		assertEquals(true, reviewCulturalOfferPage.getEmptyComment().isDisplayed());
		assertEquals(size, ((List<Comment>) commentService.getAll()).size());

		driver.close();
	}

	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void approveComment_ShouldSuccess() {
		loginSetUp();

		int size = ((List<Comment>) commentService.getAll()).size();
		Pageable pageable = PageRequest.of(0, 5);
		int notApproved = ((Page<Comment>) commentService.findAllByNotApprovedByAdminId(pageable, 4L))
				.getNumberOfElements();

		homePage.ensureIsDisplayedCommentRequestsNavigation();
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());

		homePage.getCommentRequestsPage().click();

		commentRequestsPage.ensureIsDisplayedApproveBtn();

		assertEquals("https://localhost:4200/approving-comments", driver.getCurrentUrl());

		commentRequestsPage.getApproveBtn().click();

		commentRequestsPage.ensureIsDisplayedMessage();
		assertEquals("Comment successfully approved", commentRequestsPage.getMessage().getText());
		assertEquals(size, ((List<Comment>) commentService.getAll()).size());
		assertEquals(notApproved - 1,
				((Page<Comment>) commentService.findAllByNotApprovedByAdminId(pageable, 4L)).getNumberOfElements());
		driver.close();
	}

	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void declineComment_ShouldSuccess() {
		loginSetUp();

		int size = ((List<Comment>) commentService.getAll()).size();

		homePage.ensureIsDisplayedCommentRequestsNavigation();
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());

		homePage.getCommentRequestsPage().click();

		commentRequestsPage.ensureIsDisplayedDeclineBtn();

		assertEquals("https://localhost:4200/approving-comments", driver.getCurrentUrl());

		commentRequestsPage.getDeclineBtn().click();

		commentRequestsPage.ensureIsDisplayedMessage();
		assertEquals("Comment successfully declined", commentRequestsPage.getMessage().getText());
		assertEquals(size - 1, ((List<Comment>) commentService.getAll()).size());

		driver.close();
	}
}
