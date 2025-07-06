package com.adefaultdev.DummyVkBot.browser;

import org.openqa.selenium.WebDriver;

/**
 * Setups watcher for browser, making program stop if browser stopped manually
 */
@SuppressWarnings("BusyWait")
public class BrowserWatcher implements Runnable {

    private final WebDriver driver;
    private final ExitHandler exitHandler;
    private volatile boolean isRunning = true;

    public BrowserWatcher(WebDriver driver) {
        this(driver, System::exit);
    }

    public BrowserWatcher(WebDriver driver, ExitHandler exitHandler) {

        this.driver = driver;
        this.exitHandler = exitHandler;

    }

    @Override
    public void run() {

        try {
            while (isRunning) {
                if (driver.getWindowHandles().isEmpty()) {
                    exitHandler.exit(0);
                    break;
                }
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            exitHandler.exit(1);
        }

    }

    @FunctionalInterface
    public interface ExitHandler {
        void exit(int status);
    }

    public void stop(){
        isRunning = false;
    }
}