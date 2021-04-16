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
public class DashboardPage {

	private WebDriver driver;
	
	@FindBy(id = "co-btn")
	private WebElement culturalOfferButton;
	
	@FindBy(xpath = "//*[contains(text(),'Unsubscribe')]")
	private WebElement unsubscribeButton;
	
	public DashboardPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void ensureIsDisplayedCulturalOfferButton() {
		(new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("co-btn")));
	}
	
	public void ensureIsDisplayedUnsubscribeButton() {
		(new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Unsubscribe')]")));
	}
}
