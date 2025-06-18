package com.adefaultdev.DummyVkBot.browser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class MessageSender {

    private final WebDriverWait wait;

    public MessageSender(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void sendMessage(String message) {
        try {

            WebElement inputField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("span[role='textbox']")
            ));

            inputField.click();
            inputField.clear();
            inputField.sendKeys(message);

            wait.until(d -> !inputField.getText().isEmpty());

            WebElement sendButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button[aria-label='Отправить сообщение']")
            ));
            sendButton.click();

            System.out.println("Message sent: " + message);

        } catch (Exception e) {
            System.out.println("[ERROR] Failed to send message: " + e.getMessage());
        }
    }
}