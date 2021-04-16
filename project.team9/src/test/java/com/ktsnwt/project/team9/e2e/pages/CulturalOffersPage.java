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
public class CulturalOffersPage {

	private WebDriver driver;

	@FindBy(id = "create_new_cultural_offer_button")
	private WebElement createCulturalOfferButton;

	@FindBy(id = "culturalOffer_1_update_button")
	private WebElement updateCulturalOfferButton;

	@FindBy(id = "culturalOffer_16_remove_button")
	private WebElement removeCulturalOfferButton;

	@FindBy(xpath = "//mat-dialog-container[@id='mat-dialog-0']/app-confirmation-dialog/mat-dialog-actions/button")
	private WebElement okButton;

	@FindBy(xpath = "//mat-dialog-container[@id='mat-dialog-0']/app-confirmation-dialog/mat-dialog-actions/button[2]")
	private WebElement cancelButton;

	@FindBy(xpath = "//simple-snack-bar/span")
	private WebElement message;

	@FindBy(xpath = "//pagination-template/ul/li[4]/a")
	private WebElement secondPage;
	
	@FindBy(id = "culturalOfferPage_more_button_1")
	private WebElement more;
	
	@FindBy(id = "select_category_through_culturalOffersPage")
	private WebElement select;
	
	public CulturalOffersPage(WebDriver driver) {
		this.driver = driver;
	}

	public void ensureIsDisplayedCreateNewCulturalOfferButton() {
		(new WebDriverWait(driver, 30))
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("create_new_cultural_offer_button")));
	}

	public void ensureIsDisplayedUpdateCulturalOfferButton() {
		(new WebDriverWait(driver, 30))
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("culturalOffer_1_update_button")));
	}

	public void ensureIsDisplayedRemoveCulturalOfferButton() {
		(new WebDriverWait(driver, 30))
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("culturalOffer_16_remove_button")));
	}

	public void ensureIsDisplayedOKRemoveButton() {
		(new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"//mat-dialog-container[@id='mat-dialog-0']/app-confirmation-dialog/mat-dialog-actions/button")));
	}

	public void ensureIsDisplayedCancelRemoveButton() {
		(new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"//mat-dialog-container[@id='mat-dialog-0']/app-confirmation-dialog/mat-dialog-actions/button[2]")));
	}

	public void ensureIsDisplayedMessage() {
		(new WebDriverWait(driver, 30))
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//simple-snack-bar/span")));
	}

	public void ensureIsDisplayedSecondPage() {
		(new WebDriverWait(driver, 30))
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//pagination-template/ul/li[4]/a")));
	}
	
	public void ensureIsDisplayedMoreButton() {
		(new WebDriverWait(driver, 30))
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("culturalOfferPage_more_button_1")));
	}
	
	public void ensureIsDisplayedSelectField() {
		(new WebDriverWait(driver, 30))
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("select_category_through_culturalOffersPage")));
	}
}
