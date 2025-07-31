package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

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

    public void enterEmail(String email) {
        wait.until(ExpectedConditions.visibilityOf(emailField)).clear();
        emailField.sendKeys(email);
    }

    public void enterPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public boolean isLoginButtonVisible() {
        return isElementVisible(loginButton);
    }

    public boolean isLoginErrorVisible() {
        try {
            WebDriverWait toastWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            toastWait.until(ExpectedConditions.visibilityOf(loginErrorToast));
            return loginErrorToast.getText().toLowerCase().contains("wrong credentials");
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void clickLogin() {
        loginButton.click();
    }
}