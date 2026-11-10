package com.mydemo.tests;

import com.mydemo.config.Config;
import com.mydemo.driver.AppiumServer;
import com.mydemo.driver.DriverManager;
import com.mydemo.pages.CatalogPage;
import com.mydemo.utils.Gestures;
import com.mydemo.utils.MobileGestures;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.ScreenOrientation;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.util.Map;

/**
 * Bütün dəst üçün BİR sessiya açılır (sürətli olsun deyə).
 * Hər testdən əvvəl tətbiqin datası təmizlənir -> səbət boş, istifadəçi çıxış etmiş vəziyyətdə başlayır.
 */
public abstract class BaseTest {

    protected static final String PKG = Config.appPackage();

    protected AndroidDriver driver;
    protected Gestures gestures;
    protected MobileGestures mobile;
    protected CatalogPage catalog;

    @BeforeSuite(alwaysRun = true)
    public void startSession() {
        AppiumServer.start();
        DriverManager.start();
    }

    @AfterSuite(alwaysRun = true)
    public void stopSession() {
        DriverManager.quit();
        AppiumServer.stop();
    }

    @BeforeClass(alwaysRun = true)
    public void init() {
        driver = DriverManager.android();
        gestures = new Gestures(driver);
        mobile = new MobileGestures(driver);
    }

    @BeforeMethod(alwaysRun = true)
    public void freshApp() {
        if (driver.getOrientation() != ScreenOrientation.PORTRAIT) {
            driver.rotate(ScreenOrientation.PORTRAIT);
        }
        driver.executeScript("mobile: clearApp", Map.of("appId", PKG));
        driver.activateApp(PKG);
        catalog = new CatalogPage().waitUntilLoaded();
    }
}
