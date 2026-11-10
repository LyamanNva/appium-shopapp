package com.mydemo.driver;

import com.mydemo.config.Config;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

/**
 * Driver sessiyasını yaradır və saxlayır.
 * ThreadLocal -> paralel işləmədə hər thread-in öz driver-i olur.
 */
public final class DriverManager {

    private static final ThreadLocal<AppiumDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static AppiumDriver get() {
        AppiumDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("Driver yaradılmayıb. Əvvəlcə DriverManager.start() çağırın.");
        }
        return driver;
    }

    public static boolean isStarted() {
        return DRIVER.get() != null;
    }

    /** Android-ə xas metodlar (pressKey, openNotifications və s.) üçün. */
    public static AndroidDriver android() {
        return (AndroidDriver) get();
    }

    public static void start() {
        if (DRIVER.get() != null) {
            return;
        }
        AppiumDriver driver = "ios".equalsIgnoreCase(Config.get("platform"))
                ? createIos()
                : createAndroid();
        // Implicit wait = 0. Layihədə yalnız explicit wait istifadə olunur (ikisini qarışdırmaq məsləhət deyil).
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        DRIVER.set(driver);
    }

    public static void quit() {
        AppiumDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                DRIVER.remove();
            }
        }
    }

    // ---------------------------------------------------------------- Android

    private static AndroidDriver createAndroid() {
        // Capabilities: W3C formatında, "appium:" prefiksini Options sinfi özü əlavə edir
        UiAutomator2Options options = new UiAutomator2Options()
                .setDeviceName(Config.get("deviceName"))
                .setAppPackage(Config.get("appPackage"))
                .setAppActivity(Config.get("appActivity"))
                .setAutoGrantPermissions(true)          // runtime icazələri avtomatik verilir
                .setNoReset(false)                      // hər sessiyada tətbiq datası təmizlənir
                .setNewCommandTimeout(Duration.ofSeconds(Config.getInt("newCommandTimeout")));

        if (Config.has("udid")) {
            options.setUdid(Config.get("udid"));
        }
        if (Config.has("platformVersion")) {
            options.setPlatformVersion(Config.get("platformVersion"));
        }
        if (Config.has("app")) {
            options.setApp(absolute(Config.get("app")));
        }
        // Options sinfində hazır metodu olmayan capability-ləri belə əlavə etmək olar:
        options.setCapability("appium:disableWindowAnimation", true);
        options.setCapability("appium:appWaitActivity", "*");   // Splash -> Main keçidi üçün

        return new AndroidDriver(AppiumServer.url(), options);
    }

    // ---------------------------------------------------------------- iOS (şablon)

    private static IOSDriver createIos() {
        XCUITestOptions options = new XCUITestOptions()
                .setDeviceName(Config.get("deviceName"))
                .setNewCommandTimeout(Duration.ofSeconds(Config.getInt("newCommandTimeout")));

        if (Config.has("udid")) {
            options.setUdid(Config.get("udid"));
        }
        if (Config.has("platformVersion")) {
            options.setPlatformVersion(Config.get("platformVersion"));
        }
        if (Config.has("app")) {
            options.setApp(absolute(Config.get("app")));   // .app və ya .ipa
        }
        if (Config.has("bundleId")) {
            options.setBundleId(Config.get("bundleId"));
        }
        return new IOSDriver(AppiumServer.url(), options);
    }

    private static String absolute(String path) {
        Path p = Paths.get(path);
        return p.isAbsolute() ? p.toString() : Paths.get(System.getProperty("user.dir")).resolve(p).toString();
    }
}
