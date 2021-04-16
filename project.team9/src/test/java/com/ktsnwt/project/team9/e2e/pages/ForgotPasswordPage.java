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
public class ForgotPasswordPage {

	private WebDriver driver;

	@FindBy(id = "email-address")
	private WebElement email;
	
	@FindBy(xpath = "//form/button")
	private WebElement send;
	
	@FindBy(xpath = "//simple-snack-bar/span")
	private WebElement message;

	public ForgotPasswordPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void ensureIsDisplayedEmail() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("email-address")));
    }
	
	public void ensureIsDisplayedMessage() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//simple-snack-bar/span")));
    }
	
}
