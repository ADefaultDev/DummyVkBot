package com.adefaultdev.DummyVkBot.browser;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class BrowserLauncher {
    public static void browserSetup(){

        ChromeOptions options = new ChromeOptions();
        options.setBinary(BrowserConfig.getBrowserPath());

        WebDriverManager.chromedriver()
                .driverVersion(BrowserConfig.getDriverVersion())
                .setup();

        WebDriver driver = new ChromeDriver();
        driver.get("https://google.com");
    }
}