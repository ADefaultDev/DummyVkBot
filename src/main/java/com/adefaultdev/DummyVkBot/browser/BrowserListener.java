package com.adefaultdev.DummyVkBot.browser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("BusyWait")
public class BrowserListener {

    private WebDriver driver;
    private WebDriverWait wait;
    private final Map<String, Long> seenMessages = new ConcurrentHashMap<>();
    private volatile boolean shouldContinue = true;
    private static final long TTL_MILLIS = 10 * 60 * 1000;

    public void start(String messengerUrl) {

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        driver.get(messengerUrl);
        System.out.println("Opened URL: " + messengerUrl);

        if (!waitForUserLogin()) {
            System.out.println("Login timeout reached. Stopping...");
            stop();
            return;
        }

        while (shouldContinue) {

            try {
                checkForNewMessages();
                Thread.sleep(4000); // Intentionally using polling every 4s; VK UI has no event system
            } catch (InterruptedException e) {
                System.out.println("Bot interrupted");
                break;
            } catch (Exception e) {
                System.out.println("[ERROR] " + e.getMessage());
                stop();
                break;
            }
        }

    }

    private boolean waitForUserLogin() {

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("spa_root")));
            System.out.println("User successfully logged in");
            return true;
        } catch (TimeoutException e) {
            System.out.println("[TIMEOUT] Login timeout after 30 seconds");
            return false;
        }

    }

    private void checkForNewMessages() {

        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("span.MessageText")));
            List<WebElement> messages = driver.findElements(By.cssSelector("span.MessageText"));

            cleanupOldMessages();

            if (!messages.isEmpty()) {

                String text = messages.get(messages.size() - 1).getText();

                if (!text.isEmpty() && !seenMessages.containsKey(text)) {
                    seenMessages.put(text, System.currentTimeMillis());
                    System.out.println("New message: " + text);
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("[WARNING] No messages found");
        }

    }

    private void cleanupOldMessages() {

        long now = System.currentTimeMillis();
        seenMessages.entrySet().removeIf(entry -> now - entry.getValue() > TTL_MILLIS);

    }

    public void stop() {

        System.out.println("Stopping browser...");
        shouldContinue = false;

        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.out.println("[ERROR] Failed to quit browser: " + e.getMessage());
            } finally {
                driver = null;
            }
        }

    }

}