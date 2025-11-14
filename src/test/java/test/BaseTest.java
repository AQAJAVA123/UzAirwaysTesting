package test;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public abstract class BaseTest {
    protected static WebDriver driver;
    protected static final String BASE_URL = "https://uzairways.com/en";
    protected static final String AUTH_URL = "https://percab.uzairways.com";

    public static WebDriver getDriver() {
        return driver;
    }

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        writeEnvironmentInfo();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    public void writeEnvironmentInfo() {
        try {
            Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();

            Properties props = new Properties();
            props.setProperty("Browser", caps.getBrowserName() + " " + caps.getBrowserVersion());
            props.setProperty("OS", System.getProperty("os.name"));

            FileOutputStream fos = new FileOutputStream("target/allure-results/environment.properties");
            props.store(fos, "Allure Environment Properties");
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
