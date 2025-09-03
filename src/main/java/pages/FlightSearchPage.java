package main.java.pages;

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
    private List<WebElement> monthHeaders;

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

    @FindBy(css = "div.dropdown-menu.show input.form-control")
    private WebElement arrivalSearchBox;

    @FindBy(css = "ul.dropdown-menu.inner.show")
    private WebElement cityDropdownList;

    @FindBy(css = "input[data-note='Выберите дату обратно']")
    private WebElement returnInput;

    private static final String CITY_DROPDOWN_ITEM = "//ul[contains(@class,'dropdown-menu')]//span[contains(text(), '%s')]";
    private static final String CALENDAR_DATE_CELL = ".//td[not(contains(@class, 'off')) and normalize-space(text())='%s']";
    private static final String CURRENCY_OPTION = "//span[@class='only-code' and text()='%s']";

    public FlightSearchPage(WebDriver driver) {
        super(driver);
    }

    private By getCityDropdownItem(String city) {
        return By.xpath(String.format(CITY_DROPDOWN_ITEM, city));
    }

    private By getCalendarDateCell(String day) {
        return By.xpath(String.format(CALENDAR_DATE_CELL, day));
    }

    private By getCurrencyOption(String currency) {
        return By.xpath(String.format(CURRENCY_OPTION, currency));
    }

    public void enableRoundTripIfDisabled() {
        waitUntilClickable(toggleLabel);
        if (oneWayToggleCheckbox.isSelected()) {
            toggleLabel.click();
            waitUntilReturnInputVisible();
        }
    }

    public void selectDepartureCity(String cityName) {
        fromButton.click();
        waitUntilVisible(cityDropdownList);
        WebElement city = waitUntilClickable(getCityDropdownItem(cityName));
        scrollIntoView(city);
        city.click();
    }

    public void selectArrivalCity(String cityName) {
        waitUntilClickable(toButton).click();
        try {
            WebElement searchBox = wait.until(ExpectedConditions.presenceOfElementLocated(ARRIVAL_SEARCH_BOX));
            searchBox.clear();
            searchBox.sendKeys(cityName);
            searchBox.sendKeys(Keys.ENTER);
        } catch (TimeoutException e) {
            WebElement city = waitUntilClickable(getCityDropdownItem(cityName));
            scrollIntoView(city);
            city.click();
        }
    }

    public boolean selectDepartureDate(String day, String targetMonth) {
        jsClick(departureInput);
        waitUntilVisible(calendarLeft);

        final int maxRetries = 12;
        for (int i = 0; i < maxRetries; i++) {
            if (isTargetMonthVisible(targetMonth)) {
                WebElement calendar = getCalendarForMonth(targetMonth);
                WebElement dateCell = calendar.findElement(getCalendarDateCell(day));
                scrollIntoView(dateCell);
                dateCell.click();
                return true;
            }
            waitUntilClickable(nextButtonLeft).click();
            waitForCalendarUpdate();
        }

        return false;
    }

    public void selectReturnDate(String day) {
        jsClick(waitUntilReturnInputVisible());
        waitUntilVisible(calendarRight);
        WebElement dateCell = calendarRight.findElement(getCalendarDateCell(day));
        scrollIntoView(dateCell);
        dateCell.click();
    }

    public void selectPassengers(int adults, int children, int babies) {
        waitUntilClickable(passengersButton);
        scrollIntoView(passengersButton);
        jsClick(passengersButton);
        waitUntilVisible(passengerPopover);

        for (int i = getCurrentValue(adultInput); i < adults; i++) {
            adultPlusButton.click();
        }

        for (int i = getCurrentValue(childInput); i < children; i++) {
            childPlusButton.click();
        }

        for (int i = getCurrentValue(babyInput); i < babies; i++) {
            babyPlusButton.click();
        }

        waitUntilClickable(applyPassengersButton).click();
    }

    public void selectCurrency(String currencyCode) {
        waitUntilClickable(currencyDropdown);
        jsClick(currencyDropdown);
        waitUntilVisible(dropdownMenu);
        WebElement option = waitUntilClickable(getCurrencyOption(currencyCode));
        option.click();
    }

    private boolean isTargetMonthVisible(String targetMonth) {
        for (WebElement header : monthHeaders) {
            if (header.getText().toLowerCase().contains(targetMonth.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private WebElement getCalendarForMonth(String targetMonth) {
        for (int i = 0; i < monthHeaders.size(); i++) {
            if (monthHeaders.get(i).getText().toLowerCase().contains(targetMonth.toLowerCase())) {
                return calendarContainers.get(i);
            }
        }
        throw new NoSuchElementException("No calendar found showing " + targetMonth);
    }

    private void waitForCalendarUpdate() {
        wait.until(ExpectedConditions.invisibilityOf(calendarLoading));
    }

    private WebElement waitUntilReturnInputVisible() {
        return waitUntilVisible(returnInput);
    }

    public void clickSearch() {
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
    }
}