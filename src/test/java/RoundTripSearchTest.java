import org.testng.annotations.*;
import pages.MainPage;
import pages.FlightSearchPage;

import static org.testng.Assert.assertTrue;

public class RoundTripSearchTest extends BaseTest {
    private MainPage mainPage;
    private FlightSearchPage flightSearchPage;

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
        flightSearchPage.selectDepartureCity("Tashkent");
        flightSearchPage.selectArrivalCity("Istanbul");
        flightSearchPage.selectDepartureDate("10", "July 2025");
        flightSearchPage.selectReturnDate("20");
        flightSearchPage.selectPassengers(1, 0, 0);
        flightSearchPage.selectCurrency("USD");
        flightSearchPage.clickSearch();

        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL after round-trip search: " + currentUrl);

        assertTrue(currentUrl.contains("booking"),
                "User is not redirected to the Booking page after submitting flight search details.");
    }
}
