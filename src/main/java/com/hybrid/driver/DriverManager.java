package com.hybrid.driver;

import org.openqa.selenium.WebDriver;

/**
 * ThreadLocal WebDriver holder — safe for parallel test execution.
 */
public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

    private DriverManager() {}

    public static WebDriver getDriver() {
        return DRIVER_THREAD_LOCAL.get();
    }

    public static void setDriver(WebDriver driver) {
        DRIVER_THREAD_LOCAL.set(driver);
    }

    public static void quitDriver() {
        if (DRIVER_THREAD_LOCAL.get() != null) {
            DRIVER_THREAD_LOCAL.get().quit();
            DRIVER_THREAD_LOCAL.remove();
        }
    }
}
