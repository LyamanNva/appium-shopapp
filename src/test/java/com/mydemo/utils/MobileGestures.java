package com.mydemo.utils;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.HashMap;
import java.util.Map;

/**
 * UiAutomator2 driver-in hazır jest əmrləri:  driver.executeScript("mobile: ...Gesture", params)
 * W3C Actions-dan qısadır, amma yalnız Android-də işləyir.
 * (iOS-da analoqları: mobile: swipe, mobile: scroll, mobile: pinch, mobile: touchAndHold, mobile: dragFromToForDuration ...)
 *
 * Qeyd: elementId lazım olduğu üçün buraya driver.findElement(...) ilə tapılmış element verin
 * (PageFactory proxy-si yox).
 */
public class MobileGestures {

    private final AppiumDriver driver;

    public MobileGestures(AppiumDriver driver) {
        this.driver = driver;
    }

    private static String id(WebElement element) {
        return ((RemoteWebElement) element).getId();
    }

    public void click(WebElement element) {
        driver.executeScript("mobile: clickGesture", Map.of("elementId", id(element)));
    }

    public void clickAt(int x, int y) {
        driver.executeScript("mobile: clickGesture", Map.of("x", x, "y", y));
    }

    public void doubleClick(WebElement element) {
        driver.executeScript("mobile: doubleClickGesture", Map.of("elementId", id(element)));
    }

    public void longClick(WebElement element, int durationMs) {
        driver.executeScript("mobile: longClickGesture", Map.of(
                "elementId", id(element),
                "duration", durationMs));
    }

    public void drag(WebElement element, int endX, int endY) {
        driver.executeScript("mobile: dragGesture", Map.of(
                "elementId", id(element),
                "endX", endX,
                "endY", endY));
    }

    /** direction: up | down | left | right;  percent: 0.0 - 1.0 */
    public void swipe(WebElement element, String direction, double percent) {
        driver.executeScript("mobile: swipeGesture", Map.of(
                "elementId", id(element),
                "direction", direction,
                "percent", percent));
    }

    /** Element əvəzinə ekran sahəsi üzrə swipe. */
    public void swipeArea(int left, int top, int width, int height, String direction, double percent) {
        Map<String, Object> params = new HashMap<>();
        params.put("left", left);
        params.put("top", top);
        params.put("width", width);
        params.put("height", height);
        params.put("direction", direction);
        params.put("percent", percent);
        driver.executeScript("mobile: swipeGesture", params);
    }

    /** @return true -> hələ scroll etmək mümkündür; false -> sona çatdı */
    public boolean scroll(WebElement element, String direction, double percent) {
        Object more = driver.executeScript("mobile: scrollGesture", Map.of(
                "elementId", id(element),
                "direction", direction,
                "percent", percent));
        return Boolean.TRUE.equals(more);
    }

    /** Sürətli atma (fling). */
    public boolean fling(WebElement element, String direction) {
        Object more = driver.executeScript("mobile: flingGesture", Map.of(
                "elementId", id(element),
                "direction", direction));
        return Boolean.TRUE.equals(more);
    }

    /** Zoom in. */
    public void pinchOpen(WebElement element, double percent) {
        driver.executeScript("mobile: pinchOpenGesture", Map.of(
                "elementId", id(element),
                "percent", percent));
    }

    /** Zoom out. */
    public void pinchClose(WebElement element, double percent) {
        driver.executeScript("mobile: pinchCloseGesture", Map.of(
                "elementId", id(element),
                "percent", percent));
    }
}
