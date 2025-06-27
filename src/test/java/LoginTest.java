import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;
import pages.LoginPage;

public class LoginTest {
    WebDriver driver;
    LoginPage loginPage;

    @BeforeClass
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://percab.uzairways.com/auth/login?redirect=/orders?lang=en");
        loginPage = new LoginPage(driver);
    }

    @Test
    public void testLoginForm() {
        loginPage.enterEmail("testuser@example.com");
        loginPage.enterPassword("Password123");

        assert loginPage.isLoginButtonVisible();
        loginPage.clickLogin();
    }

    @AfterClass
    public void tearDown() {
        if (driver != null)
            driver.quit();
    }
}
