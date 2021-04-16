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
public class UserPage {

	private WebDriver driver;
	
	@FindBy(xpath = "//*[contains(text(),'Registered users')]")
	private WebElement regsTab;
	
	@FindBy(xpath = "//*[contains(text(),'Remove')]")
	private WebElement removeButton;
	
	@FindBy(xpath = "//*[contains(text(),'Add new administrator')]")
	private WebElement addButton;
	
	@FindBy(xpath = "//div[@id='search-div-user-page']/app-search/div/mat-form-field/div/div/div[3]/input")
	private WebElement search;
	
	@FindBy(xpath = "//mat-dialog-container[@id='mat-dialog-0']/app-confirmation-dialog/mat-dialog-actions/button")
	private WebElement okButton;

	@FindBy(xpath = "//mat-dialog-container[@id='mat-dialog-0']/app-confirmation-dialog/mat-dialog-actions/button[2]")
	private WebElement noButton;
	
	@FindBy(xpath = "//simple-snack-bar/span")
	private WebElement message;
	
	@FindBy(id = "add-admin-email")
	private WebElement emailAdmin;
	
	@FindBy(id = "add-admin-username")
	private WebElement usernameAdmin;
	
	@FindBy(id = "add-admin-firstname")
	private WebElement firstNameAdmin;
	
	@FindBy(id = "add-admin-lastname")
	private WebElement lastNameAdmin;
	
	@FindBy(id = "add-admin-btn")
	private WebElement addBtn;
	
	@FindBy(id = "close-add-admin-btn")
	private WebElement closeBtn;
	
	public UserPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void ensureIsDisplayedSearch() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='search-div-user-page']/app-search/div/mat-form-field/div/div/div[3]/input")));
    }
	
	public void ensureIsDisplayedRegUsersTab() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Registered users')]")));
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
	
	public void ensureIsDisplayedEmail() {
		(new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("add-admin-email")));
	}
}
