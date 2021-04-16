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
public class CategoryPage {

	private WebDriver driver;
	
	@FindBy(xpath = "//*[contains(text(),'Remove')]")
	private WebElement removeButton;
	
	@FindBy(xpath = "//*[contains(text(),'Add new category')]")
	private WebElement addButton;
	
	@FindBy(xpath = "//*[contains(text(),'Update')]")
	private WebElement updateButton;
	
	@FindBy(xpath = "//div[@id='search-div-category-page']/app-search/div/mat-form-field/div/div/div[3]/input")
	private WebElement search;
	
	@FindBy(xpath = "//mat-dialog-container[@id='mat-dialog-0']/app-confirmation-dialog/mat-dialog-actions/button")
	private WebElement okButton;

	@FindBy(xpath = "//mat-dialog-container[@id='mat-dialog-0']/app-confirmation-dialog/mat-dialog-actions/button[2]")
	private WebElement noButton;
	
	@FindBy(xpath = "//simple-snack-bar/span")
	private WebElement message;
	
	@FindBy(id = "category-name")
	private WebElement categoryName;
	
	@FindBy(id = "category-desc")
	private WebElement categoryDescription;
	
	@FindBy(id = "category-btn")
	private WebElement addOrUpdateBtn;
	
	@FindBy(id = "close-category-btn")
	private WebElement closeBtn;
	
	public CategoryPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void ensureIsDisplayedSearch() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='search-div-category-page']/app-search/div/mat-form-field/div/div/div[3]/input")));
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
	
	public void ensureIsDisplayedName() {
		(new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("category-name")));
	}
	
	public void ensureIsDisplayedAddOrUpdateBtn() {
		(new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("category-btn")));
	}
}
