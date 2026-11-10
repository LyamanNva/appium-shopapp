package com.mydemo.pages;

import com.mydemo.utils.Waits;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

/** Login ekranı. Test istifadəçisi tətbiqin öz ekranında göstərilir. */
public class LoginPage extends BasePage {

    public static final String USER = "bod@example.com";
    public static final String LOCKED_USER = "alice@example.com";
    public static final String PASSWORD = "10203040";

    public static final By LOGIN_MENU_ITEM =
            AppiumBy.xpath("//*[@content-desc='Login Menu Item' or @text='Log In']");
    public static final By LOGOUT_MENU_ITEM =
            AppiumBy.xpath("//*[@content-desc='Logout Menu Item' or @text='Log Out']");

    public LoginPage openFromMenu() {
        openMenu();
        Waits.clickable(LOGIN_MENU_ITEM).click();
        Waits.visible(id("loginBtn"));
        return this;
    }

    public LoginPage login(String username, String password) {
        Waits.visible(id("nameET"));
        if (!username.isEmpty()) {
            fill("nameET", username);
        }
        if (!password.isEmpty()) {
            fill("passwordET", password);
        }
        find("loginBtn").click();
        return this;
    }

    public String username() {
        return text("nameET");
    }
}
