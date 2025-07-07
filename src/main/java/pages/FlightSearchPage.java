package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
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

    public FlightSearchPage(WebDriver driver) {
        super(driver);
    }

    public void enableRoundTripIfDisabled() {
        wait.until(ExpectedConditions.elementToBeClickable(toggleLabel));
        if (oneWayToggleCheckbox.isSelected()) {
            toggleLabel.click();
            wait.until(d -> {
                try {
                    WebElement returnInput = d.findElement(By.cssSelector("input[data-note]"));
                    return returnInput.isDisplayed();
                } catch (Exception e) {
                    return false;
                }
            });
        }
    }

    public void selectDepartureCity(String cityName) {
        fromButton.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("ul.dropdown-menu.inner.show")));
        WebElement city = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//ul[contains(@class,'dropdown-menu')]//span[contains(text(), '" + cityName + "')]")));
        scrollIntoView(city);
        city.click();
    }

    public void selectArrivalCity(String cityName) {
        wait.until(ExpectedConditions.elementToBeClickable(toButton)).click();
        try {
            WebElement searchBox = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("div.dropdown-menu.show input.form-control")));
            searchBox.clear();
            searchBox.sendKeys(cityName);
            searchBox.sendKeys(Keys.ENTER);
        } catch (TimeoutException e) {
            WebElement city = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//ul[contains(@class,'dropdown-menu')]//span[contains(text(), '" + cityName + "')]")));
            scrollIntoView(city);
            city.click();
        }
    }

    public void selectDepartureDate(String day, String targetMonth) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", departureInput);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".drp-calendar")));

        while (!isTargetMonthVisible(targetMonth)) {
            try {
                WebElement nextButton = driver.findElement(By.cssSelector(".drp-calendar.left .next.available"));
                nextButton.click();
            } catch (NoSuchElementException e) {
                throw new NoSuchElementException("No 'Next' button found in the departure calendar — cannot navigate to " + targetMonth);
            }
            waitForCalendarUpdate();
        }

        WebElement correctCalendar = getCalendarForMonth(targetMonth);
        WebElement dateCell = correctCalendar.findElement(
                By.xpath(".//td[not(contains(@class, 'off')) and normalize-space(text())='" + day + "']")
        );
        scrollIntoView(dateCell);
        dateCell.click();
    }

    public void selectReturnDate(String day) {
        WebElement returnInput = getReturnInput();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", returnInput);

        WebElement calendar = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".drp-calendar.right")));

        WebElement dateCell = calendar.findElement(
                By.xpath(".//td[not(contains(@class, 'off')) and normalize-space(text())='" + Integer.parseInt(day) + "']"));
        scrollIntoView(dateCell);
        dateCell.click();
    }

    public void selectPassengers(int adults, int children, int babies) {
        WebElement passengersButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("div.passengers__text")));
        scrollIntoView(passengersButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", passengersButton);

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div.passengers__popover.passengers__popover--active")));

        // Adults
        if (adults > 1) {
            WebElement adultInput = driver.findElement(By.id("inputadult"));
            WebElement adultPlus = driver.findElement(By.cssSelector("#inputadult ~ button.quantity__plus"));
            int current = Integer.parseInt(adultInput.getAttribute("value"));
            while (current < adults) {
                adultPlus.click();
                current++;
            }
        }

        // Children
        if (children > 0) {
            WebElement childInput = driver.findElement(By.id("inputchild"));
            WebElement childPlus = driver.findElement(By.cssSelector("#inputchild ~ button.quantity__plus"));
            int current = Integer.parseInt(childInput.getAttribute("value"));
            while (current < children) {
                childPlus.click();
                current++;
            }
        }

        // Babies
        if (babies > 0) {
            WebElement babyInput = driver.findElement(By.id("inputbaby"));
            WebElement babyPlus = driver.findElement(By.cssSelector("#inputbaby ~ button.quantity__plus"));
            int current = Integer.parseInt(babyInput.getAttribute("value"));
            while (current < babies) {
                babyPlus.click();
                current++;
            }
        }

        WebElement applyButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("div.passengers__popover--active .choosePassengers")));
        applyButton.click();

        System.out.printf("Passengers selected — Adults: %d, Children: %d, Babies: %d%n", adults, children, babies);
    }

    public void selectCurrency(String currencyCode) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement currencyDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("span.currency-selected")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", currencyDropdown);

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div.dropdown-menu.show")));

        String xpath = String.format("//span[@class='only-code' and text()='%s']", currencyCode);
        WebElement currencyOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        currencyOption.click();
    }

    private boolean isTargetMonthVisible(String targetMonth) {
        List<WebElement> headers = driver.findElements(By.cssSelector("th.month"));
        for (WebElement header : headers) {
            if (header.getText().toLowerCase().contains(targetMonth.toLowerCase()) ||
                    header.getText().toLowerCase().contains("июль 2025")) {
                return true;
            }
        }
        return false;
    }

    private WebElement getCalendarForMonth(String targetMonth) {
        List<WebElement> calendars = driver.findElements(By.cssSelector(".drp-calendar"));
        for (WebElement calendar : calendars) {
            WebElement header = calendar.findElement(By.cssSelector("th.month"));
            if (header.getText().toLowerCase().contains(targetMonth.toLowerCase()) ||
                    header.getText().toLowerCase().contains("июль 2025")) {
                return calendar;
            }
        }
        throw new NoSuchElementException("No calendar found showing " + targetMonth);
    }

    private void waitForCalendarUpdate() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".drp-calendar.left .loading")
        ));
    }

    private WebElement getReturnInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("input[data-note='Выберите дату обратно']")));
    }

    public void clickSearch() {
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
    }

}
