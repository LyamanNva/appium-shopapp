package com.mydemo.tests;

import com.mydemo.pages.BasePage;
import com.mydemo.utils.Gestures.Direction;
import com.mydemo.utils.Waits;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Kataloq: siyahı, çeşidləmə, swipe/scroll, reytinq dialoqu. */
public class CatalogTest extends BaseTest {

    @Test(description = "Məhsullar ad və qiymətlə göstərilir")
    public void productsAreListed() {
        Assert.assertEquals(catalog.text("productTV"), "Products");
        List<String> titles = catalog.titles();
        System.out.println("Məhsullar: " + titles + "  qiymətlər: " + catalog.prices());
        Assert.assertTrue(titles.size() >= 2);
        Assert.assertTrue(titles.get(0).startsWith("Sauce Labs"), "İlk məhsul: " + titles.get(0));
    }

    @Test(description = "Çeşidləmə: qiymət artan sıra ilə")
    public void sortByPriceAscending() {
        List<Double> prices = catalog.sortBy("priceAscCL").prices();
        List<Double> sorted = new ArrayList<>(prices);
        sorted.sort(Comparator.naturalOrder());
        Assert.assertEquals(prices, sorted, "Qiymətlər artan sıra ilə olmalıdır");
    }

    @Test(description = "Çeşidləmə: ad Z-dən A-ya")
    public void sortByNameDescending() {
        List<String> titles = catalog.sortBy("nameDesCL").titles();
        List<String> sorted = new ArrayList<>(titles);
        sorted.sort(String.CASE_INSENSITIVE_ORDER.reversed());
        Assert.assertEquals(titles, sorted, "Adlar azalan sıra ilə olmalıdır");
    }

    @Test(description = "W3C swipe: səhifənin sonuna qədər sürüşdür və geri qayıt")
    public void swipeToBottomAndBack() {
        int swipes = 0;
        while (!catalog.shown("twitterIV") && swipes++ < 10) {
            gestures.swipe(Direction.UP, 0.6);
        }
        Assert.assertTrue(catalog.shown("twitterIV"), "Səhifənin sonundakı sosial ikonlar görünməlidir");

        swipes = 0;
        while (!catalog.shown("productTV") && swipes++ < 10) {
            gestures.swipe(Direction.DOWN, 0.6);
        }
        Assert.assertTrue(catalog.shown("productTV"));
    }

    @Test(description = "mobile: scrollGesture — sona çatana qədər scroll")
    public void scrollGestureToEnd() {
        WebElement scrollView = Waits.visible(BasePage.id("scrollView"));
        int steps = 0;
        while (mobile.scroll(scrollView, "down", 1.0) && steps++ < 15) {
            // true qayıdırsa hələ scroll etmək olur
        }
        Assert.assertTrue(catalog.shown("twitterIV"));
    }

    @Test(description = "Ulduzla reytinq vermək -> təşəkkür dialoqu")
    public void rateProduct() {
        catalog.tap("start4IV");
        Assert.assertEquals(catalog.text("sortTV"), "Thank you for submitting your review!");
        catalog.tap("closeBt");
        Assert.assertTrue(Waits.gone(BasePage.id("closeBt")));
    }
}
