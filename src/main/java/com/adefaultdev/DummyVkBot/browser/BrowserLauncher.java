package com.adefaultdev.DummyVkBot.browser;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Setups Chrome browser
 * Using arguments in options to bypass cloudflare and similar protection
 */
public class BrowserLauncher {

    public static WebDriver launchBrowser() {

        ChromeOptions options = new ChromeOptions();

        options.setBinary(BrowserConfig.getBrowserPath());

        options.addArguments(
                "--disable-blink-features=AutomationControlled",
                "--start-maximized",
                "--no-sandbox",
                "--disable-dev-shm-usage"
        );

        WebDriverManager.chromedriver()
                .driverVersion(BrowserConfig.getDriverVersion())
                .setup();

        return new ChromeDriver(options);
    }
}