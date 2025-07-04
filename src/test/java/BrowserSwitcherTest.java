import com.adefaultdev.DummyVkBot.browser.BrowserSwitcher;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;

import java.util.Set;

import static org.mockito.Mockito.*;

class BrowserSwitcherTest {

    @Test
    void testSwitchTabs_SwitchesToAnotherTab() {

        WebDriver mockDriver = mock(WebDriver.class);
        TargetLocator mockTargetLocator = mock(TargetLocator.class);

        when(mockDriver.getWindowHandle()).thenReturn("window1");
        when(mockDriver.getWindowHandles()).thenReturn(Set.of("window1", "window2"));
        when(mockDriver.switchTo()).thenReturn(mockTargetLocator);

        BrowserSwitcher switcher = new BrowserSwitcher(mockDriver);

        switcher.switchTabs();

        verify(mockDriver).getWindowHandle();
        verify(mockDriver).getWindowHandles();
        verify(mockDriver).switchTo();
        verify(mockTargetLocator).window("window2");
        verifyNoMoreInteractions(mockDriver, mockTargetLocator);

    }

    @Test
    void testSwitchTabs_NoOtherTabs_NoSwitch() {

        WebDriver mockDriver = mock(WebDriver.class);

        when(mockDriver.getWindowHandle()).thenReturn("onlyWindow");
        when(mockDriver.getWindowHandles()).thenReturn(Set.of("onlyWindow"));

        BrowserSwitcher switcher = new BrowserSwitcher(mockDriver);

        switcher.switchTabs();

        verify(mockDriver).getWindowHandle();
        verify(mockDriver).getWindowHandles();
        verify(mockDriver, never()).switchTo();

    }
}
