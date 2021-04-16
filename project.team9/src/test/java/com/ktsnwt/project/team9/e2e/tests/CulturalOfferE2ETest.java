package com.ktsnwt.project.team9.e2e.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
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

import com.ktsnwt.project.team9.e2e.pages.CreateUpdateCulturalOfferPage;
import com.ktsnwt.project.team9.e2e.pages.CulturalOffersPage;
import com.ktsnwt.project.team9.e2e.pages.HomePage;
import com.ktsnwt.project.team9.e2e.pages.LoginPage;
import com.ktsnwt.project.team9.e2e.pages.ReviewCulturalOfferPage;
import com.ktsnwt.project.team9.model.CulturalOffer;
import com.ktsnwt.project.team9.services.implementations.CulturalOfferService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource("classpath:test.properties")
public class CulturalOfferE2ETest {

	@Autowired
	private CulturalOfferService culturalOfferService;

	public static final String BASE_URL = "https://localhost:4200";

	private WebDriver driver;

	private LoginPage loginPage;

	private HomePage homePage;

	private CulturalOffersPage culturalOffersPage;

	private CreateUpdateCulturalOfferPage createUpdateCulturalOfferPage;

	private ReviewCulturalOfferPage reviewCulturalOfferPage;

	@Before
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
		ChromeOptions handlingSSL = new ChromeOptions();
		handlingSSL.setAcceptInsecureCerts(true);
		driver = new ChromeDriver(handlingSSL);

		driver.navigate().to(BASE_URL);

		driver.manage().window().maximize();

		loginPage = PageFactory.initElements(driver, LoginPage.class);
		homePage = PageFactory.initElements(driver, HomePage.class);
		culturalOffersPage = PageFactory.initElements(driver, CulturalOffersPage.class);
		createUpdateCulturalOfferPage = PageFactory.initElements(driver, CreateUpdateCulturalOfferPage.class);
		reviewCulturalOfferPage = PageFactory.initElements(driver, ReviewCulturalOfferPage.class);

	}

	private void loginSetUp() {
		driver.navigate().to(BASE_URL + "/auth/sign-in");

		loginPage.ensureIsDisplayedEmail();

		loginPage.getEmail().sendKeys("email_adresa1@gmail.com");
		loginPage.getPassword().sendKeys("sifra123");

		loginPage.getSignIn().click();

	}

	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void createCulturalOffer_withValidParameteres_ShouldSuccess() throws InterruptedException {
		int size = ((List<CulturalOffer>) culturalOfferService.getAll()).size();

		loginSetUp();

		homePage.ensureIsDisplayedCulturalOfferNavigation();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());

		homePage.getCulturalOffersPage().click();

		culturalOffersPage.ensureIsDisplayedCreateNewCulturalOfferButton();

		assertEquals("https://localhost:4200/cultural-offers", driver.getCurrentUrl());

		culturalOffersPage.getCreateCulturalOfferButton().click();

		createUpdateCulturalOfferPage.ensureIsDisplayedNameInput();

		assertEquals("https://localhost:4200/cultural-offers/create", driver.getCurrentUrl());

		createUpdateCulturalOfferPage.getName().sendKeys("neko ime");
		createUpdateCulturalOfferPage.getLocation().click();
		createUpdateCulturalOfferPage.getLocation().sendKeys("crkvina");
		Thread.sleep(1000);
		createUpdateCulturalOfferPage.getLocation().sendKeys(Keys.ARROW_DOWN);
		createUpdateCulturalOfferPage.getLocation().sendKeys(Keys.ENTER);
		createUpdateCulturalOfferPage.getCategory().click();
		createUpdateCulturalOfferPage.getCategory().findElement(By.xpath("//mat-option[@id='select_category_1']/span"))
				.click();
		createUpdateCulturalOfferPage.getDescription().sendKeys("opis");
		String absolutePath = FileSystems.getDefault().getPath("src/test/resources/uploadedImages/comment_slika6.jpg")
				.normalize().toAbsolutePath().toString();
		createUpdateCulturalOfferPage.getImage().sendKeys(absolutePath);
		createUpdateCulturalOfferPage.getCreateButton().click();

		reviewCulturalOfferPage.ensureIsDisplayedName();

		assertEquals(reviewCulturalOfferPage.getName().getText(), "neko ime");
		assertEquals("https://localhost:4200/cultural-offers/21", driver.getCurrentUrl());
		assertEquals(size + 1, ((List<CulturalOffer>) culturalOfferService.getAll()).size());

		driver.close();
	}

	@Test
	public void createCulturalOffer_withInvalidParameteres_ShouldFail() throws InterruptedException {
		int size = ((List<CulturalOffer>) culturalOfferService.getAll()).size();

		loginSetUp();

		homePage.ensureIsDisplayedCulturalOfferNavigation();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());

		homePage.getCulturalOffersPage().click();

		culturalOffersPage.ensureIsDisplayedCreateNewCulturalOfferButton();

		assertEquals("https://localhost:4200/cultural-offers", driver.getCurrentUrl());

		culturalOffersPage.getCreateCulturalOfferButton().click();

		createUpdateCulturalOfferPage.ensureIsDisplayedNameInput();

		assertEquals("https://localhost:4200/cultural-offers/create", driver.getCurrentUrl());

		createUpdateCulturalOfferPage.getName().sendKeys("neko ime");
		createUpdateCulturalOfferPage.getLocation().click();
		createUpdateCulturalOfferPage.getLocation().sendKeys("danila");
		Thread.sleep(1000);
		createUpdateCulturalOfferPage.getLocation().sendKeys(Keys.ARROW_DOWN);
		createUpdateCulturalOfferPage.getLocation().sendKeys(Keys.ENTER);
		createUpdateCulturalOfferPage.getCategory().click();
		createUpdateCulturalOfferPage.getCategory().findElement(By.xpath("//mat-option[@id='select_category_1']/span"))
				.click();
		createUpdateCulturalOfferPage.getDescription().sendKeys("opis");
		String absolutePath = FileSystems.getDefault().getPath("src/test/resources/uploadedImages/comment_slika6.jpg")
				.normalize().toAbsolutePath().toString();
		createUpdateCulturalOfferPage.getImage().sendKeys(absolutePath);
		createUpdateCulturalOfferPage.getCreateButton().click();

		assertEquals(size, ((List<CulturalOffer>) culturalOfferService.getAll()).size());
		assertEquals("https://localhost:4200/cultural-offers/create", driver.getCurrentUrl());

		createUpdateCulturalOfferPage.ensureIsDisplayedMessage();

		assertEquals(createUpdateCulturalOfferPage.getMessage().getText(),
				"Location needs to be unique. Choose another location.");

		driver.close();
	}

	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void updateCulturalOffer_withValidParameteres_ShouldSuccess() throws InterruptedException {
		loginSetUp();

		homePage.ensureIsDisplayedCulturalOfferNavigation();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());

		homePage.getCulturalOffersPage().click();

		culturalOffersPage.ensureIsDisplayedUpdateCulturalOfferButton();

		assertEquals("https://localhost:4200/cultural-offers", driver.getCurrentUrl());

		culturalOffersPage.getUpdateCulturalOfferButton().click();

		createUpdateCulturalOfferPage.ensureIsDisplayedNameInput();

		assertEquals("https://localhost:4200/cultural-offers/update/1", driver.getCurrentUrl());

		createUpdateCulturalOfferPage.getLocation().click();
		Thread.sleep(1000);
		createUpdateCulturalOfferPage.getLocation().sendKeys(Keys.CONTROL, Keys.SHIFT, Keys.ARROW_LEFT,
				Keys.ARROW_LEFT);
		createUpdateCulturalOfferPage.getLocation().sendKeys(Keys.BACK_SPACE);
		createUpdateCulturalOfferPage.getLocation().sendKeys("crkvina");
		Thread.sleep(1000);
		createUpdateCulturalOfferPage.getLocation().sendKeys(Keys.ARROW_DOWN);
		createUpdateCulturalOfferPage.getLocation().sendKeys(Keys.TAB);
		Thread.sleep(1000);
		createUpdateCulturalOfferPage.getUpdateButton().click();

		reviewCulturalOfferPage.ensureIsDisplayedName();

		assertEquals(reviewCulturalOfferPage.getName().getText(), "Manastir 1");
		assertEquals("https://localhost:4200/cultural-offers/1", driver.getCurrentUrl());
		assertEquals("Crkvina, Bosnia and Herzegovina",
				culturalOfferService.getById(1L).getGeolocation().getLocation());

		driver.close();
	}

	@Test
	public void updateCulturalOffer_withInvalidParameteres_ShouldFail() throws InterruptedException {
		loginSetUp();

		homePage.ensureIsDisplayedCulturalOfferNavigation();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());

		homePage.getCulturalOffersPage().click();

		culturalOffersPage.ensureIsDisplayedUpdateCulturalOfferButton();

		assertEquals("https://localhost:4200/cultural-offers", driver.getCurrentUrl());

		culturalOffersPage.getUpdateCulturalOfferButton().click();

		createUpdateCulturalOfferPage.ensureIsDisplayedNameInput();

		assertEquals("https://localhost:4200/cultural-offers/update/1", driver.getCurrentUrl());

		createUpdateCulturalOfferPage.getLocation().click();
		Thread.sleep(1000);
		createUpdateCulturalOfferPage.getLocation().sendKeys(Keys.CONTROL, Keys.SHIFT, Keys.ARROW_LEFT,
				Keys.ARROW_LEFT);
		createUpdateCulturalOfferPage.getLocation().sendKeys(Keys.BACK_SPACE);
		createUpdateCulturalOfferPage.getLocation().sendKeys("danila");
		Thread.sleep(1000);
		createUpdateCulturalOfferPage.getLocation().sendKeys(Keys.ARROW_DOWN);
		createUpdateCulturalOfferPage.getLocation().sendKeys(Keys.TAB);
		Thread.sleep(1000);
		createUpdateCulturalOfferPage.getUpdateButton().click();

		createUpdateCulturalOfferPage.ensureIsDisplayedMessage();

		assertEquals("Location need to be unique. Choose another location.",
				createUpdateCulturalOfferPage.getMessage().getText());
		assertEquals("Neka lokacija 1", culturalOfferService.getById(1L).getGeolocation().getLocation());
		driver.close();
	}

	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void deleteCulturalOffer_ShouldSuccess() throws InterruptedException, IOException {
		int size = ((List<CulturalOffer>) culturalOfferService.getAll()).size();

		Path path = Paths.get("src/main/resources/uploadedImages/imageRNG.jpg");
		byte[] content = Files.readAllBytes(path);
		File fileExist = new File(path.toString());

		loginSetUp();

		homePage.ensureIsDisplayedCulturalOfferNavigation();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());

		homePage.getCulturalOffersPage().click();

		culturalOffersPage.ensureIsDisplayedCreateNewCulturalOfferButton();

		assertEquals("https://localhost:4200/cultural-offers", driver.getCurrentUrl());

		culturalOffersPage.ensureIsDisplayedSecondPage();

		culturalOffersPage.getSecondPage().click();

		culturalOffersPage.ensureIsDisplayedRemoveCulturalOfferButton();

		culturalOffersPage.getRemoveCulturalOfferButton().click();

		culturalOffersPage.ensureIsDisplayedOKRemoveButton();

		culturalOffersPage.getOkButton().click();

		culturalOffersPage.ensureIsDisplayedMessage();

		assertEquals("You have successfully deleted cultural offer!",
				createUpdateCulturalOfferPage.getMessage().getText());
		assertEquals(size - 1, ((List<CulturalOffer>) culturalOfferService.getAll()).size());
		OutputStream outputStream = new FileOutputStream(fileExist);
		outputStream.write(content);
		outputStream.close();

		driver.close();
	}

	@Test
	public void deleteCulturalOffer_PressCancelButton_ShouldSuccess() throws InterruptedException, IOException {
		int size = ((List<CulturalOffer>) culturalOfferService.getAll()).size();

		loginSetUp();

		homePage.ensureIsDisplayedCulturalOfferNavigation();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());

		homePage.getCulturalOffersPage().click();

		culturalOffersPage.ensureIsDisplayedCreateNewCulturalOfferButton();

		assertEquals("https://localhost:4200/cultural-offers", driver.getCurrentUrl());

		culturalOffersPage.ensureIsDisplayedSecondPage();

		culturalOffersPage.getSecondPage().click();

		culturalOffersPage.ensureIsDisplayedRemoveCulturalOfferButton();

		culturalOffersPage.getRemoveCulturalOfferButton().click();

		culturalOffersPage.ensureIsDisplayedCancelRemoveButton();

		culturalOffersPage.getCancelButton().click();

		assertEquals(size, ((List<CulturalOffer>) culturalOfferService.getAll()).size());

		driver.close();
	}

	@Test
	public void reviewCulturalOfferThroughHomePage_ShouldShowAllInformationAboutConcreteCulturalOffer()
			throws InterruptedException, IOException {
		homePage.ensureIsDisplayedFestivalsTab();
		homePage.ensureIsDisplayedSearch();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getSearch().sendKeys("festival 100");
		
		homePage.getFestivalsTab().click();

		Thread.sleep(1000);
		
		homePage.ensureIsDisplayedMoreButton();
		
		homePage.getMore().click();
		
		reviewCulturalOfferPage.ensureIsDisplayedName();

		assertEquals("https://localhost:4200/cultural-offers/3", driver.getCurrentUrl());
		assertEquals("Festival 100", reviewCulturalOfferPage.getName().getText());

		driver.close();
	}

	@Test
	public void reviewCulturalOfferThroughCulturalOffersPage_ShouldShowAllInformationAboutConcreteCulturalOffer()
			throws InterruptedException, IOException {
		homePage.ensureIsDisplayedCulturalOfferNavigation();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());

		homePage.getCulturalOffersPage().click();

		culturalOffersPage.ensureIsDisplayedSelectField();

		assertEquals("https://localhost:4200/cultural-offers", driver.getCurrentUrl());

		culturalOffersPage.getSelect().click();
		culturalOffersPage.getSelect().findElement(By.xpath("//mat-option[2]/span")).click();

		culturalOffersPage.ensureIsDisplayedMoreButton();

		culturalOffersPage.getMore().click();

		reviewCulturalOfferPage.ensureIsDisplayedName();

		assertEquals("https://localhost:4200/cultural-offers/1", driver.getCurrentUrl());
		assertEquals("Manastir 1", reviewCulturalOfferPage.getName().getText());

		driver.close();
	}
}
