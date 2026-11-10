package com.mydemo.pages;

import com.mydemo.utils.Waits;

/** Sifariş: çatdırılma ünvanı -> ödəniş -> yoxlama -> tamamlandı. */
public class CheckoutPage extends BasePage {

    public boolean isShippingFormShown() {
        return appears("fullNameET", 6);
    }

    public CheckoutPage fillShipping(String name, String address, String city, String zip, String country) {
        fill("fullNameET", name);
        fill("address1ET", address);
        fill("cityET", city);
        fill("zipET", zip);
        fill("countryET", country);
        return this;
    }

    /** Hər üç addımda eyni düymə: To Payment / Review Order / Place Order. */
    public CheckoutPage next() {
        Waits.clickable(id("paymentBtn")).click();
        return this;
    }

    public CheckoutPage fillPayment(String holder, String cardNumber, String expiry, String cvv) {
        Waits.visible(id("cardNumberET"));
        fill("nameET", holder);
        fill("cardNumberET", cardNumber);
        fill("expirationDateET", expiry);
        fill("securityCodeET", cvv);
        return this;
    }

    public String reviewTotal() {
        return text("totalAmountTV");
    }

    public String completeMessage() {
        return text("completeTV");
    }

    public void continueShopping() {
        tap("shoopingBt");
    }
}
