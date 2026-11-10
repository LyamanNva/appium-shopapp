package com.mydemo.driver;

import com.mydemo.config.Config;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Appium serverini Java kodundan işə salır / dayandırır.
 * config.properties -> startServer=true olduqda istifadə olunur.
 * (Kompüterdə Node.js və "npm i -g appium" quraşdırılmış olmalıdır.)
 */
public final class AppiumServer {

    private static AppiumDriverLocalService service;

    private AppiumServer() {
    }

    public static synchronized void start() {
        if (!Config.getBool("startServer") || (service != null && service.isRunning())) {
            return;
        }
        service = new AppiumServiceBuilder()
                .withIPAddress("127.0.0.1")
                .usingAnyFreePort()
                .withTimeout(Duration.ofSeconds(60))
                .withEnvironment(environment())
                .withArgument(GeneralServerFlag.LOG_LEVEL, "info")
                // WebView üçün chromedriver-in avtomatik yüklənməsi və "mobile: shell" icazəsi (Appium 3 sintaksisi)
                .withArgument(GeneralServerFlag.ALLOW_INSECURE,
                        "uiautomator2:chromedriver_autodownload,uiautomator2:adb_shell")
                .build();
        service.start();
    }

    /** IDE-dən işlədəndə ANDROID_HOME olmaya bilər -> standart SDK yolunu əlavə edirik. */
    private static Map<String, String> environment() {
        Map<String, String> env = new HashMap<>(System.getenv());
        File sdk = new File(System.getProperty("user.home"), "Library/Android/sdk");
        if (!env.containsKey("ANDROID_HOME") && sdk.isDirectory()) {
            env.put("ANDROID_HOME", sdk.getAbsolutePath());
            env.put("ANDROID_SDK_ROOT", sdk.getAbsolutePath());
        }
        return env;
    }

    public static synchronized void stop() {
        if (service != null && service.isRunning()) {
            service.stop();
        }
    }

    /** İşləyən serverin ünvanı: ya lokal başladılan, ya da config-dəki serverUrl. */
    public static URL url() {
        try {
            if (service != null && service.isRunning()) {
                return service.getUrl();
            }
            return new URL(Config.get("serverUrl"));
        } catch (MalformedURLException e) {
            throw new IllegalStateException("serverUrl yanlışdır: " + Config.get("serverUrl"), e);
        }
    }
}
