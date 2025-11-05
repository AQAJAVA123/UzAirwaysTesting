package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;

public class MainPage extends pages.BasePage {

    @FindBy(id = "cookieyes")
    private WebElement acceptCookiesBtn;

    public MainPage(WebDriver driver) {
        super(driver);
    }

    @Step("Accept cookies if banner is present")
    public void acceptCookiesIfPresent() {
        if (isElementVisible(acceptCookiesBtn)) {
            waitUntilClickable(acceptCookiesBtn).click();
        }
    }
}