package utils;

import io.qameta.allure.Attachment;
import org.openqa.selenium.*;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class AllureUtils {

    @Attachment(value = "Screenshot", type = "image/png")
    public static byte[] takeScreenshot(WebDriver driver) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception ignored) {
            return new byte[0];
        }
    }

    @Attachment(value = "Page Source", type = "text/html", fileExtension = ".html")
    public static byte[] attachPageSource(WebDriver driver) {
        try {
            return driver.getPageSource().getBytes(StandardCharsets.UTF_8);
        } catch (Exception ignored) {
            return "Page source unavailable".getBytes(StandardCharsets.UTF_8);
        }
    }

    @Attachment(value = "Browser Console Logs", type = "text/plain")
    public static String attachConsoleLogs(WebDriver driver) {
        try {
            List<LogEntry> entries = driver.manage().logs().get(LogType.BROWSER).getAll();
            if (entries == null || entries.isEmpty()) {
                return "No console logs captured.";
            }
            return entries.stream()
                    .map(e -> String.format("[%s] %s", e.getLevel(), e.getMessage()))
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (Exception e) {
            return "Console logs are not available for this driver or were not enabled.";
        }
    }
}