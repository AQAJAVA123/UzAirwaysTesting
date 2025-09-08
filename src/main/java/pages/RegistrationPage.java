package main.java.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;

public class RegistrationPage extends BasePage {

    @FindBy(name = "email")
    private WebElement emailInput;

    @FindBy(name = "first_name")
    private WebElement firstNameInput;

    @FindBy(name = "last_name")
    private WebElement lastNameInput;

    @FindBy(name = "date")
    private WebElement birthDateInput;

    @FindBy(name = "password")
    private WebElement passwordInput;

    @FindBy(name = "password_confirmation")
    private WebElement confirmPasswordInput;

    @FindBy(css = "button[type='submit']")
    private WebElement registerButton;

    public RegistrationPage(WebDriver driver) {
        super(driver);
    }

    public void enterEmail(String email) {
        waitUntilVisible(emailInput).sendKeys(email);
    }

    public void enterFirstName(String firstName) {
        waitUntilVisible(firstNameInput).sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        waitUntilVisible(lastNameInput).sendKeys(lastName);
    }

    public void enterBirthDate(String birthDate) {
        waitUntilVisible(birthDateInput).sendKeys(birthDate);
    }

    public void enterPassword(String password) {
        waitUntilVisible(passwordInput).sendKeys(password);
    }

    public void confirmPassword(String password) {
        waitUntilVisible(confirmPasswordInput).sendKeys(password);
    }

    public void clickRegister() {
        waitUntilClickable(registerButton).click();
    }
}
