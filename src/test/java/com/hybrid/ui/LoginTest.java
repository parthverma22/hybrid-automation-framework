package com.hybrid.ui;

import com.hybrid.base.BaseTest;
import com.hybrid.config.ConfigReader;
import com.hybrid.pages.InventoryPage;
import com.hybrid.pages.LoginPage;
import com.hybrid.utils.ExcelUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

@Feature("SauceDemo Login")
public class LoginTest extends BaseTest {

    @DataProvider(name = "loginData", parallel = false)
    public Object[][] getLoginData() {
        return ExcelUtils.getTestData("LoginData");
    }

    @Test(dataProvider = "loginData",
          description = "Data-driven login — credentials and expected outcome read from Excel via Apache POI")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies both valid and invalid login scenarios using data from an Excel workbook.")
    @SuppressWarnings("unchecked")
    public void testLoginWithExcelData(Map<String, String> data) {
        navigateTo(ConfigReader.get("saucedemo.url"));
        LoginPage loginPage = new LoginPage();
        loginPage.enterUsername(data.get("username")).enterPassword(data.get("password"));

        if ("success".equalsIgnoreCase(data.get("expectedResult"))) {
            InventoryPage inventory = loginPage.clickLogin();
            Assert.assertTrue(inventory.isOnInventoryPage(),
                    "Expected inventory page for user: " + data.get("username"));
        } else {
            loginPage.clickLogin(); // driver stays on login page when login fails
            String error = loginPage.getErrorMessage();
            Assert.assertTrue(error.contains("Epic sadface"),
                    "Expected error banner for user: " + data.get("username") + ". Got: " + error);
        }
    }

    @Test(description = "Valid credentials navigate to the Products inventory page")
    @Severity(SeverityLevel.BLOCKER)
    public void testValidLogin() {
        navigateTo(ConfigReader.get("saucedemo.url"));
        InventoryPage inventory = new LoginPage()
                .enterUsername(ConfigReader.get("sauce.username"))
                .enterPassword(ConfigReader.get("sauce.password"))
                .clickLogin();

        Assert.assertTrue(inventory.isOnInventoryPage(), "Should be on inventory page after valid login");
        Assert.assertEquals(inventory.getPageTitle(), "Products", "Page title should be 'Products'");
    }

    @Test(description = "Invalid credentials display an error banner without navigating away")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidLoginShowsError() {
        navigateTo(ConfigReader.get("saucedemo.url"));
        LoginPage loginPage = new LoginPage();
        loginPage.enterUsername("wrong_user").enterPassword("wrong_pass");
        loginPage.clickLogin(); // stays on login page

        String error = loginPage.getErrorMessage();
        Assert.assertTrue(error.contains("Username and password do not match"),
                "Unexpected error text: " + error);
    }
}
