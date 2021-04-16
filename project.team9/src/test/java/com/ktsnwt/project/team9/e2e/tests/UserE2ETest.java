package com.ktsnwt.project.team9.e2e.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.e2e.pages.HomePage;
import com.ktsnwt.project.team9.e2e.pages.LoginPage;
import com.ktsnwt.project.team9.e2e.pages.UserPage;
import com.ktsnwt.project.team9.model.Admin;
import com.ktsnwt.project.team9.services.implementations.AdminService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource("classpath:test.properties")
public class UserE2ETest {
	@Autowired
	AdminService adminService;
	
	private WebDriver driver;
	
	public static final String BASE_URL = "https://localhost:4200";
	
	private HomePage homePage;
	
	private LoginPage signInPage;
	
	private UserPage userPage;
	
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
		userPage = PageFactory.initElements(driver, UserPage.class);
	}
	
	private void signInSetUp() {
		driver.navigate().to(BASE_URL + "/auth/sign-in");

		signInPage.ensureIsDisplayedEmail();

		signInPage.getEmail().sendKeys("email_adresa1@gmail.com");
		signInPage.getPassword().sendKeys("sifra123");

		signInPage.getSignIn().click();
	}
	
	@Test
	public void searchAdmins_WithExistingValue_ShouldSuccess() {
		signInSetUp();

		homePage.ensureIsDisplayedCulturalOfferNavigation();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getUserPage().click();
		userPage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/users", driver.getCurrentUrl());
		
		userPage.getSearch().sendKeys("6");
		pause(2000);
		List<WebElement> rows = driver.findElements(By.tagName("tr"));
		
		assertEquals(1, rows.size());
		driver.close();
	}
	
	@Test
	public void searchAdmins_WithNonExistingValue_ShouldFail() {
		signInSetUp();

		homePage.ensureIsDisplayedCulturalOfferNavigation();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getUserPage().click();
		userPage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/users", driver.getCurrentUrl());
		
		userPage.getSearch().sendKeys("asdfghj");
		pause(2000);
		List<WebElement> rows = driver.findElements(By.tagName("tr"));
		
		assertEquals(0, rows.size());
		driver.close();
	}
	
	@Test
	public void searchRegUsers_WithExistingValue_ShouldSuccess() {
		signInSetUp();

		homePage.ensureIsDisplayedCulturalOfferNavigation();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getUserPage().click();
		userPage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/users", driver.getCurrentUrl());
		
		userPage.getRegsTab().click();
		userPage.getSearch().sendKeys("22");
		pause(2000);
		List<WebElement> rows = driver.findElements(By.tagName("tr"));
		
		assertEquals(1, rows.size());
		driver.close();
	}
	
	@Test
	public void searchRegUsers_WithNonExistingValue_ShouldFail() {
		signInSetUp();

		homePage.ensureIsDisplayedCulturalOfferNavigation();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getUserPage().click();
		userPage.ensureIsDisplayedSearch();
		userPage.ensureIsDisplayedRegUsersTab();
		
		assertEquals("https://localhost:4200/users", driver.getCurrentUrl());
		
		userPage.getRegsTab().click();
		userPage.getSearch().sendKeys("asdfghj");
		pause(2000);
		List<WebElement> rows = driver.findElements(By.tagName("tr"));
		
		assertEquals(0, rows.size());
		driver.close();
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void deleteAdmin_WithAdminWithNoCO_ShouldSuccess() {
		signInSetUp();
		int size = ((List<Admin>) adminService.getAll()).size();

		homePage.ensureIsDisplayedCulturalOfferNavigation();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getUserPage().click();
		userPage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/users", driver.getCurrentUrl());
		
		userPage.getSearch().sendKeys("6");
		pause(2000);
		
		userPage.getRemoveButton().click();
		
		userPage.ensureIsDisplayedOKRemoveButton();

		userPage.getOkButton().click();

		userPage.ensureIsDisplayedMessage();
		
		assertEquals("You have successfully deleted admin!",
				userPage.getMessage().getText());
		assertEquals(size - 1, ((List<Admin>) adminService.getAll()).size());
		driver.close();
	}
	
	@Test
	public void deleteAdmin_WithAdminWithNoCOCancel_ShouldSuccess() {
		signInSetUp();
		int size = ((List<Admin>) adminService.getAll()).size();

		homePage.ensureIsDisplayedCulturalOfferNavigation();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getUserPage().click();
		userPage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/users", driver.getCurrentUrl());
		
		userPage.getSearch().sendKeys("6");
		pause(2000);
		
		userPage.getRemoveButton().click();
		
		userPage.ensureIsDisplayedOKRemoveButton();

		userPage.getNoButton().click();
		
		assertEquals(size, ((List<Admin>) adminService.getAll()).size());
		driver.close();
	}
	
	@Test
	public void deleteAdmin_WithAdminWithCO_ShouldFail() {
		signInSetUp();
		int size = ((List<Admin>) adminService.getAll()).size();

		homePage.ensureIsDisplayedCulturalOfferNavigation();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getUserPage().click();
		userPage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/users", driver.getCurrentUrl());
		
		userPage.getSearch().sendKeys("admin 3");
		pause(2000);
		
		userPage.getRemoveButton().click();
		
		userPage.ensureIsDisplayedOKRemoveButton();

		userPage.getOkButton().click();
		
		userPage.ensureIsDisplayedMessage();
		
		assertEquals("Admin has cultural offers, so he can't be deleted",
				userPage.getMessage().getText());
		assertEquals(size, ((List<Admin>) adminService.getAll()).size());
		driver.close();
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void createAdmin_WithValidParams_ShouldSuccess() {
		signInSetUp();
		int size = ((List<Admin>) adminService.getAll()).size();

		homePage.ensureIsDisplayedCulturalOfferNavigation();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getUserPage().click();
		userPage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/users", driver.getCurrentUrl());
		
		userPage.getAddButton().click();
		userPage.ensureIsDisplayedEmail();
		
		userPage.getEmailAdmin().sendKeys("marko.markovic@gmail.com");
		userPage.getUsernameAdmin().sendKeys("markic98");
		userPage.getFirstNameAdmin().sendKeys("Marko");
		userPage.getLastNameAdmin().sendKeys("Markovic");
		userPage.getAddBtn().click();
		
		userPage.ensureIsDisplayedMessage();
		
		assertEquals("Administrator added successfully.",
				userPage.getMessage().getText());
		assertEquals(size + 1, ((List<Admin>) adminService.getAll()).size());
		driver.close();
	}
	
	@Test
	public void createAdmin_WithValidParamsAndClose_ShouldSuccess() {
		signInSetUp();
		int size = ((List<Admin>) adminService.getAll()).size();

		homePage.ensureIsDisplayedCulturalOfferNavigation();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getUserPage().click();
		userPage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/users", driver.getCurrentUrl());
		
		userPage.getAddButton().click();
		userPage.ensureIsDisplayedEmail();
		
		userPage.getEmailAdmin().sendKeys("marko.markovic@gmail.com");
		userPage.getUsernameAdmin().sendKeys("markic98");
		userPage.getFirstNameAdmin().sendKeys("Marko");
		userPage.getLastNameAdmin().sendKeys("Markovic");
		userPage.getCloseBtn().click();
		
		assertEquals(size, ((List<Admin>) adminService.getAll()).size());
		driver.close();
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void createAdmin_WithExistingEmail_ShouldFail() {
		signInSetUp();
		int size = ((List<Admin>) adminService.getAll()).size();

		homePage.ensureIsDisplayedCulturalOfferNavigation();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getUserPage().click();
		userPage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/users", driver.getCurrentUrl());
		
		userPage.getAddButton().click();
		userPage.ensureIsDisplayedEmail();
		
		userPage.getEmailAdmin().sendKeys("email_adresa2@gmail.com");
		userPage.getUsernameAdmin().sendKeys("markic98");
		userPage.getFirstNameAdmin().sendKeys("Marko");
		userPage.getLastNameAdmin().sendKeys("Markovic");
		userPage.getAddBtn().click();
		
		userPage.ensureIsDisplayedMessage();
		
		assertEquals("User with this email already exists.",
				userPage.getMessage().getText());
		assertEquals(size, ((List<Admin>) adminService.getAll()).size());
		driver.close();
	}
	
	@Test
	public void createAdmin_WithExistingUsername_ShouldFail() {
		signInSetUp();
		int size = ((List<Admin>) adminService.getAll()).size();

		homePage.ensureIsDisplayedCulturalOfferNavigation();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getUserPage().click();
		userPage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/users", driver.getCurrentUrl());
		
		userPage.getAddButton().click();
		userPage.ensureIsDisplayedEmail();
		
		userPage.getEmailAdmin().sendKeys("marko.markovic@gmail.com");
		userPage.getUsernameAdmin().sendKeys("admin 2");
		userPage.getFirstNameAdmin().sendKeys("Marko");
		userPage.getLastNameAdmin().sendKeys("Markovic");
		userPage.getAddBtn().click();
		
		userPage.ensureIsDisplayedMessage();
		
		assertEquals("User with this username already exists.",
				userPage.getMessage().getText());
		assertEquals(size, ((List<Admin>) adminService.getAll()).size());
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
