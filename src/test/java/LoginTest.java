import org.testng.annotations.*;
import pages.LoginPage;

import static org.testng.Assert.assertTrue;

public class LoginTest extends BaseTest{
    private LoginPage loginPage;

    private final String testEmail = "testuser@example.com";
    private final String testPassword = "Password123";

    @BeforeClass
    public void setup() {
        driver.get(AUTH_URL + "/auth/login?redirect=/orders?lang=en");
        loginPage = new LoginPage(driver);
    }

    @Test
    public void testLoginForm() {
        loginPage.enterEmail(testEmail);
        loginPage.enterPassword(testPassword);

        assertTrue(loginPage.isLoginButtonVisible(), "Login button should be visible.");
        loginPage.clickLogin();

        assertTrue(loginPage.isLoginErrorVisible(), "Expected 'Wrong credentials' toast to appear.");
    }

}
