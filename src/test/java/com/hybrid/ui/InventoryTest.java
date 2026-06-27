package com.hybrid.ui;

import com.hybrid.base.BaseTest;
import com.hybrid.config.ConfigReader;
import com.hybrid.pages.InventoryPage;
import com.hybrid.pages.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Feature("SauceDemo Inventory")
public class InventoryTest extends BaseTest {

    @BeforeMethod
    public void login() {
        navigateTo(ConfigReader.get("saucedemo.url"));
        new LoginPage()
                .enterUsername(ConfigReader.get("sauce.username"))
                .enterPassword(ConfigReader.get("sauce.password"))
                .clickLogin();
    }

    @Test(description = "Inventory page displays exactly 6 products")
    @Severity(SeverityLevel.NORMAL)
    @Description("SauceDemo always shows 6 products on the inventory page for a standard user.")
    public void testInventoryItemCount() {
        Assert.assertEquals(new InventoryPage().getInventoryItemCount(), 6,
                "Expected 6 inventory items");
    }

    @Test(description = "Adding one item updates the cart badge to 1")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddSingleItemToCart() {
        InventoryPage page = new InventoryPage();
        page.addItemByIndex(0);
        Assert.assertEquals(page.getCartCount(), 1, "Cart badge should show 1 after adding 1 item");
    }

    @Test(description = "Adding two items updates the cart badge to 2")
    @Severity(SeverityLevel.NORMAL)
    public void testAddMultipleItemsToCart() {
        InventoryPage page = new InventoryPage();
        page.addItemByIndex(0).addItemByIndex(1);
        Assert.assertEquals(page.getCartCount(), 2, "Cart badge should show 2 after adding 2 items");
    }

    @Test(description = "Page title is 'Products' on the inventory page")
    @Severity(SeverityLevel.MINOR)
    public void testInventoryPageTitle() {
        Assert.assertEquals(new InventoryPage().getPageTitle(), "Products");
    }
}
