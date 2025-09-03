package test;

import org.testng.annotations.*;
import pages.MainPage;
import pages.FlightSearchPage;

import static org.testng.Assert.assertTrue;

public class OneWaySearchTest extends BaseTest {
    private MainPage mainPage;
    private FlightSearchPage flightSearchPage;

    private final String departureCity = "Tashkent";
    private final String arrivalCity = "Istanbul";
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
    public void testOneWayFlightSearch() {
        mainPage.acceptCookiesIfPresent();

        flightSearchPage.selectDepartureCity(departureCity);
        flightSearchPage.selectArrivalCity(arrivalCity);
        flightSearchPage.selectDepartureDate(departureDay, departureMonth);
        flightSearchPage.selectCurrency(currency);
        flightSearchPage.clickSearch();

        String currentUrl = driver.getCurrentUrl();

        assertTrue(currentUrl.contains("booking"),
                "User is not redirected to the Booking page after submitting flight search details.");
    }
}
