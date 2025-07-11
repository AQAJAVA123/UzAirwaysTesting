import org.testng.annotations.*;
import pages.MainPage;
import pages.FlightSearchPage;

import static org.testng.Assert.assertTrue;

public class SameCitySearchTest extends BaseTest {
    private MainPage mainPage;
    private FlightSearchPage flightSearchPage;

    private final String cityName = "Tashkent";
    private final String departureDay = "10";
    private final String departureMonth = "August 2025";
    private final String currency = "USD";

    @BeforeClass
    public void initPages() {
        driver.get(BASE_URL);
        mainPage = new MainPage(driver);
        flightSearchPage = new FlightSearchPage(driver);
    }

    @Test
    public void testSameCitySearchError() {
        mainPage.acceptCookiesIfPresent();

        flightSearchPage.selectDepartureCity(cityName);
        flightSearchPage.selectArrivalCity(cityName);
        flightSearchPage.selectDepartureDate(departureDay, departureMonth);
        flightSearchPage.selectCurrency(currency);
        flightSearchPage.clickSearch();

        String pageSource = driver.getPageSource().toLowerCase();
        assertTrue(pageSource.contains("departure and arrival cities must be different"),
                "Expected error message for same departure and arrival cities.");
    }
}
