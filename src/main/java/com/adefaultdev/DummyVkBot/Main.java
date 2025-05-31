package com.adefaultdev.DummyVkBot;

import com.adefaultdev.DummyVkBot.browser.BrowserLauncher;
import com.adefaultdev.DummyVkBot.dsConnection.DeepSeekClient;

public class Main {
    public static void main(String[] args) {

        System.out.println("Running");
        BrowserLauncher.browserSetup();
        DeepSeekClient.dsClientSetup();

        //add logic to create path to get ready messages(text and pictures)
    }
}