package com.adefaultdev.DummyVkBot;

import com.adefaultdev.DummyVkBot.browser.BrowserLauncher;
import com.adefaultdev.DummyVkBot.browser.BrowserWatcher;
import com.adefaultdev.DummyVkBot.browser.wrapper.WebDriverManagerWrapper;
import com.adefaultdev.DummyVkBot.dsConnection.DeepSeekClient;
import com.adefaultdev.DummyVkBot.vkConnection.VkClient;
import org.openqa.selenium.WebDriver;

public class Main {

    public static void main(String[] args) {

        BrowserLauncher browserLauncher = new BrowserLauncher(new WebDriverManagerWrapper());
        WebDriver driver = browserLauncher.launchBrowser();

        DeepSeekClient dsClient = new DeepSeekClient(driver);
        dsClient.setup("https://www.deepseek.com/en");

        Thread watcherThread = new Thread(new BrowserWatcher(driver));
        watcherThread.setDaemon(true);
        watcherThread.start();

        VkClient vkClient = new VkClient();
        vkClient.start("https://vk.com/", driver, dsClient);

        //add logic to create path to get ready messages(text and pictures)

    }
}