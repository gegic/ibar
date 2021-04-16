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
public class CommentRequestsPage {
	private WebDriver driver;
	
	@FindBy(xpath = "//*[contains(text(),'Approve')]")
	private WebElement approveBtn;
	
	@FindBy(xpath = "//*[contains(text(),'Decline')]")
	private WebElement declineBtn;
	
	@FindBy(xpath = "//simple-snack-bar/span")
	private WebElement message;
	
	public CommentRequestsPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void ensureIsDisplayedApproveBtn() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Approve')]")));
    }
	
	public void ensureIsDisplayedDeclineBtn() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Decline')]")));
    }
	
	public void ensureIsDisplayedMessage() {
		(new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//simple-snack-bar/span")));
	}
}
