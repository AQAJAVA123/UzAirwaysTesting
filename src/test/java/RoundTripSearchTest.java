import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;
import pages.MainPage;
import pages.FlightSearchPage;

import static org.testng.Assert.assertTrue;

public class RoundTripSearchTest {
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
    public void testRoundTripSearch() {
        mainPage.acceptCookiesIfPresent();

        flightSearchPage.enableRoundTripIfDisabled();
        flightSearchPage.selectFromCity("Tashkent");
        flightSearchPage.selectToCity("Istanbul");
        flightSearchPage.selectDepartureDate("10");
        flightSearchPage.selectReturnDate("20");
        flightSearchPage.selectPassengers(1, 0, 0);
        flightSearchPage.selectCurrency("USD");
        flightSearchPage.clickSearch();

        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL after round-trip search: " + currentUrl);

        assertTrue(currentUrl.contains("booking") || currentUrl.contains("flights") || currentUrl.contains("search"),
                "Expected redirect to booking system.");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
