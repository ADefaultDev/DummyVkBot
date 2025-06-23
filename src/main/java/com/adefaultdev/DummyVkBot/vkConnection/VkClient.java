package com.adefaultdev.DummyVkBot.vkConnection;

import com.adefaultdev.DummyVkBot.browser.MessageSender;
import com.adefaultdev.DummyVkBot.dsConnection.DeepSeekClient;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BrowserListener — listening new messages from VK with Selenium.
 * - Getting information about current user
 * - Checking out for new messages
 * - Able to ignore user's own messages
 * - Sends back a copy of last incoming message
 * - Used in DummyVkBot.
 */

@SuppressWarnings("BusyWait")
public class VkClient {

    private WebDriver driver;
    private WebDriverWait wait;
    private final Map<String, Long> seenMessages = new ConcurrentHashMap<>();
    private volatile boolean shouldContinue = true;
    private static final long TTL_MILLIS = 10 * 60 * 1000;
    private String myHref = null;
    private DeepSeekClient deepSeekClient;

    /**
     * Setups the listener in VK.
     * Requires user to log in manually.
     *
     * @param messengerUrl URL for VK site
     */
    public void start(String messengerUrl, WebDriver driver, DeepSeekClient deepSeekClient) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.deepSeekClient = deepSeekClient;

        driver.get(messengerUrl);

        myHref = determineCurrentUserHref();
        if (myHref == null) {
            System.out.println("Failed to detect current user. Stopping.");
            stop();
            return;
        }

        if (!waitForUserLogin()) {
            System.out.println("Login timeout reached. Stopping...");
            stop();
            return;
        }

        startMessageProcessing();
    }

    /**
     * Getting a reference for current user (href) to manage outgoing messages
     * @return href of the current user or null if href is not detected
     */
    private String determineCurrentUserHref() {
        try {
            WebElement profileLink = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector("a#top_profile_link"))
            );
            String href = profileLink.getAttribute("href");
            System.out.println("Detected myHref from header: " + href);
            return href;
        } catch (Exception e) {
            System.out.println("[ERROR] Cannot detect current user href: " + e.getMessage());
            return null;
        }
    }


    /**
     * Checking if the user logged in on VK site
     */
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

    private void startMessageProcessing() {
        MessageSender vkMessageSender = new MessageSender(driver);

        while (shouldContinue) {
            try {
                String newMessage = checkForNewMessages();
                if (newMessage != null) {
                    processAndRespondToMessage(newMessage, vkMessageSender);
                }
                Thread.sleep(4000);
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

    /**
     * Using DeepSeek client, creating a response for a new message
     * If the answer is not empty, sending generated response in VK
     */
    private void processAndRespondToMessage(String incomingMessage, MessageSender vkMessageSender) {

        try {
            String preMessage = "Составь небольшой ответ на данное сообщение и не используй эмодзи. Только текст: ";
            String generatedResponse = deepSeekClient.sendMessageAndGetResponse(preMessage + incomingMessage);

            if (generatedResponse != null && !generatedResponse.isEmpty()) {
                vkMessageSender.sendMessage(generatedResponse);
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to process message: " + e.getMessage());
        }

    }

    /**
     * -After entering any dialogue, checking if new messages are incoming
     * -Able to distinguish user's messages from other's, preventing duplicating
     * -New messages are stored in seenMessages, preventing from answering same messages few times
     * -The store for messages getting cleaned up every 10 minutes to avoid excessive memory usage
     */
    private String checkForNewMessages() {

        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div.ConvoMessage")));

            List<WebElement> messageBlocks = driver.findElements(By.cssSelector("div.ConvoMessage"));
            cleanupOldMessages();

            if (!messageBlocks.isEmpty()) {
                WebElement lastMessageBlock = messageBlocks.get(messageBlocks.size() - 1);

                String authorHref = "";
                try {
                    WebElement authorLink = lastMessageBlock.findElement(By.cssSelector("a.ConvoMessageHeader__authorLink"));
                    authorHref = authorLink.getAttribute("href");
                } catch (NoSuchElementException ignored) {}

                if (authorHref.equalsIgnoreCase(myHref)) {
                    return null;
                }

                List<WebElement> spans = lastMessageBlock.findElements(By.cssSelector("span.MessageText"));
                StringBuilder fullMessageBuilder = new StringBuilder();
                for (WebElement span : spans) {
                    fullMessageBuilder.append(span.getText()).append("\n");
                }

                String fullMessage = fullMessageBuilder.toString().trim().replaceAll("\\s+", " ");

                if (!fullMessage.isEmpty() && !seenMessages.containsKey(fullMessage)) {
                    seenMessages.put(fullMessage, System.currentTimeMillis());
                    System.out.println("New full message: " + fullMessage);
                    return fullMessage;
                }
            }

        } catch (NoSuchElementException e) {
            System.out.println("[WARNING] No messages found");
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }

        return null;
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