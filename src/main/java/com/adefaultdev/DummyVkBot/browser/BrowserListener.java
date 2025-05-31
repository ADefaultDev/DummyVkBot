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

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.chat-list")));

    }

    private void checkForNewMessages() {

        List<WebElement> messages = driver.findElements(By.cssSelector(".im-mess--text")); //The css selector is dependent on messenger

        for (WebElement message : messages) {
            try {
                String text = message.getText();
                if (!seenMessages.contains(text)) {
                    seenMessages.add(text);
                    System.out.println("Новое сообщение: " + text); //Until there is no logic for DeepSeekClient, just print messages
                }
            } catch (StaleElementReferenceException ignored) {}
        }

    }

    public void stop() {

        if (driver != null) {
            driver.quit();
        }
    }
}
