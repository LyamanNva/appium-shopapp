package com.mydemo.tests;

import com.mydemo.pages.CartPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/** Səbət: boş vəziyyət, say və məbləğ, silmə. */
public class CartTest extends BaseTest {

    @Test(description = "Boş səbət: 'No Items' və 'Go Shopping'")
    public void emptyCart() {
        catalog.openCart();
        CartPage cart = new CartPage();
        Assert.assertTrue(cart.isEmpty());
        Assert.assertEquals(cart.emptyMessage(), "No Items");
        cart.goShopping();
        catalog.waitUntilLoaded();
    }

    @Test(description = "Səbətdə sayı artırdıqda ümumi məbləğ iki qat olur")
    public void increaseQuantityUpdatesTotal() {
        catalog.openProduct(0).addToCart().openCart();
        CartPage cart = new CartPage();
        double single = cart.total();

        cart.plus();

        Assert.assertEquals(cart.quantity(), 2);
        Assert.assertEquals(cart.total(), single * 2, 0.01);
        Assert.assertTrue(cart.itemsLabel().startsWith("2"), "Etiket: " + cart.itemsLabel());
    }

    @Test(description = "Məhsulu səbətdən silmək")
    public void removeItem() {
        catalog.openProduct(0).addToCart().openCart();
        CartPage cart = new CartPage();
        cart.remove();
        Assert.assertTrue(cart.isEmpty(), "Silindikdən sonra səbət boş olmalıdır");
    }
}
