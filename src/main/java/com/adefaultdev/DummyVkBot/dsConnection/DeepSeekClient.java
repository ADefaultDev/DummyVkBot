package com.adefaultdev.DummyVkBot.dsConnection;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

/**
 * Getting message from VK
 * Generates answer with DeepSeek and sends it back
 */
public class DeepSeekClient {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private String deepSeekUrl;
    private String lastResponse;

    public DeepSeekClient(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    /**
     * Creating DeepSeek client
     * @param url - URL of DeepSeek site
     */
    public void setup(String url) {
        this.deepSeekUrl = url;
        System.out.println("DeepSeek client is up. URL: " + url);

        try {
            driver.get(url);
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("div.chat-container, div.conversation-wrapper")
            ));
            System.out.println("DeepSeek site loaded");
        } catch (Exception e) {
            System.out.println("[Error] During Deep Seek client setup " + e.getMessage());
        }
    }

    /**
     * Sends message to DeepSeek
     * @param message - text of the message to send
     */
    public void sendMessage(String message) {
        if (deepSeekUrl == null || deepSeekUrl.isEmpty()) {
            System.out.println("[ОШИБКА] URL DeepSeek is not available. Call setup()");
            return;
        }

        try {

            String originalHandle = driver.getWindowHandle();
            if (driver.getWindowHandles().size() == 1) {
                ((JavascriptExecutor)driver).executeScript("window.open('" + deepSeekUrl + "', '_blank');");
                wait.until(ExpectedConditions.numberOfWindowsToBe(2));
            }

            for (String handle : driver.getWindowHandles()) {
                if (!handle.equals(originalHandle)) {
                    driver.switchTo().window(handle);
                    break;
                }
            }

            waitForChatReady();

            WebElement chatInput = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector("textarea.chat-input, textarea.message-input")
                    )
            );

            chatInput.click();
            chatInput.clear();
            chatInput.sendKeys(message);

            wait.until(d -> !chatInput.getAttribute("value").isEmpty());

            WebElement sendButton = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.cssSelector("button.send-button, button[aria-label='Send']")
                    )
            );
            sendButton.click();

            Thread.sleep(1000);

            driver.close();
            driver.switchTo().window(originalHandle);

            waitForResponse();
        } catch (Exception e) {
            System.out.println("[Error] DeepSeek: " + e.getMessage());
            cleanupTabs();
        }
    }

    private void waitForChatReady() {

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("div.chat-container, div.conversation-wrapper")
            ));

            try {
                WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("button.modal-close, button[aria-label='Close']")
                ));
                closeBtn.click();
            } catch (Exception ignored) {}

        } catch (TimeoutException e) {
            throw new RuntimeException("Time limit exceeded for DeepSeek");
        }

    }

    private void cleanupTabs() {
        try {
            String mainWindow = driver.getWindowHandles().iterator().next();
            for (String handle : driver.getWindowHandles()) {
                if (!handle.equals(mainWindow)) {
                    driver.switchTo().window(handle).close();
                }
            }
            driver.switchTo().window(mainWindow);
        } catch (Exception e) {
            System.out.println("[Error] tabs cleanup  " + e.getMessage());
        }
    }

    private void waitForResponse() {
        try {
            WebElement response = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector("div.response-content, div.assistant-message")
                    )
            );
            this.lastResponse = response.getText();
        } catch (TimeoutException e) {
            System.out.println("[TIMEOUT] Response not received");
        }
    }

    public String getLastResponse() {
        return this.lastResponse;
    }
}