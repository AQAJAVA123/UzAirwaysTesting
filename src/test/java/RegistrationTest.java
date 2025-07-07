import org.testng.annotations.*;
import pages.RegistrationPage;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class RegistrationTest extends BaseTest {
    RegistrationPage registrationPage;

    @BeforeClass
    public void setup() {
        driver.get(AUTH_URL + "/auth/register");
        registrationPage = new RegistrationPage(driver);
    }

    String testEmail = "testuser@example.com";
    String firstName = "Abc";
    String lastName = "Smith";
    String birthDate = "01.01.2000";
    String password = "Password123";

    @Test
    public void testUserRegistration() {

        registrationPage.enterEmail(testEmail);
        registrationPage.enterFirstName(firstName);
        registrationPage.enterLastName(lastName);
        registrationPage.enterBirthDate(birthDate);
        registrationPage.enterPassword(password);
        registrationPage.confirmPassword(password);

        registrationPage.clickRegister();

        boolean isCaptchaVisible = driver.getPageSource().toLowerCase().contains("captcha");
        assertFalse(isCaptchaVisible, "CAPTCHA is a bug");

        assertTrue(driver.getCurrentUrl().contains("success"), "Registration did not redirect to success page.");
    }
}

