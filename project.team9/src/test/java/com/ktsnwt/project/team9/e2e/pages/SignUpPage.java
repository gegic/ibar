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
public class SignUpPage {

	private WebDriver driver;

	@FindBy(id = "signup-firstname")
	private WebElement firstName;
	
	@FindBy(id = "signup-lastname")
	private WebElement lastName;
	
	@FindBy(id = "signup-username")
	private WebElement username;
	
	@FindBy(id = "signup-email")
	private WebElement email;

	@FindBy(id = "signup-password")
	private WebElement password;
	
	@FindBy(id = "signup-email-error")
	private WebElement emailError;

	@FindBy(id = "signup-password-length-error")
	private WebElement passwordLenError;
	
	@FindBy(xpath = "//form/button")
	private WebElement signUp;
	
	@FindBy(xpath = "//simple-snack-bar/span")
	private WebElement message;

	public SignUpPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void ensureIsDisplayedEmail() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("signup-email")));
    }
	
	public void ensureIsDisplayedMessage() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//simple-snack-bar/span")));
    }
}
