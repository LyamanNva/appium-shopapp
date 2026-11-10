package com.mydemo.utils;

import com.mydemo.config.Config;
import com.mydemo.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Explicit wait köməkçiləri. */
public final class Waits {

    private Waits() {
    }

    public static WebDriverWait waitFor(int seconds) {
        return new WebDriverWait(DriverManager.get(), Duration.ofSeconds(seconds));
    }

    public static WebDriverWait defaultWait() {
        return waitFor(Config.getInt("explicitWait"));
    }

    /** Element DOM-da var (görünməsi şərt deyil) — toast üçün əlverişlidir. */
    public static WebElement present(By by) {
        return defaultWait().until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public static WebElement visible(By by) {
        return defaultWait().until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public static WebElement clickable(By by) {
        return defaultWait().until(ExpectedConditions.elementToBeClickable(by));
    }

    public static boolean gone(By by) {
        return defaultWait().until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    /** Gözləmədən yoxlayır: element hazırda ekrandadır? */
    public static boolean exists(By by) {
        return !DriverManager.get().findElements(by).isEmpty();
    }
}
