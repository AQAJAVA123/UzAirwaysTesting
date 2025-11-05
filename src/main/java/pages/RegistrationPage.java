package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;

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

    @Step("Enter email: {email}")
    public void enterEmail(String email) {
        waitUntilVisible(emailInput).sendKeys(email);
    }

    @Step("Enter first name: {firstName}")
    public void enterFirstName(String firstName) {
        waitUntilVisible(firstNameInput).sendKeys(firstName);
    }

    @Step("Enter last name: {lastName}")
    public void enterLastName(String lastName) {
        waitUntilVisible(lastNameInput).sendKeys(lastName);
    }

    @Step("Enter birth date: {birthDate}")
    public void enterBirthDate(String birthDate) {
        waitUntilVisible(birthDateInput).sendKeys(birthDate);
    }

    @Step("Enter password")
    public void enterPassword(String password) {
        waitUntilVisible(passwordInput).sendKeys(password);
    }

    @Step("Confirm password")
    public void confirmPassword(String password) {
        waitUntilVisible(confirmPasswordInput).sendKeys(password);
    }

    @Step("Click Register")
    public void clickRegister() {
        waitUntilClickable(registerButton).click();
    }
}