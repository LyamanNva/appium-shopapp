package com.mydemo.pages;

import com.mydemo.utils.Waits;

/** Səbət ekranı. */
public class CartPage extends BasePage {

    public boolean isEmpty() {
        return appears("noItemTitleTV", 5);
    }

    public String emptyMessage() {
        return text("noItemTitleTV");
    }

    public void goShopping() {
        tap("shoppingBt");
    }

    public CartPage plus() {
        tap("plusIV");
        return this;
    }

    public int quantity() {
        return Integer.parseInt(text("noTV").trim());
    }

    public String itemsLabel() {
        return text("itemsTV");
    }

    public double total() {
        return Double.parseDouble(text("totalPriceTV").replaceAll("[^0-9.]", ""));
    }

    public CartPage remove() {
        find("removeBt").click();
        return this;
    }

    public void proceedToCheckout() {
        Waits.clickable(id("cartBt")).click();
    }
}
