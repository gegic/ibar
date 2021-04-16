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
public class ReviewCulturalOfferPage {

	private WebDriver driver;
	
	@FindBy(id = "individual_culturalOffer_name")
	private WebElement name;
	
	@FindBy(xpath = "//bar-rating/div/div/div[4]")
	private WebElement star;
	
	@FindBy(xpath = "//bar-rating/div/div/div[1]")
	private WebElement star2;
	
	@FindBy(xpath = "//bar-rating/div/div/div[@class='br-unit br-selected ng-star-inserted']")
	private WebElement starRated;
	
	@FindBy(id = "comments-review-btn")
	private WebElement reviewBtn;
	
	@FindBy(id = "add-new-comment-btn")
	private WebElement addCommentBtn;
	
	@FindBy(tagName = "app-comment")
	private WebElement comment;
	
	@FindBy(id = "comment-text")
	private WebElement commentText;
	
	@FindBy(id = "comment-image-file-input")
	private WebElement commentImg;
	
	@FindBy(id = "add-comment-btn")
	private WebElement newCommentBtn;
	
	@FindBy(id = "close-add-comment-btn")
	private WebElement closeBtn;
	
	@FindBy(xpath = "//simple-snack-bar/span")
	private WebElement message;
	
	@FindBy(id = "all-empty-comment")
	private WebElement emptyComment;
	
	@FindBy(id = "news-review-btn")
	private WebElement newsButton;
	
	@FindBy(xpath = "//*[contains(text(),'Subscribe')]")
	private WebElement subscribeButton;

	@FindBy(xpath = "//*[contains(text(),'Unsubscribe')]")
	private WebElement unsubscribeButton;
	
	public ReviewCulturalOfferPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void ensureIsDisplayedName() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("individual_culturalOffer_name")));
    }
	
	public void ensureIsDisplayedStar() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//bar-rating/div/div/div[4]")));
    }
	
	public void ensureIsDisplayedStarRated() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//bar-rating/div/div/div[@class='br-unit br-selected ng-star-inserted']")));
    }
	
	public void ensureIsDisplayedReviewBtn() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("comments-review-btn")));
    }
	
	public void ensureIsDisplayedAddCommentBtn() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("add-new-comment-btn")));
    }
	
	public void ensureIsDisplayedComment() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.tagName("app-comment")));
    }
	
	public void ensureIsDisplayedCommentText() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("comment-text")));
    }
	
	public void ensureIsDisplayedCommentImg() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("comment-image-file-input")));
    }
	
	public void ensureIsDisplayedNewCommentBtn() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("add-comment-btn")));
    }
	
	public void ensureIsDisplayedCloseBtn() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("close-add-comment-btn")));
    }
	
	public void ensureIsDisplayedMessage() {
		(new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//simple-snack-bar/span")));
	}
	
	public void ensureIsDisplayedNewsButton() {
		(new WebDriverWait(driver, 30))
		.until(ExpectedConditions.visibilityOfElementLocated(By.id("news-review-btn")));
	}

	public void ensureIsDisplayedSubscribeButton() {
		(new WebDriverWait(driver, 30))
		.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Subscribe')]")));
	}
	public void ensureIsDisplayedUnsubscribeButton() {
		(new WebDriverWait(driver, 30))
		.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Unsubscribe')]")));
	}
	
	public void ensureIsDisplayedEmptyComment() {
		(new WebDriverWait(driver, 30))
		.until(ExpectedConditions.visibilityOfElementLocated(By.id("all-empty-comment")));
	}
}
