import org.testng.annotations.*;
import pages.MainPage;
import pages.FlightSearchPage;

import static org.testng.Assert.assertTrue;

public class OneWaySearchTest extends BaseTest {
    private MainPage mainPage;
    private FlightSearchPage flightSearchPage;

    @BeforeClass
    public void initPages() {
        driver.get(BASE_URL);
        mainPage = new MainPage(driver);
        flightSearchPage = new FlightSearchPage(driver);
    }

    @Test
    public void testOneWayFlightSearch() {
        mainPage.acceptCookiesIfPresent();

        flightSearchPage.selectDepartureCity("Tashkent");
        flightSearchPage.selectArrivalCity("Istanbul");
        flightSearchPage.selectDepartureDate("10", "July 2025");
        flightSearchPage.selectCurrency("USD");
        flightSearchPage.clickSearch();

        String currentUrl = driver.getCurrentUrl();

        assertTrue(currentUrl.contains("booking"),
                "User is not redirected to the Booking page after submitting flight search details.");
    }
}
