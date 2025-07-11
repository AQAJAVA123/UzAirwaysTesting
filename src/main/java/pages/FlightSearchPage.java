package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

public class FlightSearchPage extends BasePage {

    // Static elements
    @FindBy(css = "button[data-id='tablofrom']")
    private WebElement fromButton;

    @FindBy(css = "button[data-id='tabloto']")
    private WebElement toButton;

    @FindBy(id = "bookdates")
    private WebElement departureInput;

    @FindBy(id = "booking")
    private WebElement searchButton;

    @FindBy(id = "one-way-toggler")
    private WebElement oneWayToggleCheckbox;

    @FindBy(css = "label.input-toggler")
    private WebElement toggleLabel;

    // Dynamic locators
    private static final By CITY_DROPDOWN_LIST = By.cssSelector("ul.dropdown-menu.inner.show");
    private static final String CITY_DROPDOWN_ITEM = "//ul[contains(@class,'dropdown-menu')]//span[contains(text(), '%s')]";
    private static final By ARRIVAL_SEARCH_BOX = By.cssSelector("div.dropdown-menu.show input.form-control");
    private static final By RETURN_INPUT_SELECTOR = By.cssSelector("input[data-note='Выберите дату обратно']");
    private static final By CALENDAR_CONTAINER = By.cssSelector(".drp-calendar");
    private static final By CALENDAR_CONTAINER_LEFT = By.cssSelector(".drp-calendar.left");
    private static final By CALENDAR_CONTAINER_RIGHT = By.cssSelector(".drp-calendar.right");
    private static final By CALENDAR_HEADER = By.cssSelector("th.month");
    private static final String CALENDAR_DATE_CELL = ".//td[not(contains(@class, 'off')) and normalize-space(text())='%s']";
    private static final By NEXT_BUTTON_LEFT = By.cssSelector(".drp-calendar.left .next.available");
    private static final By NEXT_BUTTON_RIGHT = By.cssSelector(".drp-calendar.right .next.available");
    private static final By CALENDAR_LOADING = By.cssSelector(".drp-calendar.left .loading");
    private static final By PASSENGERS_BUTTON = By.cssSelector("div.passengers__text");
    private static final By PASSENGER_POPOVER = By.cssSelector("div.passengers__popover.passengers__popover--active");
    private static final By APPLY_PASSENGERS_BUTTON = By.cssSelector("div.passengers__popover--active .choosePassengers");
    private static final By CURRENCY_DROPDOWN = By.cssSelector("span.currency-selected");
    private static final By DROPDOWN_MENU = By.cssSelector("div.dropdown-menu.show");
    private static final String CURRENCY_OPTION = "//span[@class='only-code' and text()='%s']";
    private static final By ADULT_INPUT = By.id("inputadult");
    private static final By ADULT_PLUS_BUTTON = By.cssSelector("#inputadult ~ button.quantity__plus");
    private static final By CHILD_INPUT = By.id("inputchild");
    private static final By CHILD_PLUS_BUTTON = By.cssSelector("#inputchild ~ button.quantity__plus");
    private static final By BABY_INPUT = By.id("inputbaby");
    private static final By BABY_PLUS_BUTTON = By.cssSelector("#inputbaby ~ button.quantity__plus");

    public FlightSearchPage(WebDriver driver) {
        super(driver);
    }

    public void enableRoundTripIfDisabled() {
        wait.until(ExpectedConditions.elementToBeClickable(toggleLabel));
        if (oneWayToggleCheckbox.isSelected()) {
            toggleLabel.click();
            wait.until(d -> {
                try {
                    WebElement returnInput = d.findElement(RETURN_INPUT_SELECTOR);
                    return returnInput.isDisplayed();
                } catch (Exception e) {
                    return false;
                }
            });
        }
    }

    public void selectDepartureCity(String cityName) {
        fromButton.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(CITY_DROPDOWN_LIST));
        WebElement city = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(String.format(CITY_DROPDOWN_ITEM, cityName))));
        scrollIntoView(city);
        city.click();
    }

    public void selectArrivalCity(String cityName) {
        wait.until(ExpectedConditions.elementToBeClickable(toButton)).click();
        try {
            WebElement searchBox = wait.until(ExpectedConditions.presenceOfElementLocated(ARRIVAL_SEARCH_BOX));
            searchBox.clear();
            searchBox.sendKeys(cityName);
            searchBox.sendKeys(Keys.ENTER);
        } catch (TimeoutException e) {
            WebElement city = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(String.format(CITY_DROPDOWN_ITEM, cityName))));
            scrollIntoView(city);
            city.click();
        }
    }

    public void selectDepartureDate(String day, String targetMonth) {
        jsClick(departureInput);

        wait.until(ExpectedConditions.visibilityOfElementLocated(CALENDAR_CONTAINER));

        while (!isTargetMonthVisible(targetMonth)) {
            try {
                WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(NEXT_BUTTON_LEFT));
                nextButton.click();
            } catch (NoSuchElementException e) {
                throw new NoSuchElementException("No 'Next' button found in the departure calendar — cannot navigate to " + targetMonth);
            }
            waitForCalendarUpdate();
        }

        WebElement correctCalendar = getCalendarForMonth(targetMonth);
        WebElement dateCell = correctCalendar.findElement(
                By.xpath(String.format(CALENDAR_DATE_CELL, day))
        );
        scrollIntoView(dateCell);
        dateCell.click();
    }

    public void selectReturnDate(String day) {
        WebElement returnInput = getReturnInput();
        jsClick(returnInput);

        WebElement calendar = wait.until(ExpectedConditions.visibilityOfElementLocated(
                CALENDAR_CONTAINER_RIGHT));

        WebElement dateCell = calendar.findElement(
                By.xpath(String.format(CALENDAR_DATE_CELL, day)));
        scrollIntoView(dateCell);
        dateCell.click();
    }

    public void selectPassengers(int adults, int children, int babies) {
        WebElement passengersButton = wait.until(ExpectedConditions.elementToBeClickable(
                PASSENGERS_BUTTON));
        scrollIntoView(passengersButton);
        jsClick(passengersButton);

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                PASSENGER_POPOVER));

        // Adults
        if (adults >= 1) {
            WebElement adultInput = driver.findElement(ADULT_INPUT);
            WebElement adultPlus = driver.findElement((ADULT_PLUS_BUTTON));
            int current = Integer.parseInt(adultInput.getAttribute("value"));
            while (current < adults) {
                adultPlus.click();
                current++;
            }
        }

        // Children
        if (children > 0) {
            WebElement childInput = driver.findElement(CHILD_INPUT);
            WebElement childPlus = driver.findElement((CHILD_PLUS_BUTTON));
            int current = Integer.parseInt(childInput.getAttribute("value"));
            while (current < children) {
                childPlus.click();
                current++;
            }
        }

        // Babies
        if (babies > 0) {
            WebElement babyInput = driver.findElement(BABY_INPUT);
            WebElement babyPlus = driver.findElement(BABY_PLUS_BUTTON);
            int current = Integer.parseInt(babyInput.getAttribute("value"));
            while (current < babies) {
                babyPlus.click();
                current++;
            }
        }

        WebElement applyButton = wait.until(ExpectedConditions.elementToBeClickable(
                APPLY_PASSENGERS_BUTTON));
        applyButton.click();

        System.out.printf("Passengers selected — Adults: %d, Children: %d, Babies: %d%n", adults, children, babies);
    }

    public void selectCurrency(String currencyCode) {
        WebElement currencyDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                CURRENCY_DROPDOWN));
        jsClick(currencyDropdown);

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                DROPDOWN_MENU));

        WebElement currencyOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(String.format(CURRENCY_OPTION, currencyCode))));
        currencyOption.click();
    }

    private boolean isTargetMonthVisible(String targetMonth) {
        List<WebElement> headers = driver.findElements(CALENDAR_HEADER);
        for (WebElement header : headers) {
            if (header.getText().toLowerCase().contains(targetMonth.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private WebElement getCalendarForMonth(String targetMonth) {
        List<WebElement> calendars = driver.findElements(CALENDAR_CONTAINER);
        for (WebElement calendar : calendars) {
            WebElement header = calendar.findElement(CALENDAR_HEADER);
            if (header.getText().toLowerCase().contains(targetMonth.toLowerCase())) {
                return calendar;
            }
        }
        throw new NoSuchElementException("No calendar found showing " + targetMonth);
    }

    private void waitForCalendarUpdate() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                CALENDAR_LOADING));
    }

    private WebElement getReturnInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(
                RETURN_INPUT_SELECTOR));
    }

    public void clickSearch() {
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
    }

}
