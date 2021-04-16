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
public class CreateUpdateCulturalOfferPage {

	private WebDriver driver;
	
	@FindBy(id = "culturalOffer_name_input")
	private WebElement name;
	
	@FindBy(id = "culturalOffer_location_input")
	private WebElement location;
	
	@FindBy(id = "select_category")
	private WebElement category;
	
	@FindBy(id = "culturalOffer_description_input")
	private WebElement description;
	
	@FindBy(id = "culturalOffer_image_file_select")
	private WebElement image;
	
	@FindBy(id = "create_culturalOffer_button")
	private WebElement createButton;
	
	@FindBy(id = "update_culturalOffer_button")
	private WebElement updateButton;
	
	@FindBy(xpath = "//simple-snack-bar/span")
	private WebElement message;
	
	public CreateUpdateCulturalOfferPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void ensureIsDisplayedNameInput() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id("culturalOffer_name_input")));
    }
	
	public void ensureIsDisplayedMessage() {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//simple-snack-bar/span")));
    }
}
