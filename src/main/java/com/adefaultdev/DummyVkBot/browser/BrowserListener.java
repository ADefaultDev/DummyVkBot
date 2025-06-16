package com.adefaultdev.DummyVkBot.browser;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BrowserListener {

    private WebDriver driver;
    private WebDriverWait wait;
    private final Set<String> seenMessages = new HashSet<>();

    public void start(String messengerUrl) {

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        driver.get(messengerUrl);

        waitForUserLogin();

        while (true) {
            try {
                checkForNewMessages();
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                break;
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private void waitForUserLogin() {
        try {

            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("spa_root")));
        } catch (TimeoutException e){
            System.err.println("30 sec timeout");
            driver.quit(); //add additional conditions and alternative handling
        }
    }

    private void checkForNewMessages() {
        try {

            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("span.MessageText")));

            List<WebElement> messages = driver.findElements(By.cssSelector("span.MessageText"));

            if (!messages.isEmpty()) {

                WebElement lastMessage = messages.get(messages.size() - 1); // getting only the last message
                String text = lastMessage.getText();

                if (!text.isEmpty() && !seenMessages.contains(text)) {
                    seenMessages.add(text);
                    System.out.println("Last message: " + text);
                }
            }
        } catch (NoSuchElementException e) {
            System.err.println("No messages: " + e.getMessage());
        }

    }

    public void stop() {

        if (driver != null) {
            driver.quit();
        }
    }

}
