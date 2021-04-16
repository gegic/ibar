package com.ktsnwt.project.team9.e2e.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
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

import com.ktsnwt.project.team9.e2e.pages.CategoryPage;
import com.ktsnwt.project.team9.e2e.pages.HomePage;
import com.ktsnwt.project.team9.e2e.pages.LoginPage;
import com.ktsnwt.project.team9.model.Category;
import com.ktsnwt.project.team9.services.implementations.CategoryService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource("classpath:test.properties")
public class CategoryE2ETest {
	@Autowired
	CategoryService categoryService;
	
	private WebDriver driver;
	
	public static final String BASE_URL = "https://localhost:4200";
	
	private HomePage homePage;
	
	private LoginPage signInPage;
	
	private CategoryPage categoryPage;
	
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
		categoryPage = PageFactory.initElements(driver, CategoryPage.class);
	}
	
	private void signInSetUp() {
		driver.navigate().to(BASE_URL + "/auth/sign-in");

		signInPage.ensureIsDisplayedEmail();

		signInPage.getEmail().sendKeys("email_adresa1@gmail.com");
		signInPage.getPassword().sendKeys("sifra123");

		signInPage.getSignIn().click();
	}
	
	@Test
	public void searchCategories_WithExistingValue_ShouldSuccess() {
		signInSetUp();

		homePage.ensureIsDisplayedCategoriesNavigation();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getCategoryPage().click();
		categoryPage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/categories", driver.getCurrentUrl());
		
		categoryPage.getSearch().sendKeys("Manastiri");
		pause(2000);
		List<WebElement> rows = driver.findElements(By.tagName("tr"));
		
		assertEquals(1, rows.size());
		driver.close();
	}
	
	@Test
	public void searchCategories_WithNonExistingValue_ShouldFail() {
		signInSetUp();

		homePage.ensureIsDisplayedCategoriesNavigation();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getCategoryPage().click();
		categoryPage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/categories", driver.getCurrentUrl());
		
		categoryPage.getSearch().sendKeys("RNG");
		pause(2000);
		List<WebElement> rows = driver.findElements(By.tagName("tr"));
		
		pause(2000);
		
		assertEquals(0, rows.size());
		driver.close();
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void deleteCategory_WithCategoryWithNoCO_ShouldSuccess() {
		signInSetUp();
		
		int size = ((List<Category>) categoryService.getAll()).size();

		homePage.ensureIsDisplayedCategoriesNavigation();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getCategoryPage().click();
		categoryPage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/categories", driver.getCurrentUrl());
		
		categoryPage.getSearch().sendKeys("Kategorija za brisanje");
		pause(2000);
		
		categoryPage.getRemoveButton().click();
		
		categoryPage.ensureIsDisplayedOKRemoveButton();

		categoryPage.getOkButton().click();

		categoryPage.ensureIsDisplayedMessage();
		
		assertEquals("You have successfully deleted category!",
				categoryPage.getMessage().getText());
		assertEquals(size - 1, ((List<Category>) categoryService.getAll()).size());
		driver.close();
	}
	
	@Test
	public void deleteCategory_WithCategoryWithNoCOCancel_ShouldSuccess() {
		signInSetUp();
		int size = ((List<Category>) categoryService.getAll()).size();

		homePage.ensureIsDisplayedCategoriesNavigation();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getCategoryPage().click();
		categoryPage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/categories", driver.getCurrentUrl());
		
		categoryPage.getSearch().sendKeys("Manastiri");
		pause(2000);
		
		categoryPage.getRemoveButton().click();
		
		categoryPage.ensureIsDisplayedOKRemoveButton();

		categoryPage.getNoButton().click();
		
		assertEquals(size, ((List<Category>) categoryService.getAll()).size());
		driver.close();
	}
	
	@Test
	public void deleteCategory_WithCategoryWithCO_ShouldFail() {
		signInSetUp();
		int size = ((List<Category>) categoryService.getAll()).size();

		homePage.ensureIsDisplayedCategoriesNavigation();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getCategoryPage().click();
		categoryPage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/categories", driver.getCurrentUrl());
		
		categoryPage.getSearch().sendKeys("Manastir");
		pause(2000);
		
		categoryPage.getRemoveButton().click();
		
		categoryPage.ensureIsDisplayedOKRemoveButton();

		categoryPage.getOkButton().click();
		
		categoryPage.ensureIsDisplayedMessage();
		
		assertEquals("Can't delete this, category has cultural offer!",
				categoryPage.getMessage().getText());
		assertEquals(size, ((List<Category>) categoryService.getAll()).size());
		driver.close();
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void createCategory_WithValidParams_ShouldSuccess() {
		signInSetUp();
		int size = ((List<Category>) categoryService.getAll()).size();

		homePage.ensureIsDisplayedCategoriesNavigation();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getCategoryPage().click();
		categoryPage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/categories", driver.getCurrentUrl());
		
		categoryPage.getAddButton().click();
		categoryPage.ensureIsDisplayedName();
		
		categoryPage.getCategoryName().sendKeys("New category");
		categoryPage.getCategoryDescription().sendKeys("New description");
		pause(1000);
		categoryPage.getAddOrUpdateBtn().click();
		
		categoryPage.ensureIsDisplayedMessage();
		
		categoryPage.ensureIsDisplayedSearch();
		categoryPage.getSearch().sendKeys("New category");
		pause(2000);
		
		List<WebElement> data = driver.findElements(By.tagName("td"));
		String newName = data.get(0).getText();
		String newDesc = data.get(1).getText();
		
		assertEquals("Category added successfully",
				categoryPage.getMessage().getText());
		assertEquals(size + 1, ((List<Category>) categoryService.getAll()).size());
		assertEquals(newName, "New category");
		assertEquals(newDesc, "New description");
		driver.close();
	}
	
	@Test
	public void createCategory_WithValidParamsAndClose_ShouldSuccess() {
		signInSetUp();
		int size = ((List<Category>) categoryService.getAll()).size();

		homePage.ensureIsDisplayedCategoriesNavigation();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getCategoryPage().click();
		categoryPage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/categories", driver.getCurrentUrl());
		
		categoryPage.getAddButton().click();
		categoryPage.ensureIsDisplayedName();
		
		categoryPage.getCategoryName().sendKeys("New category");
		categoryPage.getCategoryDescription().sendKeys("New description");
		categoryPage.getCloseBtn().click();
		
		assertEquals(size, ((List<Category>) categoryService.getAll()).size());
		driver.close();
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void createCategory_WithExistingName_ShouldFail() {
		signInSetUp();
		int size = ((List<Category>) categoryService.getAll()).size();

		homePage.ensureIsDisplayedCategoriesNavigation();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getCategoryPage().click();
		categoryPage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/categories", driver.getCurrentUrl());
		
		categoryPage.getAddButton().click();
		categoryPage.ensureIsDisplayedName();
		
		categoryPage.getCategoryName().sendKeys("Manastiri");
		categoryPage.getCategoryDescription().sendKeys("Neka desc");
		pause(1000);
		categoryPage.getAddOrUpdateBtn().click();
		
		categoryPage.ensureIsDisplayedMessage();
		
		assertEquals("Category already exist!",
				categoryPage.getMessage().getText());
		assertEquals(size, ((List<Category>) categoryService.getAll()).size());
		driver.close();
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void updateCategory_WithValidParams_ShouldSuccess() {
		signInSetUp();
		int size = ((List<Category>) categoryService.getAll()).size();

		homePage.ensureIsDisplayedCategoriesNavigation();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getCategoryPage().click();
		categoryPage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/categories", driver.getCurrentUrl());
		
		categoryPage.getUpdateButton().click();
		categoryPage.ensureIsDisplayedName();
		
		categoryPage.getCategoryName().clear();
		categoryPage.getCategoryName().sendKeys("New category");
		categoryPage.getCategoryDescription().clear();
		categoryPage.getCategoryDescription().sendKeys("New description");
		pause(1000);
		categoryPage.getAddOrUpdateBtn().click();
		
		categoryPage.ensureIsDisplayedMessage();
		
		categoryPage.ensureIsDisplayedSearch();
		categoryPage.getSearch().sendKeys("New category");
		pause(2000);

		List<WebElement> data = driver.findElements(By.tagName("td"));
		String newName = data.get(0).getText();
		String newDesc = data.get(1).getText();
		
		assertEquals("Category updated successfully",
				categoryPage.getMessage().getText());
		assertEquals(size, ((List<Category>) categoryService.getAll()).size());
		assertEquals(newName, "New category");
		assertEquals(newDesc, "New description");
		driver.close();
	}
	
	@Test
	public void updateCategory_WithValidParamsAndClose_ShouldSuccess() {
		signInSetUp();
		int size = ((List<Category>) categoryService.getAll()).size();

		homePage.ensureIsDisplayedCategoriesNavigation();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getCategoryPage().click();
		categoryPage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/categories", driver.getCurrentUrl());
		
		categoryPage.getUpdateButton().click();
		categoryPage.ensureIsDisplayedName();
		
		categoryPage.getCategoryName().sendKeys("New category");
		
		categoryPage.getCategoryDescription().sendKeys("New description");
		categoryPage.getCloseBtn().click();
		
		assertEquals(size, ((List<Category>) categoryService.getAll()).size());
		driver.close();
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void updateCategory_WithExistingName_ShouldFail() {
		signInSetUp();
		int size = ((List<Category>) categoryService.getAll()).size();

		homePage.ensureIsDisplayedCategoriesNavigation();

		assertEquals("https://localhost:4200/", driver.getCurrentUrl());
		
		homePage.getCategoryPage().click();
		categoryPage.ensureIsDisplayedSearch();
		
		assertEquals("https://localhost:4200/categories", driver.getCurrentUrl());
		
		categoryPage.getUpdateButton().click();
		categoryPage.ensureIsDisplayedName();
		
		categoryPage.getCategoryName().clear();
		categoryPage.getCategoryName().sendKeys("Festivali");
		categoryPage.getCategoryName().clear();
		categoryPage.getCategoryDescription().sendKeys("Neka desc");
		pause(1000);
		categoryPage.getAddOrUpdateBtn().click();
		
		categoryPage.ensureIsDisplayedMessage();
		
		assertEquals("Category already exists",
				categoryPage.getMessage().getText());
		assertEquals(size, ((List<Category>) categoryService.getAll()).size());
		driver.close();
	}
	
	@AfterEach
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
