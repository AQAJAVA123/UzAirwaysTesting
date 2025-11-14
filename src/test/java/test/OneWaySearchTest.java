package test;

import io.qameta.allure.*;
import org.testng.annotations.*;
import pages.MainPage;
import pages.FlightSearchPage;

import static org.testng.Assert.assertTrue;

@Epic("Flight search")
@Feature("One-way flight search")
public class OneWaySearchTest extends BaseTest {
    private MainPage mainPage;
    private FlightSearchPage flightSearchPage;

    private final String departureCity = "Tashkent";
    private final String arrivalCity = "Istanbul";
    private final String departureDay = "10";
    private final String departureMonth = "December 2025";
    private final String currency = "USD";

    @BeforeMethod
    public void initPages() {
        driver.get(BASE_URL);
        mainPage = new MainPage(driver);
        flightSearchPage = new FlightSearchPage(driver);
    }

    @Step("User performs one-way flight search")
    @Test
    @Story("Search for one-way flight")
    @Description("Verify that a user can search for a one-way flight")
    @Severity(SeverityLevel.CRITICAL)
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
