package com.hybrid.base;

import com.hybrid.config.ConfigReader;
import com.hybrid.driver.DriverFactory;
import com.hybrid.driver.DriverManager;
import com.hybrid.reports.ExtentManager;
import com.hybrid.utils.ExcelUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    @BeforeSuite(alwaysRun = true)
    public void globalSetup() {
        ExcelUtils.generateIfAbsent();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        String browser = System.getProperty("browser", ConfigReader.get("browser"));
        DriverManager.setDriver(DriverFactory.createDriver(browser));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }

    @AfterSuite(alwaysRun = true)
    public void globalTearDown() {
        ExtentManager.flush();
    }

    protected void navigateTo(String url) {
        DriverManager.getDriver().get(url);
    }
}
