package com.mydemo.pages;

import com.mydemo.driver.DriverManager;
import com.mydemo.utils.Waits;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/** Bütün səhifələrin ortaq hissəsi: başlıq (menyu, səbət), sadə əməliyyatlar, scroll ilə axtarış. */
public class BasePage {

    public static final String APP_ID = "com.saucelabs.mydemoapp.android:id/";

    protected final AndroidDriver driver = DriverManager.android();

    public static By id(String shortId) {
        return AppiumBy.id(APP_ID + shortId);
    }

    public static By byText(String text) {
        return AppiumBy.androidUIAutomator("new UiSelector().text(\"" + text + "\")");
    }

    /** Element ekrandadırsa qaytarır, deyilsə UiScrollable ilə scroll edib tapır. */
    public WebElement find(String shortId) {
        List<WebElement> onScreen = driver.findElements(id(shortId));
        if (!onScreen.isEmpty()) {
            return onScreen.get(0);
        }
        try {
            return driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true).instance(0))"
                            + ".scrollIntoView(new UiSelector().resourceId(\"" + APP_ID + shortId + "\"))"));
        } catch (WebDriverException notScrollable) {
            return Waits.visible(id(shortId));
        }
    }

    public void tap(String shortId) {
        Waits.clickable(id(shortId)).click();
    }

    public String text(String shortId) {
        return Waits.visible(id(shortId)).getText();
    }

    public boolean shown(String shortId) {
        return Waits.exists(id(shortId));
    }

    /** Verilən saniyə ərzində element görünürmü (xəta atmadan). */
    public boolean appears(String shortId, int seconds) {
        try {
            Waits.waitFor(seconds).until(ExpectedConditions.presenceOfElementLocated(id(shortId)));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Sahəyə (lazımdırsa scroll edib) mətn yazır və klaviaturanı gizlədir. */
    public void fill(String shortId, String value) {
        WebElement field = find(shortId);
        field.click();
        field.clear();
        field.sendKeys(value);
        if (driver.isKeyboardShown()) {
            driver.hideKeyboard();
        }
    }

    /** Sistem icazə pəncərəsi çıxıbsa "Allow" basır (runtime permission). */
    public void allowPermissionIfAsked() {
        By allow = AppiumBy.id("com.android.permissioncontroller:id/permission_allow_button");
        try {
            Waits.waitFor(4).until(ExpectedConditions.elementToBeClickable(allow)).click();
        } catch (TimeoutException noDialog) {
            // icazə artıq verilib
        }
    }

    // ---- başlıq
    public void openMenu() {
        tap("menuIV");
    }

    public void openMenu(String itemText) {
        openMenu();
        Waits.clickable(byText(itemText)).click();
    }

    public void openCart() {
        tap("cartRL");
    }

    public String cartBadge() {
        return text("cartTV");
    }
}
