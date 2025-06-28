package com.adefaultdev.DummyVkBot.browser;

import org.openqa.selenium.WebDriver;

/**
 * Use WebDriver to switch tabs
 */
public class BrowserSwitcher {

    private final WebDriver driver;

    public BrowserSwitcher(WebDriver driver){
        this.driver = driver;
    }

    public void switchTabs(){

        String currentWindow = driver.getWindowHandle();

        for(String window : driver.getWindowHandles()){
            if(!window.equals(currentWindow)){
                driver.switchTo().window(window);
            }
        }

    }

}
