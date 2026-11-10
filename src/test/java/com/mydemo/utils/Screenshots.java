package com.mydemo.utils;

import com.mydemo.driver.DriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Ekran və element şəkillərini target/screenshots qovluğuna yazır. */
public final class Screenshots {

    private static final Path DIR = Paths.get("target", "screenshots");
    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private Screenshots() {
    }

    public static Path screen(String name) {
        return write(name, DriverManager.get().getScreenshotAs(OutputType.BYTES));
    }

    public static Path element(WebElement element, String name) {
        return write(name, element.getScreenshotAs(OutputType.BYTES));
    }

    private static Path write(String name, byte[] png) {
        try {
            Files.createDirectories(DIR);
            Path file = DIR.resolve(name.replaceAll("[^a-zA-Z0-9_-]", "_") + "_" + LocalDateTime.now().format(TS) + ".png");
            Files.write(file, png);
            return file;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
