package com.mydemo.tests;

import com.mydemo.pages.BasePage;
import com.mydemo.utils.Screenshots;
import com.mydemo.utils.Waits;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

/** Qalan Appium imkanları: barmaqla çəkmək, WebView, arxa plan, ekran şəkli. */
public class ExtrasTest extends BaseTest {

    @Test(description = "Drawing: W3C Actions ilə imza lövhəsində fiqur çəkmək, sonra təmizləmək")
    public void drawSignature() {
        catalog.openMenu("Drawing");
        catalog.allowPermissionIfAsked();
        WebElement pad = Waits.visible(BasePage.id("signature_pad"));
        Rectangle r = pad.getRect();
        String empty = pad.getScreenshotAs(OutputType.BASE64);

        int left = r.x + r.width / 4, right = r.x + 3 * r.width / 4;
        int top = r.y + r.height / 4, bottom = r.y + 3 * r.height / 4;
        gestures.draw(List.of(new Point(left, bottom), new Point((left + right) / 2, top),
                new Point(right, bottom), new Point(left, bottom)));          // üçbucaq

        pad = Waits.visible(BasePage.id("signature_pad"));
        String drawn = pad.getScreenshotAs(OutputType.BASE64);
        Assert.assertNotEquals(drawn, empty, "Çəkilən xətt lövhədə görünməlidir");
        Screenshots.element(pad, "signature");

        catalog.tap("clearBtn");
        pad = Waits.visible(BasePage.id("signature_pad"));
        Assert.assertNotEquals(pad.getScreenshotAs(OutputType.BASE64), drawn, "Clear lövhəni təmizləməlidir");
    }

    @Test(description = "WebView: yanlış URL xəta verir, düzgün URL WebView açır; kontekstlər siyahısı")
    public void webView() {
        catalog.openMenu("WebView");
        catalog.fill("urlET", "yanlis url");
        catalog.tap("goBtn");
        Assert.assertTrue(Waits.visible(BasePage.id("urlErrorTV")).isDisplayed());

        catalog.fill("urlET", "https://www.google.com");
        catalog.tap("goBtn");
        Assert.assertTrue(catalog.appears("webView", 15), "WebView açılmalıdır");
        // Hibrid tətbiqdə: driver.context("WEBVIEW_...") ilə HTML-ə keçmək olur
        System.out.println("Kontekstlər: " + driver.getContextHandles());
        Assert.assertEquals(driver.getContext(), "NATIVE_APP");
    }

    @Test(description = "About ekranı")
    public void aboutScreen() {
        catalog.openMenu("About");
        String version = catalog.text("versionTV");
        System.out.println("Versiya: " + version);
        Assert.assertTrue(version.toUpperCase().contains("V"), "Versiya: " + version);
    }

    @Test(description = "Tətbiq arxa plana gedib qayıdanda səbət saxlanılır")
    public void backgroundKeepsCart() {
        catalog.openProduct(0).addToCart();
        driver.runAppInBackground(Duration.ofSeconds(2));
        Assert.assertEquals(catalog.cartBadge(), "1");
    }

    @Test(description = "Ekran şəkli və page source")
    public void screenshotAndPageSource() throws Exception {
        Path shot = Screenshots.screen("catalog");
        System.out.println("Ekran şəkli: " + shot.toAbsolutePath());
        Assert.assertTrue(Files.size(shot) > 0);
        Assert.assertTrue(driver.getPageSource().contains("com.saucelabs.mydemoapp.android"));
    }
}
