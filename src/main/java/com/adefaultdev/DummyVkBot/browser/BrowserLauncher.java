package com.adefaultdev.DummyVkBot.browser;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;

public class BrowserLauncher {

    public static void launchBrowser() {

        ChromeOptions options = new ChromeOptions();
        options.setBinary(BrowserConfig.getBrowserPath());

        WebDriverManager.chromedriver()
                .driverVersion(BrowserConfig.getDriverVersion())
                .setup();

        BrowserListener listener = new BrowserListener();
        listener.start("https://vk.com/");
    }
}