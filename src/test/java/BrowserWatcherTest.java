import com.adefaultdev.DummyVkBot.browser.BrowserWatcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.WebDriver;

import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BrowserWatcherTest {

    @Mock
    private WebDriver driver;

    @Mock
    private BrowserWatcher.ExitHandler exitHandler;

    @Test
    void shouldCallExit0WhenWindowHandlesEmpty() throws InterruptedException {

        when(driver.getWindowHandles()).thenReturn(Collections.emptySet());

        BrowserWatcher watcher = new BrowserWatcher(driver, exitHandler);
        Thread testThread = new Thread(watcher);
        testThread.start();

        Thread.sleep(3500);

        verify(exitHandler, atLeastOnce()).exit(0);
        testThread.interrupt();

    }

    @Test
    void shouldCallExit1OnException() throws InterruptedException {

        when(driver.getWindowHandles()).thenThrow(new RuntimeException("Simulated error"));

        BrowserWatcher watcher = new BrowserWatcher(driver, exitHandler);
        Thread testThread = new Thread(watcher);
        testThread.start();

        Thread.sleep(3500);

        verify(exitHandler, atLeastOnce()).exit(1);
        testThread.interrupt();

    }
}