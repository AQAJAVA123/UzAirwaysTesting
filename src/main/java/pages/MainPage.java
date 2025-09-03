package main.java.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;

public class MainPage extends BasePage {

    @FindBy(id = "cookieyes")
    private WebElement acceptCookiesBtn;

    public MainPage(WebDriver driver) {
        super(driver);
    }

    public void acceptCookiesIfPresent() {
        if (isElementVisible(acceptCookiesBtn)) {
            waitUntilClickable(acceptCookiesBtn).click();
        }
    }
}
