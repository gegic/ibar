package com.ktsnwt.project.team9.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HomePage {

	private WebDriver driver;

	@FindBy(id = "culturalOffers_navigation")
	private WebElement culturalOffersPage;
	
	@FindBy(id = "signup-navigation")
	private WebElement signUpPage;
	
	@FindBy(id = "signin-navigation")
	private WebElement signInPage;
	
	@FindBy(id = "home_page_more_button_3")
	private WebElement more;
	
	@FindBy(id = "home_page_more_button_1")
	private WebElement more1;
	
	@FindBy(id = "profile-button")
	private WebElement profileButton;
	
	@FindBy(id = "profile-circle-button")
	private WebElement profileCircleButton;
	
	@FindBy(id = "change-password-button")
	private WebElement changePasswordButton;
	
	@FindBy(id = "edit-button")
	private WebElement editButton;
	
	@FindBy(id = "save-profile-button")
	private WebElement saveProfileButton;
	
	@FindBy(id = "email-profile")
	private WebElement emailProfile;
	
	@FindBy(id = "username-profile")
	private WebElement usernameProfile;

	@FindBy(id = "first-name-profile")
	private WebElement firstNameProfile;
	
	@FindBy(id = "first-name-error")
	private WebElement firstNameError;
	
	@FindBy(id = "last-name-profile")
	private WebElement lastNameProfile;
	
	@FindBy(id = "email-profile-input")
	private WebElement emailProfileInput;
	
	@FindBy(id = "username-profile-input")
	private WebElement usernameProfileInput;

	@FindBy(id = "first-name-profile-input")
	private WebElement firstNameProfileInput;
	
	@FindBy(id = "last-name-profile-input")
	private WebElement lastNameProfileInput;
	
	@FindBy(id = "old-password")
	private WebElement oldPassword;
	
	@FindBy(id = "new-password")
	private WebElement newPassword;
	
	@FindBy(id = "six-characters")
	private WebElement sixCharacters;
	
	@FindBy(id = "confirm-password")
	private WebElement confirmPassword;
	
	@FindBy(id = "no-match")
	private WebElement noMatch;
	
	@FindBy(id = "submit-password-button")
	private WebElement submitPasswordButton;
	
	@FindBy(xpath = "//simple-snack-bar/span")
	private WebElement message;
	
	@FindBy(id = "mat-tab-label-0-1")
	private WebElement festivalsTab;
	
	@FindBy(id = "user-navigation")
	private WebElement userPage;
	
	@FindBy(id = "category-navigation")
	private WebElement categoryPage;
	
	@FindBy(xpath = "//div[@id='search_div_home_page']/app-search/div/mat-form-field/div/div/div[3]/input")
	private WebElement search;

	@FindBy(id = "comment-requests-navigation")
	private WebElement commentRequestsPage;

	@FindBy(xpath = "//*[contains(text(),'My News')]")
	private WebElement myNewsPage;
	
	@FindBy(xpath = "//*[contains(text(),'Dashboard')]")
	private WebElement dashboardPage;
	
	public HomePage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void ensureIsDisplayedCulturalOfferNavigation() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("culturalOffers_navigation")));
    }
	
	public void ensureIsDisplayedCategoriesNavigation() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("category-navigation")));
    }
	
	public void ensureIsDisplayedSignUpNavigation() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("signup-navigation")));
    }
	
	public void ensureIsDisplayedSignInNavigation() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("signin-navigation")));
    }

	public void ensureIsDisplayedMoreButton() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("home_page_more_button_3")));
    }
	
	public void ensureIsDisplayedMore1Button() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("home_page_more_button_1")));
    }
	
	public void ensureIsDisplayedProfileButton() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("profile-button")));
    }
	
	public void ensureIsDisplayedProfileCircleButton() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("profile-circle-button")));
    }
	
	public void ensureIsDisplayedChangePasswordButton() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("change-password-button")));
    }
	
	public void ensureIsDisplayedEditButton() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-button")));
    }
	
	public void ensureIsDisplayedSaveProfileButton() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("save-profile-button")));
    }
	
	public void ensureIsDisplayedEmailProfile() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("email-profile")));
    }
	
	public void ensureIsDisplayedEmailProfileInput() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("email-profile-input")));
    }
	
	public void ensureIsDisplayedOldPassword() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("old-password")));
    }
	
	public void ensureIsDisplayedSixCharacters() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("six-characters")));
    }
	
	public void ensureIsDisplayedNoMatch() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("no-match")));
    }
	
	public void ensureIsDisplayedSubmitPasswordButton() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("submit-password-button")));
    }
	
	public void ensureIsDisplayedMessage() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//simple-snack-bar/span")));
    }
	
	public void ensureIsDisplayedFestivalsTab() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("mat-tab-label-0-1")));
    }
	
	public void ensureIsDisplayedSearch() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='search_div_home_page']/app-search/div/mat-form-field/div/div/div[3]/input")));
    }
	
	public void ensureIsDisplayedCommentRequestsNavigation() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("comment-requests-navigation")));
    }
}
