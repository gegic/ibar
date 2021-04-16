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
public class MyNewsPage {

	private WebDriver driver;

	
	@FindBy(id = "co-btn")
	private WebElement culturalOfferButton;

	@FindBy(id = "more-btn")
	private WebElement moreButton;
	
	@FindBy(id = "mat-tab-label-1-1")
	private WebElement festivalTab;
	
	
	public MyNewsPage(WebDriver driver) {
		this.driver = driver;
	}
	
	
	public void ensureIsDisplayedMoreBtn() {
		(new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("more-btn")));
	}
	public void ensureIsDisplayedCulturalOfferButton() {
		(new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("co-btn")));
	}
	
	public void ensureIsDisplayedTab(){
		(new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("mat-tab-label-1-1")));
	}
}
