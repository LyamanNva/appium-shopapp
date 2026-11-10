package com.mydemo.pages;

import com.mydemo.utils.Waits;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

/** Məhsul kataloqu (ana ekran). */
public class CatalogPage extends BasePage {

    public CatalogPage waitUntilLoaded() {
        Waits.visible(id("productRV"));
        Waits.visible(id("titleTV"));
        return this;
    }

    public List<String> titles() {
        return driver.findElements(id("titleTV")).stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public List<Double> prices() {
        return driver.findElements(id("priceTV")).stream()
                .map(e -> Double.parseDouble(e.getText().replaceAll("[^0-9.]", "")))
                .collect(Collectors.toList());
    }

    /** optionId: nameAscCL | nameDesCL | priceAscCL | priceDesCL */
    public CatalogPage sortBy(String optionId) {
        tap("sortIV");
        tap(optionId);
        return waitUntilLoaded();
    }

    public ProductPage openProduct(int index) {
        driver.findElements(id("productIV")).get(index).click();
        return new ProductPage().waitUntilLoaded();
    }
}
