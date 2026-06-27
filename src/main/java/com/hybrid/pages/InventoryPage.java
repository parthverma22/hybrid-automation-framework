package com.hybrid.pages;

import com.hybrid.driver.DriverManager;
import com.hybrid.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class InventoryPage {

    private final WebDriver driver;

    private final By pageTitle      = By.className("title");
    private final By addToCartBtns  = By.cssSelector("[data-test^='add-to-cart']");
    private final By cartBadge      = By.className("shopping_cart_badge");
    private final By inventoryItems = By.className("inventory_item");

    public InventoryPage() {
        this.driver = DriverManager.getDriver();
    }

    public boolean isOnInventoryPage() {
        return driver.getCurrentUrl().contains("inventory");
    }

    public String getPageTitle() {
        return WaitUtils.waitForVisible(driver, pageTitle).getText();
    }

    public InventoryPage addItemByIndex(int index) {
        List<WebElement> buttons = driver.findElements(addToCartBtns);
        if (index >= 0 && index < buttons.size()) {
            buttons.get(index).click();
        }
        return this;
    }

    public int getCartCount() {
        List<WebElement> badges = driver.findElements(cartBadge);
        return badges.isEmpty() ? 0 : Integer.parseInt(badges.get(0).getText());
    }

    public int getInventoryItemCount() {
        WaitUtils.waitForVisible(driver, inventoryItems);
        return driver.findElements(inventoryItems).size();
    }
}
