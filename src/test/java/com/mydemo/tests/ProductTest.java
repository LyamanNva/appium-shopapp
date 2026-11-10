package com.mydemo.tests;

import com.mydemo.pages.ProductPage;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.testng.Assert;
import org.testng.annotations.Test;

/** Məhsul ekranı: təfərrüat, say, rəng, səbətə əlavə, BACK düyməsi. */
public class ProductTest extends BaseTest {

    @Test(description = "Kataloqdan seçilən məhsulun təfərrüatı açılır")
    public void openDetails() {
        String expected = catalog.titles().get(0);
        ProductPage product = catalog.openProduct(0);
        Assert.assertEquals(product.title(), expected);
        Assert.assertTrue(product.price().contains("$"), "Qiymət: " + product.price());
    }

    @Test(description = "Say: + və - düymələri")
    public void changeQuantity() {
        ProductPage product = catalog.openProduct(0);
        Assert.assertEquals(product.quantity(), 1);
        product.plus().plus();
        Assert.assertEquals(product.quantity(), 3);
        product.minus();
        Assert.assertEquals(product.quantity(), 2);
    }

    @Test(description = "Rəng seçimi")
    public void selectColor() {
        ProductPage product = catalog.openProduct(0);
        int colors = product.colorCount();
        System.out.println("Rəng sayı: " + colors);
        Assert.assertTrue(colors >= 1);
        product.selectColor(colors - 1);
    }

    @Test(description = "Səbətə əlavə etdikdə başlıqdakı sayğac artır")
    public void addToCartUpdatesBadge() {
        ProductPage product = catalog.openProduct(0);
        product.plus().addToCart();
        Assert.assertEquals(product.cartBadge(), "2");
    }

    @Test(description = "Android BACK düyməsi kataloqa qaytarır")
    public void backKeyReturnsToCatalog() {
        catalog.openProduct(0);
        driver.pressKey(new KeyEvent(AndroidKey.BACK));
        catalog.waitUntilLoaded();
        Assert.assertEquals(catalog.text("productTV"), "Products");
    }
}
