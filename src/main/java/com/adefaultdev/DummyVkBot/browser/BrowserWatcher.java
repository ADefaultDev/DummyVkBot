package com.adefaultdev.DummyVkBot.browser;

import org.openqa.selenium.WebDriver;

/**
 * Setups watcher for browser, making program stop if browser stopped manually
 */
@SuppressWarnings("BusyWait")
public class BrowserWatcher implements Runnable {
    private final WebDriver driver;

    public BrowserWatcher(WebDriver driver) {

        this.driver = driver;

    }

    @Override
    public void run() {

        try {
            while (true) {
                if (driver.getWindowHandles().isEmpty()) {
                    System.exit(0);
                }
                Thread.sleep(1000); // Had to check in thread due to lack of API
            }
        } catch (Exception e) {
            System.exit(1);
        }
    }

}