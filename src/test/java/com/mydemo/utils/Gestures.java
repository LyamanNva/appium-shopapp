package com.mydemo.utils;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.List;

import static org.openqa.selenium.interactions.PointerInput.Kind.TOUCH;
import static org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT;
import static org.openqa.selenium.interactions.PointerInput.Origin.viewport;

/**
 * W3C Actions API ilə jestlər (Android və iOS-da eyni işləyir).
 * Köhnə TouchAction / MultiTouchAction silinib — müasir üsul budur.
 *
 * Hər jest = barmaq (PointerInput) + addımlar ardıcıllığı (Sequence):
 *   move -> down -> (pause / move) -> up
 */
public class Gestures {

    public enum Direction { UP, DOWN, LEFT, RIGHT }

    private final AppiumDriver driver;

    public Gestures(AppiumDriver driver) {
        this.driver = driver;
    }

    // ------------------------------------------------------------ köməkçilər

    private static PointerInput finger(String name) {
        return new PointerInput(TOUCH, name);
    }

    public static Point center(WebElement element) {
        Rectangle r = element.getRect();
        return new Point(r.x + r.width / 2, r.y + r.height / 2);
    }

    // ------------------------------------------------------------ TAP

    /** Koordinata toxunuş. */
    public void tap(Point p) {
        PointerInput f = finger("finger");
        Sequence tap = new Sequence(f, 0)
                .addAction(f.createPointerMove(Duration.ZERO, viewport(), p.x, p.y))
                .addAction(f.createPointerDown(LEFT.asArg()))
                .addAction(new Pause(f, Duration.ofMillis(80)))
                .addAction(f.createPointerUp(LEFT.asArg()));
        driver.perform(List.of(tap));
    }

    public void tap(WebElement element) {
        tap(center(element));
    }

    /** İki sürətli toxunuş. */
    public void doubleTap(Point p) {
        PointerInput f = finger("finger");
        Sequence seq = new Sequence(f, 0)
                .addAction(f.createPointerMove(Duration.ZERO, viewport(), p.x, p.y))
                .addAction(f.createPointerDown(LEFT.asArg()))
                .addAction(new Pause(f, Duration.ofMillis(60)))
                .addAction(f.createPointerUp(LEFT.asArg()))
                .addAction(new Pause(f, Duration.ofMillis(100)))
                .addAction(f.createPointerDown(LEFT.asArg()))
                .addAction(new Pause(f, Duration.ofMillis(60)))
                .addAction(f.createPointerUp(LEFT.asArg()));
        driver.perform(List.of(seq));
    }

    public void doubleTap(WebElement element) {
        doubleTap(center(element));
    }

    // ------------------------------------------------------------ LONG PRESS

    public void longPress(Point p, Duration hold) {
        PointerInput f = finger("finger");
        Sequence seq = new Sequence(f, 0)
                .addAction(f.createPointerMove(Duration.ZERO, viewport(), p.x, p.y))
                .addAction(f.createPointerDown(LEFT.asArg()))
                .addAction(new Pause(f, hold))
                .addAction(f.createPointerUp(LEFT.asArg()));
        driver.perform(List.of(seq));
    }

    public void longPress(WebElement element) {
        longPress(center(element), Duration.ofSeconds(2));
    }

    // ------------------------------------------------------------ SWIPE / SCROLL

    /** Bir nöqtədən digərinə sürüşdürmə. duration kiçik olduqca "fling"ə çevrilir. */
    public void swipe(Point from, Point to, Duration duration) {
        PointerInput f = finger("finger");
        Sequence seq = new Sequence(f, 0)
                .addAction(f.createPointerMove(Duration.ZERO, viewport(), from.x, from.y))
                .addAction(f.createPointerDown(LEFT.asArg()))
                .addAction(new Pause(f, Duration.ofMillis(100)))
                .addAction(f.createPointerMove(duration, viewport(), to.x, to.y))
                .addAction(f.createPointerUp(LEFT.asArg()));
        driver.perform(List.of(seq));
    }

    /**
     * Ekranın ortasında barmağı verilən istiqamətə sürüşdürür.
     * UP = barmaq yuxarı gedir -> məzmun aşağı scroll olur.
     *
     * @param percent ekranın neçə faizi qədər məsafə (0.1 - 0.9)
     */
    public void swipe(Direction direction, double percent) {
        Dimension size = driver.manage().window().getSize();
        swipeInside(new Rectangle(0, 0, size.height, size.width), direction, percent);
    }

    /** Konkret elementin (məs. qalereya, siyahı) içində sürüşdürmə. */
    public void swipe(WebElement element, Direction direction, double percent) {
        swipeInside(element.getRect(), direction, percent);
    }

    private void swipeInside(Rectangle area, Direction direction, double percent) {
        int cx = area.x + area.width / 2;
        int cy = area.y + area.height / 2;
        int dx = (int) (area.width * percent / 2);
        int dy = (int) (area.height * percent / 2);

        Point from;
        Point to;
        switch (direction) {
            case UP:
                from = new Point(cx, cy + dy);
                to = new Point(cx, cy - dy);
                break;
            case DOWN:
                from = new Point(cx, cy - dy);
                to = new Point(cx, cy + dy);
                break;
            case LEFT:
                from = new Point(cx + dx, cy);
                to = new Point(cx - dx, cy);
                break;
            default: // RIGHT
                from = new Point(cx - dx, cy);
                to = new Point(cx + dx, cy);
        }
        swipe(from, to, Duration.ofMillis(600));
    }

    // ------------------------------------------------------------ DRAG & DROP

    /** Elementi basıb saxla -> hədəfə apar -> burax. */
    public void dragAndDrop(WebElement source, WebElement target) {
        dragAndDrop(center(source), center(target));
    }

    public void dragAndDrop(Point from, Point to) {
        PointerInput f = finger("finger");
        Sequence seq = new Sequence(f, 0)
                .addAction(f.createPointerMove(Duration.ZERO, viewport(), from.x, from.y))
                .addAction(f.createPointerDown(LEFT.asArg()))
                .addAction(new Pause(f, Duration.ofMillis(1000)))          // long press -> drag başlayır
                .addAction(f.createPointerMove(Duration.ofMillis(1000), viewport(), to.x, to.y))
                .addAction(new Pause(f, Duration.ofMillis(300)))
                .addAction(f.createPointerUp(LEFT.asArg()));
        driver.perform(List.of(seq));
    }

    // ------------------------------------------------------------ MULTI-TOUCH (pinch / zoom)

    /** İki barmaq mərkəzdən kənara -> böyütmə (zoom in). */
    public void zoom(WebElement element) {
        Rectangle r = element.getRect();
        Point c = center(element);
        int offset = Math.min(r.width, r.height) / 3;
        twoFingers(
                new Point(c.x - 20, c.y), new Point(c.x - offset, c.y),
                new Point(c.x + 20, c.y), new Point(c.x + offset, c.y));
    }

    /** İki barmaq kənardan mərkəzə -> kiçiltmə (pinch / zoom out). */
    public void pinch(WebElement element) {
        Rectangle r = element.getRect();
        Point c = center(element);
        int offset = Math.min(r.width, r.height) / 3;
        twoFingers(
                new Point(c.x - offset, c.y), new Point(c.x - 20, c.y),
                new Point(c.x + offset, c.y), new Point(c.x + 20, c.y));
    }

    /** İki barmağın eyni anda hərəkəti — iki Sequence birlikdə perform olunur. */
    public void twoFingers(Point from1, Point to1, Point from2, Point to2) {
        PointerInput f1 = finger("finger1");
        PointerInput f2 = finger("finger2");
        Duration move = Duration.ofMillis(700);

        Sequence s1 = new Sequence(f1, 0)
                .addAction(f1.createPointerMove(Duration.ZERO, viewport(), from1.x, from1.y))
                .addAction(f1.createPointerDown(LEFT.asArg()))
                .addAction(new Pause(f1, Duration.ofMillis(100)))
                .addAction(f1.createPointerMove(move, viewport(), to1.x, to1.y))
                .addAction(f1.createPointerUp(LEFT.asArg()));

        Sequence s2 = new Sequence(f2, 0)
                .addAction(f2.createPointerMove(Duration.ZERO, viewport(), from2.x, from2.y))
                .addAction(f2.createPointerDown(LEFT.asArg()))
                .addAction(new Pause(f2, Duration.ofMillis(100)))
                .addAction(f2.createPointerMove(move, viewport(), to2.x, to2.y))
                .addAction(f2.createPointerUp(LEFT.asArg()));

        driver.perform(List.of(s1, s2));
    }

    // ------------------------------------------------------------ SƏRBƏST ÇƏKMƏ (imza, şəkil)

    /** Nöqtələri ardıcıl birləşdirərək ekranda xətt çəkir. */
    public void draw(List<Point> points) {
        PointerInput f = finger("finger");
        Sequence seq = new Sequence(f, 0)
                .addAction(f.createPointerMove(Duration.ZERO, viewport(), points.get(0).x, points.get(0).y))
                .addAction(f.createPointerDown(LEFT.asArg()));
        for (Point p : points.subList(1, points.size())) {
            seq.addAction(f.createPointerMove(Duration.ofMillis(250), viewport(), p.x, p.y));
        }
        seq.addAction(f.createPointerUp(LEFT.asArg()));
        driver.perform(List.of(seq));
    }
}
