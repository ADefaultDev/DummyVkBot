package com.adefaultdev.DummyVkBot.browser;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class BrowserLauncher {

    public static void browserSetup(){   //Creates a connection to user's browser to get data from social network

        ChromeOptions options = new ChromeOptions();
        options.setBinary(BrowserConfig.getBrowserPath());

        WebDriverManager.chromedriver()
                .driverVersion(BrowserConfig.getDriverVersion())
                .setup();

        WebDriver driver = new ChromeDriver();
        driver.get("https://google.com");

        BrowserListener listener = new BrowserListener();
        listener.start("https://vk.com/");

    }
}