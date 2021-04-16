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
public class NewsReviewPage {

	private WebDriver driver;
	
	@FindBy(xpath = "//*[contains(text(),'Remove')]")
	private WebElement removeButton;
	
	@FindBy(xpath = "//*[contains(text(),'Create news')]")
	private WebElement addButton;
	
	@FindBy(xpath = "//*[contains(text(),'Update')]")
	private WebElement updateButton;
	
	@FindBy(id = "more-btn")
	private WebElement moreButton;
	
	@FindBy(id = "news-title")
	private WebElement newsTitle;
	
	
	@FindBy(id = "co-btn")
	private WebElement culturalOfferButton;
	
	@FindBy(xpath = "//mat-dialog-container[@id='mat-dialog-0']/app-confirmation-dialog/mat-dialog-actions/button")
	private WebElement okButton;

	@FindBy(xpath = "//mat-dialog-container[@id='mat-dialog-0']/app-confirmation-dialog/mat-dialog-actions/button[2]")
	private WebElement noButton;
	
	@FindBy(xpath = "//simple-snack-bar/span")
	private WebElement message;
	
	@FindBy(id = "news-content")
	private WebElement newsContent;
	
	@FindBy(id = "images")
	private WebElement newsImages;
	
	@FindBy(id = "news-btn")
	private WebElement addOrUpdateBtn;
	
	@FindBy(id = "close-news-btn")
	private WebElement closeBtn;
	
	public NewsReviewPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void ensureIsDisplayedMoreBtn() {
		(new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("more-btn")));
	}
	public void ensureIsDisplayedNewsContent() {
		(new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("news-content")));
	}
	public void ensureIsDisplayedOKRemoveButton() {
		(new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//mat-dialog-container[@id='mat-dialog-0']/app-confirmation-dialog/mat-dialog-actions/button")));
	}

	public void ensureIsDisplayedNoRemoveButton() {
		(new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//mat-dialog-container[@id='mat-dialog-0']/app-confirmation-dialog/mat-dialog-actions/button[2]")));
	}
	
	public void ensureIsDisplayedMessage() {
		(new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//simple-snack-bar/span")));
	}
	
	public void ensureIsDisplayedCulturalOfferButton() {
		(new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("co-btn")));
	}
	
	public void ensureIsDisplayedAddOrUpdateBtn() {
		(new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("news-btn")));
	}
}
