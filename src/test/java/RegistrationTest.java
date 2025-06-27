import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;
import pages.RegistrationPage;

import static org.testng.Assert.assertTrue;

public class RegistrationTest {
    WebDriver driver;
    RegistrationPage registrationPage;

    @BeforeClass
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://percab.uzairways.com/auth/register");
        registrationPage = new RegistrationPage(driver);
    }

    @Test
    public void testUserRegistration() throws InterruptedException {
        registrationPage.enterEmail("testuser@example.com");
        registrationPage.enterFirstName("Aziza");
        registrationPage.enterLastName("Makhmudova");
        registrationPage.enterBirthDate("01.01.2000");
        registrationPage.enterPassword("Password123");
        registrationPage.confirmPassword("Password123");

        System.out.println("Please solve the CAPTCHA manually in the browser");
        Thread.sleep(5000);

        registrationPage.clickRegister();

        assertTrue(driver.getCurrentUrl().contains("success"), "Registration did not redirect to success page.");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}

