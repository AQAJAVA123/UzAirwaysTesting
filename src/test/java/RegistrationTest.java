import org.testng.annotations.*;
import pages.RegistrationPage;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class RegistrationTest extends BaseTest {
    private RegistrationPage registrationPage;

    private final String testEmail = "testuser@example.com";
    private final String firstName = "Abc";
    private final String lastName = "Smith";
    private final String birthDate = "01.01.2000";
    private final String password = "Password123";

    @BeforeClass
    public void setup() {
        driver.get(AUTH_URL + "/auth/register");
        registrationPage = new RegistrationPage(driver);
    }

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
        assertFalse(isCaptchaVisible, "[BUG]: CAPTCHA is present");

        assertTrue(driver.getCurrentUrl().contains("success"), "Registration did not redirect to success page.");
    }
}

