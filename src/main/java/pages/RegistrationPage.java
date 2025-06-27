package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class RegistrationPage {
    WebDriver driver;
    WebDriverWait wait;

    @FindBy(name = "email")
    WebElement emailField;

    @FindBy(name = "first_name")
    WebElement firstNameField;

    @FindBy(name = "last_name")
    WebElement lastNameField;

    @FindBy(name = "date")
    WebElement birthDateField;

    @FindBy(name = "password")
    WebElement passwordField;

    @FindBy(name = "password_confirmation")
    WebElement confirmPasswordField;

    @FindBy(css = "button[type='submit']")
    WebElement registerButton;

    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    public void enterEmail(String email) {
        emailField.sendKeys(email);
    }

    public void enterFirstName(String firstName) {
        wait.until(ExpectedConditions.visibilityOf(firstNameField)).sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        lastNameField.sendKeys(lastName);
    }

    public void enterBirthDate(String birthDate) {
        wait.until(ExpectedConditions.visibilityOf(birthDateField)).clear();
        birthDateField.sendKeys(birthDate);
    }

    public void enterPassword(String password) {
        passwordField.sendKeys(password);
    }

    public void confirmPassword(String password) {
        confirmPasswordField.sendKeys(password);
    }

    public void clickRegister() {
        registerButton.click();
    }
}
