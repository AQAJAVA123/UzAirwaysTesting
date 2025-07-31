package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;

import java.util.List;

public class FlightSearchPage extends BasePage {

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

    @FindBy(css = ".drp-calendar")
    private List<WebElement> calendarContainers;

    @FindBy(css = ".drp-calendar.left")
    private WebElement calendarLeft;

    @FindBy(css = ".drp-calendar.right")
    private WebElement calendarRight;

    @FindBy(css = "th.month")
    private List<WebElement> calendarHeaders;

    @FindBy(css = ".drp-calendar.left .next.available")
    private WebElement nextButtonLeft;

    @FindBy(css = ".drp-calendar.right .next.available")
    private WebElement nextButtonRight;

    @FindBy(css = ".drp-calendar.left .loading")
    private WebElement calendarLoading;

    @FindBy(css = "div.passengers__text")
    private WebElement passengersButton;

    @FindBy(css = "div.passengers__popover.passengers__popover--active")
    private WebElement passengerPopover;

    @FindBy(css = "div.passengers__popover--active .choosePassengers")
    private WebElement applyPassengersButton;

    @FindBy(css = "span.currency-selected")
    private WebElement currencyDropdown;

    @FindBy(css = "div.dropdown-menu.show")
    private WebElement dropdownMenu;

    @FindBy(id = "inputadult")
    private WebElement adultInput;

    @FindBy(css = "#inputadult ~ button.quantity__plus")
    private WebElement adultPlusButton;

    @FindBy(id = "inputchild")
    private WebElement childInput;

    @FindBy(css = "#inputchild ~ button.quantity__plus")
    private WebElement childPlusButton;

    @FindBy(id = "inputbaby")
    private WebElement babyInput;

    @FindBy(css = "#inputbaby ~ button.quantity__plus")
    private WebElement babyPlusButton;

    private static final String CITY_DROPDOWN_ITEM = "//ul[contains(@class,'dropdown-menu')]//span[contains(text(), '%s')]";
    private static final String CALENDAR_DATE_CELL = ".//td[not(contains(@class, 'off')) and normalize-space(text())='%s']";
    private static final String CURRENCY_OPTION = "//span[@class='only-code' and text()='%s']";
    private static final By ARRIVAL_SEARCH_BOX = By.cssSelector("div.dropdown-menu.show input.form-control");
    private static final By CITY_DROPDOWN_LIST = By.cssSelector("ul.dropdown-menu.inner.show");
    private static final By RETURN_INPUT_SELECTOR = By.cssSelector("input[data-note='Выберите дату обратно']");

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

        By cityLocator = By.xpath(String.format(CITY_DROPDOWN_ITEM, cityName));
        wait.until(ExpectedConditions.presenceOfElementLocated(cityLocator));
        WebElement city = wait.until(ExpectedConditions.elementToBeClickable(cityLocator));

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
        wait.until(ExpectedConditions.visibilityOf(calendarLeft));

        final int maxRetries = 12;
        for (int i = 0; i < maxRetries; i++) {
            if (isTargetMonthVisible(targetMonth)) {
                break;
            }
            wait.until(ExpectedConditions.elementToBeClickable(nextButtonLeft)).click();
            waitForCalendarUpdate();
        }

        if (!isTargetMonthVisible(targetMonth)) {
            throw new NoSuchElementException("Target month not visible after max retries: " + targetMonth);
        }

        WebElement correctCalendar = getCalendarForMonth(targetMonth);
        WebElement dateCell = correctCalendar.findElement(By.xpath(String.format(CALENDAR_DATE_CELL, day)));
        scrollIntoView(dateCell);
        dateCell.click();
    }

    public void selectReturnDate(String day) {
        WebElement returnInput = getReturnInput();
        jsClick(returnInput);
        wait.until(ExpectedConditions.visibilityOf(calendarRight));
        WebElement dateCell = calendarRight.findElement(By.xpath(String.format(CALENDAR_DATE_CELL, day)));
        scrollIntoView(dateCell);
        dateCell.click();
    }

    public void selectPassengers(int adults, int children, int babies) {
        wait.until(ExpectedConditions.elementToBeClickable(passengersButton));
        scrollIntoView(passengersButton);
        jsClick(passengersButton);
        wait.until(ExpectedConditions.visibilityOf(passengerPopover));

        int currentAdults = Integer.parseInt(adultInput.getAttribute("value"));
        while (currentAdults < adults) {
            adultPlusButton.click();
            currentAdults++;
        }

        int currentChildren = Integer.parseInt(childInput.getAttribute("value"));
        while (currentChildren < children) {
            childPlusButton.click();
            currentChildren++;
        }

        int currentBabies = Integer.parseInt(babyInput.getAttribute("value"));
        while (currentBabies < babies) {
            babyPlusButton.click();
            currentBabies++;
        }

        wait.until(ExpectedConditions.elementToBeClickable(applyPassengersButton)).click();
        System.out.printf("Passengers selected — Adults: %d, Children: %d, Babies: %d%n", adults, children, babies);
    }

    public void selectCurrency(String currencyCode) {
        wait.until(ExpectedConditions.elementToBeClickable(currencyDropdown));
        jsClick(currencyDropdown);
        wait.until(ExpectedConditions.visibilityOf(dropdownMenu));
        WebElement currencyOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(String.format(CURRENCY_OPTION, currencyCode))));
        currencyOption.click();
    }

    private boolean isTargetMonthVisible(String targetMonth) {
        for (WebElement header : calendarHeaders) {
            if (header.getText().toLowerCase().contains(targetMonth.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private WebElement getCalendarForMonth(String targetMonth) {
        for (WebElement calendar : calendarContainers) {
            WebElement header = calendar.findElement(By.cssSelector("th.month"));
            if (header.getText().toLowerCase().contains(targetMonth.toLowerCase())) {
                return calendar;
            }
        }
        throw new NoSuchElementException("No calendar found showing " + targetMonth);
    }

    private void waitForCalendarUpdate() {
        wait.until(ExpectedConditions.invisibilityOf(calendarLoading));
    }

    private WebElement getReturnInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(RETURN_INPUT_SELECTOR));
    }

    public void clickSearch() {
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
    }
}