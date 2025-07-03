package com.adefaultdev.DummyVkBot.browser;

import com.adefaultdev.DummyVkBot.browser.wrapper.WebDriverManagerWrapper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Setups Chrome browser
 * Using arguments in options to bypass cloudflare and similar protection
 */
public class BrowserLauncher {


    private final WebDriverManagerWrapper driverManagerWrapper;

    public BrowserLauncher(WebDriverManagerWrapper driverManagerWrapper) {
        this.driverManagerWrapper = driverManagerWrapper;
    }

    public WebDriver launchBrowser() {
        ChromeOptions options = new ChromeOptions();

        options.setBinary(BrowserConfig.getBrowserPath());

        options.addArguments(
                "--disable-blink-features=AutomationControlled",
                "--start-maximized",
                "--no-sandbox",
                "--disable-dev-shm-usage"
        );

        driverManagerWrapper.setup(BrowserConfig.getDriverVersion());

        return new ChromeDriver(options);
    }
}
