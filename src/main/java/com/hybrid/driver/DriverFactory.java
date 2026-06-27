package com.hybrid.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

import static com.hybrid.constants.FrameworkConstants.PAGE_LOAD_TIMEOUT;

public final class DriverFactory {

    private DriverFactory() {}

    public static WebDriver createDriver(String browser) {
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        WebDriver driver;

        switch (browser.toLowerCase().trim()) {
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions opts = new FirefoxOptions();
                if (headless) opts.addArguments("--headless");
                driver = new FirefoxDriver(opts);
            }
            default -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions opts = new ChromeOptions();
                opts.addArguments("--start-maximized", "--disable-notifications", "--disable-infobars");
                if (headless) opts.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
                driver = new ChromeDriver(opts);
            }
        }

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        return driver;
    }
}
