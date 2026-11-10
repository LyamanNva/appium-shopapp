package com.mydemo.listeners;

import com.mydemo.driver.DriverManager;
import com.mydemo.utils.Screenshots;
import org.testng.ITestListener;
import org.testng.ITestResult;

/** Test uğursuz olduqda avtomatik ekran şəkli çəkir, nəticələri konsola yazır. */
public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("\n▶ BAŞLADI : " + name(result));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("✔ KEÇDİ   : " + name(result));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String reason = result.getThrowable() == null ? "" : " (" + result.getThrowable().getMessage() + ")";
        System.out.println("⏭ ÖTÜRÜLDÜ: " + name(result) + reason);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("✘ UĞURSUZ : " + name(result));
        if (DriverManager.isStarted()) {
            try {
                System.out.println("  Ekran şəkli: " + Screenshots.screen("FAIL_" + name(result)));
            } catch (RuntimeException e) {
                System.out.println("  Ekran şəkli çəkilmədi: " + e.getMessage());
            }
        }
    }

    private static String name(ITestResult result) {
        return result.getTestClass().getRealClass().getSimpleName() + "." + result.getMethod().getMethodName();
    }
}
