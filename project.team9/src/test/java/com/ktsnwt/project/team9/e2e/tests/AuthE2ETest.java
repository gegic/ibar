package com.ktsnwt.project.team9.e2e.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.Keys;
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

import com.ktsnwt.project.team9.e2e.pages.ForgotPasswordPage;
import com.ktsnwt.project.team9.e2e.pages.HomePage;
import com.ktsnwt.project.team9.e2e.pages.LoginPage;
import com.ktsnwt.project.team9.e2e.pages.SignUpPage;
import com.ktsnwt.project.team9.model.RegisteredUser;
import com.ktsnwt.project.team9.services.implementations.RegisteredUserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource("classpath:test.properties")
public class AuthE2ETest {

	@Autowired
	private RegisteredUserService regService;
	
	public static final String BASE_URL = "https://localhost:4200";

	private WebDriver driver;
	
	private SignUpPage signUpPage;
	
	private HomePage homePage;

	private LoginPage signInPage;
	
	private ForgotPasswordPage forgotPasswordPage;
	
	@Before
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
		
		ChromeOptions handlingSSL = new ChromeOptions();
		handlingSSL.setAcceptInsecureCerts(true);
		
		driver = new ChromeDriver(handlingSSL);

		driver.navigate().to(BASE_URL);

		driver.manage().window().maximize();

		signUpPage = PageFactory.initElements(driver, SignUpPage.class);
		homePage = PageFactory.initElements(driver, HomePage.class);
		signInPage = PageFactory.initElements(driver, LoginPage.class);
		forgotPasswordPage = PageFactory.initElements(driver, ForgotPasswordPage.class);
	}
	
	private void loginSetUp() {
		driver.navigate().to(BASE_URL + "/auth/sign-in");

		signInPage.ensureIsDisplayedEmail();

		signInPage.getEmail().sendKeys("email_adresa1@gmail.com");
		signInPage.getPassword().sendKeys("sifra123");

		signInPage.getSignIn().click();
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void signUp_WithValidParams_ShouldSuccess() {
		int size = ((List<RegisteredUser>) regService.getAll()).size();
		
		homePage.ensureIsDisplayedSignUpNavigation();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getSignUpPage().click();
		
		signUpPage.ensureIsDisplayedEmail();
		
		assertEquals("https://localhost:4200/auth/sign-up", driver.getCurrentUrl());
		
		signUpPage.getFirstName().sendKeys("Marko");
		signUpPage.getLastName().sendKeys("Markovic");
		signUpPage.getUsername().sendKeys("markic98");
		signUpPage.getEmail().sendKeys("marko.markovic@gmail.com");
		signUpPage.getPassword().sendKeys("marko98");
		signUpPage.getSignUp().click();
		
		signUpPage.ensureIsDisplayedMessage();
		assertEquals("Activation link sent. Check your email.", signUpPage.getMessage().getText());
		assertEquals("https://localhost:4200/auth/sign-up", driver.getCurrentUrl());
		assertEquals(size + 1, ((List<RegisteredUser>) regService.getAll()).size());
		
		driver.close();
	}
	
	@Test
	public void signUp_WithExistingUsername_ShouldFail() {
		int size = ((List<RegisteredUser>) regService.getAll()).size();
		
		homePage.ensureIsDisplayedSignUpNavigation();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getSignUpPage().click();
		
		signUpPage.ensureIsDisplayedEmail();
		
		assertEquals("https://localhost:4200/auth/sign-up", driver.getCurrentUrl());
		
		signUpPage.getFirstName().sendKeys("Marko");
		signUpPage.getLastName().sendKeys("Markovic");
		signUpPage.getUsername().sendKeys("user 20");
		signUpPage.getEmail().sendKeys("marko.markovic@gmail.com");
		signUpPage.getPassword().sendKeys("marko98");
		signUpPage.getSignUp().click();
		
		signUpPage.ensureIsDisplayedMessage();
		assertEquals("Username already exists.", signUpPage.getMessage().getText());
		assertEquals("https://localhost:4200/auth/sign-up", driver.getCurrentUrl());
		assertEquals(size, ((List<RegisteredUser>) regService.getAll()).size());
		
		driver.close();
	}
	
	@Test
	public void signUp_WithExistingEmail_ShouldFail() {
		int size = ((List<RegisteredUser>) regService.getAll()).size();
		
		homePage.ensureIsDisplayedSignUpNavigation();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getSignUpPage().click();
		
		signUpPage.ensureIsDisplayedEmail();
		
		assertEquals("https://localhost:4200/auth/sign-up", driver.getCurrentUrl());
		
		signUpPage.getFirstName().sendKeys("Marko");
		signUpPage.getLastName().sendKeys("Markovic");
		signUpPage.getUsername().sendKeys("markic98");
		signUpPage.getEmail().sendKeys("email_adresa1@gmail.com");
		signUpPage.getPassword().sendKeys("marko98");
		signUpPage.getSignUp().click();
		
		signUpPage.ensureIsDisplayedMessage();
		assertEquals("Email already exists.", signUpPage.getMessage().getText());
		assertEquals("https://localhost:4200/auth/sign-up", driver.getCurrentUrl());
		assertEquals(size, ((List<RegisteredUser>) regService.getAll()).size());
		
		driver.close();
	}
	
	@Test
	public void signUp_WithInvalidEmail_ShouldFail() {
		int size = ((List<RegisteredUser>) regService.getAll()).size();
		
		homePage.ensureIsDisplayedSignUpNavigation();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getSignUpPage().click();
		
		signUpPage.ensureIsDisplayedEmail();
		
		assertEquals("https://localhost:4200/auth/sign-up", driver.getCurrentUrl());
		
		signUpPage.getFirstName().sendKeys("Marko");
		signUpPage.getLastName().sendKeys("Markovic");
		signUpPage.getUsername().sendKeys("markic98");
		signUpPage.getEmail().sendKeys("email_adresa1gmail.com");
		signUpPage.getPassword().sendKeys("marko98");
		signUpPage.getSignUp().click();
		
		assertEquals(true, signUpPage.getEmailError().isDisplayed());
		assertEquals("https://localhost:4200/auth/sign-up", driver.getCurrentUrl());
		assertEquals(size, ((List<RegisteredUser>) regService.getAll()).size());
		
		driver.close();
	}
	
	@Test
	public void signUp_WithShortPassword_ShouldFail() {
		int size = ((List<RegisteredUser>) regService.getAll()).size();
		
		homePage.ensureIsDisplayedSignUpNavigation();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getSignUpPage().click();
		
		signUpPage.ensureIsDisplayedEmail();
		
		assertEquals("https://localhost:4200/auth/sign-up", driver.getCurrentUrl());
		
		signUpPage.getFirstName().sendKeys("Marko");
		signUpPage.getLastName().sendKeys("Markovic");
		signUpPage.getUsername().sendKeys("markic98");
		signUpPage.getEmail().sendKeys("marko.markovic@gmail.com");
		signUpPage.getPassword().sendKeys("marko");
		signUpPage.getSignUp().click();
		
		assertEquals(true, signUpPage.getPasswordLenError().isDisplayed());
		assertEquals("https://localhost:4200/auth/sign-up", driver.getCurrentUrl());
		assertEquals(size, ((List<RegisteredUser>) regService.getAll()).size());
		
		driver.close();
	}
	
	@Test
	public void signIn_WithValidParams_ShouldSuccess() {
		homePage.ensureIsDisplayedSignUpNavigation();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getSignInPage().click();
		
		signInPage.ensureIsDisplayedEmail();
		
		assertEquals("https://localhost:4200/auth/sign-in", driver.getCurrentUrl());

		signInPage.getEmail().sendKeys("email_adresa1@gmail.com");
		signInPage.getPassword().sendKeys("sifra123");

		signInPage.getSignIn().click();
		
		homePage.ensureIsDisplayedSearch();
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		driver.close();
	}
	
	@Test
	public void signIn_WithInvalidParams_ShouldFail() {
		homePage.ensureIsDisplayedSignUpNavigation();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getSignInPage().click();
		
		signInPage.ensureIsDisplayedEmail();
		
		assertEquals("https://localhost:4200/auth/sign-in", driver.getCurrentUrl());

		signInPage.getEmail().sendKeys("aleksa.g@gmail.com");
		signInPage.getPassword().sendKeys("sifra123");

		signInPage.getSignIn().click();
		
		assertEquals("https://localhost:4200/auth/sign-in", driver.getCurrentUrl());
		
		signInPage.ensureIsDisplayedMessage();
		assertEquals("Incorrect email or password.", signInPage.getMessage().getText());
		driver.close();
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void forgotPassword_WithValidParams_ShouldSuccess() {
		homePage.ensureIsDisplayedSignInNavigation();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getSignInPage().click();
		
		signInPage.ensureIsDisplayedForgotPasswordLink();
		assertEquals("https://localhost:4200/auth/sign-in", driver.getCurrentUrl());
		
		signInPage.getForgotPasswordLink().click();
		
		assertEquals("https://localhost:4200/auth/forgot-password", driver.getCurrentUrl());
		
		forgotPasswordPage.getEmail().sendKeys("email_adresa1@gmail.com");
		forgotPasswordPage.getSend().click();
		
		forgotPasswordPage.ensureIsDisplayedMessage();
		assertEquals("New password sent. Check your email.", forgotPasswordPage.getMessage().getText());
		assertEquals("https://localhost:4200/auth/forgot-password", driver.getCurrentUrl());
		
		driver.close();
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void forgotPassword_WithNonExistingEmail_ShouldFail() {
		homePage.ensureIsDisplayedSignInNavigation();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getSignInPage().click();
		
		signInPage.ensureIsDisplayedForgotPasswordLink();
		assertEquals("https://localhost:4200/auth/sign-in", driver.getCurrentUrl());
		
		signInPage.getForgotPasswordLink().click();
		
		assertEquals("https://localhost:4200/auth/forgot-password", driver.getCurrentUrl());
		
		forgotPasswordPage.getEmail().sendKeys("ne_postojeci_email@gmail.com");
		forgotPasswordPage.getSend().click();
		
		forgotPasswordPage.ensureIsDisplayedMessage();
		assertEquals("That email address is not associated with personal user account.", forgotPasswordPage.getMessage().getText());
		assertEquals("https://localhost:4200/auth/forgot-password", driver.getCurrentUrl());
		
		driver.close();
	}
	
	@Test
	public void ProfileInfoPreview_WithValidParams_ShouldSuccess() {
		loginSetUp();
		
		homePage.ensureIsDisplayedProfileButton();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getProfileButton().click();
		homePage.ensureIsDisplayedEditButton();
		homePage.ensureIsDisplayedEmailProfile();
		
		assertEquals("Email: email_adresa1@gmail.com", homePage.getEmailProfile().getText());
		assertEquals("Username: admin 1", homePage.getUsernameProfile().getText());
		assertEquals("First name: admin1", homePage.getFirstNameProfile().getText());
		assertEquals("Last name: admin1", homePage.getLastNameProfile().getText());
		driver.close();
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void ProfileChange_WithValidParams_ShouldSuccess() {
		loginSetUp();
		
		homePage.ensureIsDisplayedProfileButton();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getProfileButton().click();
		homePage.ensureIsDisplayedEditButton();
		homePage.getEditButton().click();
		
		homePage.ensureIsDisplayedEmailProfileInput();
		
		homePage.getUsernameProfileInput().sendKeys(Keys.CONTROL, Keys.SHIFT, Keys.ARROW_LEFT, Keys.ARROW_LEFT);
		homePage.getUsernameProfileInput().sendKeys(Keys.BACK_SPACE);
		homePage.getUsernameProfileInput().sendKeys("ne_postojeci_username");
		homePage.getFirstNameProfileInput().sendKeys("ime");
		homePage.getLastNameProfileInput().sendKeys("prezime");
		
		homePage.ensureIsDisplayedSaveProfileButton();
		homePage.getSaveProfileButton().click();
		
		homePage.ensureIsDisplayedMessage();
		assertEquals("You changed account information successfully.", homePage.getMessage().getText());
		
		driver.close();
	}
	
	@Test
	public void ProfileChange_WithExistingUsername_ShouldFail() {
		loginSetUp();
		
		homePage.ensureIsDisplayedProfileButton();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getProfileButton().click();
		homePage.ensureIsDisplayedEditButton();
		homePage.getEditButton().click();
		
		homePage.ensureIsDisplayedEmailProfileInput();
		
		homePage.getUsernameProfileInput().sendKeys(Keys.CONTROL, Keys.SHIFT, Keys.ARROW_LEFT, Keys.ARROW_LEFT);
		homePage.getUsernameProfileInput().sendKeys(Keys.BACK_SPACE);
		homePage.getUsernameProfileInput().sendKeys("user 20");
		
		homePage.ensureIsDisplayedSaveProfileButton();
		homePage.getSaveProfileButton().click();
		
		homePage.ensureIsDisplayedMessage();
		assertEquals("Username already taken", homePage.getMessage().getText());
		
		driver.close();
	}
	
	@Test
	public void ProfileChange_WithEmptyFirstName_ShouldFail() {
		loginSetUp();
		
		homePage.ensureIsDisplayedProfileButton();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getProfileButton().click();
		homePage.ensureIsDisplayedEditButton();
		homePage.getEditButton().click();
		
		homePage.ensureIsDisplayedEmailProfileInput();
		
		homePage.getFirstNameProfileInput().sendKeys(Keys.CONTROL, Keys.SHIFT, Keys.ARROW_LEFT, Keys.ARROW_LEFT);
		homePage.getFirstNameProfileInput().sendKeys(Keys.BACK_SPACE);
		
		homePage.ensureIsDisplayedSaveProfileButton();
		homePage.getSaveProfileButton().click();
		
		assertEquals(true, homePage.getFirstNameError().isDisplayed());
		
		driver.close();
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void ChangePassword_WithValidParams_ShouldSuccess() {
		loginSetUp();
		
		homePage.ensureIsDisplayedProfileCircleButton();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getProfileCircleButton().click();
		homePage.ensureIsDisplayedChangePasswordButton();
		homePage.getChangePasswordButton().click();
		
		homePage.ensureIsDisplayedOldPassword();
		homePage.getOldPassword().sendKeys("sifra123");
		homePage.getNewPassword().sendKeys("novasifra");
		homePage.getConfirmPassword().sendKeys("novasifra");
		
		homePage.ensureIsDisplayedSubmitPasswordButton();
		homePage.getSubmitPasswordButton().click();
		
		signInPage.ensureIsDisplayedEmail();
		assertEquals("https://localhost:4200/auth/sign-in", driver.getCurrentUrl());
		
		driver.close();
	}
	
	@Test
	public void ChangePassword_WithShortNewPassword_ShouldFail() {
		loginSetUp();
		
		homePage.ensureIsDisplayedProfileCircleButton();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getProfileCircleButton().click();
		homePage.ensureIsDisplayedChangePasswordButton();
		homePage.getChangePasswordButton().click();
		
		homePage.ensureIsDisplayedOldPassword();
		homePage.getOldPassword().sendKeys("sifra123");
		homePage.getNewPassword().sendKeys("nova");
		homePage.getConfirmPassword().sendKeys("nova");
		
		homePage.ensureIsDisplayedSubmitPasswordButton();
		homePage.getSubmitPasswordButton().click();
		
		homePage.ensureIsDisplayedSixCharacters();
		assertEquals(true, homePage.getSixCharacters().isDisplayed());
		
		driver.close();
	}
	
	@Test
	public void ChangePassword_WithInvalidConfirmPassword_ShouldFail() {
		loginSetUp();
		
		homePage.ensureIsDisplayedProfileCircleButton();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getProfileCircleButton().click();
		homePage.ensureIsDisplayedChangePasswordButton();
		homePage.getChangePasswordButton().click();
		
		homePage.ensureIsDisplayedOldPassword();
		homePage.getOldPassword().sendKeys("sifra123");
		homePage.getNewPassword().sendKeys("novasifra1");
		homePage.getConfirmPassword().sendKeys("novasifra2");
		
		homePage.ensureIsDisplayedSubmitPasswordButton();
		homePage.getSubmitPasswordButton().click();
		
		homePage.ensureIsDisplayedNoMatch();
		assertEquals(true, homePage.getNoMatch().isDisplayed());
		
		driver.close();
	}
	
	@Test
	public void ChangePassword_WithWrongOldPassword_ShouldFail() {
		loginSetUp();
		
		homePage.ensureIsDisplayedProfileCircleButton();
		
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getProfileCircleButton().click();
		homePage.ensureIsDisplayedChangePasswordButton();
		homePage.getChangePasswordButton().click();
		
		homePage.ensureIsDisplayedOldPassword();
		homePage.getOldPassword().sendKeys("pogresna");
		homePage.getNewPassword().sendKeys("novasifra");
		homePage.getConfirmPassword().sendKeys("novasifra");
		
		homePage.ensureIsDisplayedSubmitPasswordButton();
		homePage.getSubmitPasswordButton().click();
		
		homePage.ensureIsDisplayedMessage();
		assertEquals("Incorrect old password", homePage.getMessage().getText());
		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		driver.close();
	}
	
}
