package com.mydemo.tests;

import com.mydemo.pages.BasePage;
import com.mydemo.pages.CartPage;
import com.mydemo.pages.CheckoutPage;
import com.mydemo.pages.LoginPage;
import com.mydemo.utils.Waits;
import org.testng.Assert;
import org.testng.annotations.Test;

/** Tam alış-veriş ssenarisi (end-to-end) və forma validasiyası. */
public class CheckoutTest extends BaseTest {

    /** Məhsul əlavə et -> səbət -> checkout -> login -> çatdırılma forması. */
    private CheckoutPage goToShippingForm() {
        catalog.openProduct(0).addToCart().openCart();
        CartPage cart = new CartPage();
        cart.proceedToCheckout();
        new LoginPage().login(LoginPage.USER, LoginPage.PASSWORD);

        CheckoutPage checkout = new CheckoutPage();
        if (!checkout.isShippingFormShown()) {      // login kataloqa qaytarıbsa, səbətdən yenidən davam et
            checkout.openCart();
            cart.proceedToCheckout();
        }
        Waits.visible(BasePage.id("fullNameET"));
        return checkout;
    }

    @Test(description = "Boş ünvan forması göndərilmir, xəta göstərilir")
    public void shippingValidation() {
        CheckoutPage checkout = goToShippingForm();
        checkout.next();
        Assert.assertTrue(Waits.visible(BasePage.id("fullNameErrorTV")).isDisplayed());
        Assert.assertTrue(checkout.shown("fullNameET"), "Formada qalmalıdır");
    }

    @Test(description = "E2E: məhsul seç -> səbət -> login -> ünvan -> ödəniş -> sifarişi tamamla")
    public void fullPurchase() {
        CheckoutPage checkout = goToShippingForm();

        checkout.fillShipping("Tunzala Test", "Nizami 10", "Baku", "AZ1000", "Azerbaijan").next();
        checkout.fillPayment("Tunzala Test", "3258125675687891", "0330", "123").next();

        String total = checkout.reviewTotal();
        System.out.println("Sifarişin məbləği: " + total);
        Assert.assertTrue(total.contains("$"), "Məbləğ: " + total);

        checkout.next();   // Place Order
        Assert.assertEquals(checkout.completeMessage(), "Checkout Complete");

        checkout.continueShopping();
        catalog.waitUntilLoaded();
    }
}
