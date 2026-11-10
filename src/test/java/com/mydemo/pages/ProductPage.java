package com.mydemo.pages;

import com.mydemo.utils.Waits;

/** Məhsulun təfərrüat ekranı. */
public class ProductPage extends BasePage {

    public ProductPage waitUntilLoaded() {
        Waits.visible(id("cartBt"));
        return this;
    }

    public String title() {
        return text("productTV");
    }

    public String price() {
        return text("priceTV");
    }

    public ProductPage plus() {
        find("plusIV").click();
        return this;
    }

    public ProductPage minus() {
        find("minusIV").click();
        return this;
    }

    public int quantity() {
        return Integer.parseInt(find("noTV").getText().trim());
    }

    public int colorCount() {
        find("colorRV");
        return driver.findElements(id("colorIV")).size();
    }

    public ProductPage selectColor(int index) {
        driver.findElements(id("colorIV")).get(index).click();
        return this;
    }

    public ProductPage addToCart() {
        find("cartBt").click();
        return this;
    }
}
