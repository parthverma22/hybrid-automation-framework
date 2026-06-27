package com.hybrid.pages;

import com.hybrid.driver.DriverManager;
import com.hybrid.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    private final WebDriver driver;

    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton   = By.id("login-button");
    private final By errorMessage  = By.cssSelector("[data-test='error']");

    public LoginPage() {
        this.driver = DriverManager.getDriver();
    }

    public LoginPage enterUsername(String username) {
        WaitUtils.waitForVisible(driver, usernameField).clear();
        driver.findElement(usernameField).sendKeys(username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        driver.findElement(passwordField).clear();
        driver.findElement(passwordField).sendKeys(password);
        return this;
    }

    /** Click login — returns InventoryPage for the happy path. */
    public InventoryPage clickLogin() {
        WaitUtils.waitForClickable(driver, loginButton).click();
        return new InventoryPage();
    }

    /** Returns the visible error banner text. Only present when login fails. */
    public String getErrorMessage() {
        return WaitUtils.waitForVisible(driver, errorMessage).getText();
    }
}
