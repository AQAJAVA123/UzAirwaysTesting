import org.testng.annotations.*;
import pages.MainPage;
import pages.FlightSearchPage;

import static org.testng.Assert.assertTrue;

public class RoundTripSearchTest extends BaseTest {
    private MainPage mainPage;
    private FlightSearchPage flightSearchPage;

    private final String departureCity = "Tashkent";
    private final String arrivalCity = "Istanbul";
    private final String departureDay = "10";
    private final String departureMonth = "August 2025";
    private final String returnDay = "20";
    private final int adults = 1;
    private final int children = 0;
    private final int babies = 0;
    private final String currency = "USD";

    @BeforeClass
    public void initPages() {
        driver.get(BASE_URL);
        mainPage = new MainPage(driver);
        flightSearchPage = new FlightSearchPage(driver);
    }

    @Test
    public void testRoundTripSearch() {
        mainPage.acceptCookiesIfPresent();

        flightSearchPage.enableRoundTripIfDisabled();
        flightSearchPage.selectDepartureCity(departureCity);
        flightSearchPage.selectArrivalCity(arrivalCity);
        flightSearchPage.selectDepartureDate(departureDay, departureMonth);
        flightSearchPage.selectReturnDate(returnDay);
        flightSearchPage.selectPassengers(adults, children, babies);
        flightSearchPage.selectCurrency(currency);
        flightSearchPage.clickSearch();

        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL after round-trip search: " + currentUrl);

        assertTrue(currentUrl.contains("booking"),
                "User is not redirected to the Booking page after submitting flight search details.");
    }
}
