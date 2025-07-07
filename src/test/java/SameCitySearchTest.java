import org.testng.annotations.*;
import pages.MainPage;
import pages.FlightSearchPage;

import static org.testng.Assert.assertTrue;

public class SameCitySearchTest extends BaseTest {
    private MainPage mainPage;
    private FlightSearchPage flightSearchPage;

    @BeforeClass
    public void initPages() {
        driver.get(BASE_URL);
        mainPage = new MainPage(driver);
        flightSearchPage = new FlightSearchPage(driver);
    }

    @Test
    public void testSameCitySearchError() {
        mainPage.acceptCookiesIfPresent();

        flightSearchPage.selectDepartureCity("Tashkent");
        flightSearchPage.selectArrivalCity("Tashkent");
        flightSearchPage.selectDepartureDate("10", "July 2025");
        flightSearchPage.selectCurrency("USD");
        flightSearchPage.clickSearch();

        String pageSource = driver.getPageSource().toLowerCase();
        assertTrue(pageSource.contains("departure and arrival cities must be different"),
                "Expected error message for same departure and arrival cities.");
    }
}
