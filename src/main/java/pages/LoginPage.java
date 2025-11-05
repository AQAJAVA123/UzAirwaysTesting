package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    @FindBy(name = "email")
    private WebElement emailField;

    @FindBy(name = "password")
    private WebElement passwordField;

    @FindBy(css = "button[type='submit']")
    private WebElement loginButton;

    @FindBy(css = "div[role='alert'].Vue-Toastification__toast-body")
    private WebElement loginErrorToast;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Enter email: {email}")
    public void enterEmail(String email) {
        waitUntilVisible(emailField).clear();
        emailField.sendKeys(email);
    }

    @Step("Enter password")
    public void enterPassword(String password) {
        waitUntilVisible(passwordField).clear();
        passwordField.sendKeys(password);
    }

    @Step("Click Login button")
    public void clickLogin() {
        waitUntilClickable(loginButton).click();
    }

    @Step("Is Login button visible?")
    public boolean isLoginButtonVisible() {
        return isElementVisible(loginButton);
    }

    @Step("Is login error toast visible?")
    public boolean isLoginErrorVisible() {
        try {
            return waitUntilVisible(loginErrorToast).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
}