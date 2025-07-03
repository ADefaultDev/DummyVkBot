package com.adefaultdev.DummyVkBot.browser.wrapper;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebDriverManagerWrapper {

    public void setup(String version) {
        WebDriverManager.chromedriver().driverVersion(version).setup();
    }

}
