import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;
import pages.MainPage;
import pages.FlightSearchPage;

import static org.testng.Assert.assertTrue;

public class OneWaySearchTest {
    private WebDriver driver;
    private MainPage mainPage;
    private FlightSearchPage flightSearchPage;

    @BeforeClass
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://uzairways.com/en");

        mainPage = new MainPage(driver);
        flightSearchPage = new FlightSearchPage(driver);
    }

    @Test
    public void testOneWayFlightSearch() {
        mainPage.acceptCookiesIfPresent();

        flightSearchPage.selectFromCity("Tashkent");
        flightSearchPage.selectToCity("Istanbul");
        flightSearchPage.selectDepartureDate("10");
        flightSearchPage.clickSearch();

        String currentUrl = driver.getCurrentUrl();

        assertTrue(currentUrl.contains("booking") || currentUrl.contains("flights") || !currentUrl.equals("https://uzairways.com/en"),
                "Page did not navigate after clicking 'Buy a ticket'.");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
