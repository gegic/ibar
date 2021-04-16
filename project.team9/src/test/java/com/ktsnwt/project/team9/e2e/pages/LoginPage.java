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
public class LoginPage {

	private WebDriver driver;

	@FindBy(id = "login_email")
	private WebElement email;

	@FindBy(id = "login_password")
	private WebElement password;
	
	@FindBy(xpath = "//form/button")
	private WebElement signIn;
	
	@FindBy(id = "forgot-password-link")
	private WebElement forgotPasswordLink;
	
	@FindBy(xpath = "//simple-snack-bar/span")
	private WebElement message;

	public LoginPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void ensureIsDisplayedEmail() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("login_email")));
    }
	
	public void ensureIsDisplayedMessage() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//simple-snack-bar/span")));
    }
	
	public void ensureIsDisplayedForgotPasswordLink() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("forgot-password-link")));
    }
	
}
