package com.adefaultdev.DummyVkBot;

import com.adefaultdev.DummyVkBot.browser.BrowserLauncher;
import com.adefaultdev.DummyVkBot.browser.BrowserListener;
import com.adefaultdev.DummyVkBot.dsConnection.DeepSeekClient;
import org.openqa.selenium.WebDriver;

public class Main {

    public static void main(String[] args) {

        WebDriver driver = BrowserLauncher.launchBrowser();

        DeepSeekClient dsClient = new DeepSeekClient(driver);
        dsClient.setup("https://www.deepseek.com/en");

        BrowserListener listener = new BrowserListener();
        listener.start("https://vk.com/", driver, dsClient);

        //add logic to create path to get ready messages(text and pictures)

    }
}