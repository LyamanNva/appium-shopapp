package com.mydemo.tests;

import com.mydemo.pages.BasePage;
import com.mydemo.pages.LoginPage;
import com.mydemo.utils.Waits;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/** Login: validasiya (data-driven), hazır istifadəçi, giriş/çıxış, clipboard. */
public class LoginTest extends BaseTest {

    @DataProvider(name = "invalidLogins")
    public Object[][] invalidLogins() {
        return new Object[][]{
                // istifadəçi, şifrə, görünməli olan xəta elementi, xətada olmalı mətn
                {"", LoginPage.PASSWORD, "nameErrorTV", "Username is required"},
                {LoginPage.USER, "", "passwordErrorTV", ""},
                {LoginPage.LOCKED_USER, LoginPage.PASSWORD, "passwordErrorTV", "locked out"},
        };
    }

    @Test(dataProvider = "invalidLogins", description = "Yanlış giriş cəhdləri xəta göstərir")
    public void invalidLogin(String user, String password, String errorId, String expectedText) {
        LoginPage login = new LoginPage().openFromMenu().login(user, password);
        String error = Waits.visible(BasePage.id(errorId)).getText();
        System.out.println("Xəta mesajı: " + error);
        Assert.assertTrue(error.contains(expectedText), "Xəta: " + error);
        Assert.assertTrue(login.shown("loginBtn"), "Login ekranında qalmalıdır");
    }

    @Test(description = "Ekrandakı hazır istifadəçiyə toxunduqda forma dolur")
    public void savedCredentialsFillForm() {
        LoginPage login = new LoginPage().openFromMenu();
        login.find("username1TV").click();
        Assert.assertEquals(login.username(), LoginPage.USER);
    }

    @Test(description = "Uğurlu giriş, sonra menyudan çıxış (təsdiq dialoqu ilə)")
    public void validLoginAndLogout() {
        LoginPage login = new LoginPage().openFromMenu().login(LoginPage.USER, LoginPage.PASSWORD);
        catalog.waitUntilLoaded();

        login.openMenu();
        Waits.clickable(LoginPage.LOGOUT_MENU_ITEM).click();

        // "Are you sure you want to logout" -> LOGOUT, ardınca gələ bilən məlumat dialoqu -> OK
        By positive = AppiumBy.id("android:id/button1");
        Waits.clickable(positive).click();
        try {
            Waits.waitFor(3).until(d -> !d.findElements(positive).isEmpty());
            driver.findElement(positive).click();
        } catch (org.openqa.selenium.TimeoutException noSecondDialog) {
            // ikinci dialoq yoxdur
        }

        if (!Waits.exists(LoginPage.LOGIN_MENU_ITEM)) {
            login.openMenu();
        }
        Assert.assertTrue(Waits.visible(LoginPage.LOGIN_MENU_ITEM).isDisplayed(), "Çıxışdan sonra 'Log In' görünməlidir");
    }

    @Test(description = "Clipboard-dan mətn yapışdırmaq")
    public void pasteFromClipboard() {
        LoginPage login = new LoginPage().openFromMenu();
        driver.setClipboardText(LoginPage.USER);
        login.find("nameET").click();
        driver.pressKey(new KeyEvent(AndroidKey.PASTE));
        Assert.assertEquals(login.username(), LoginPage.USER);
        if (driver.isKeyboardShown()) {
            driver.hideKeyboard();
        }
    }
}
