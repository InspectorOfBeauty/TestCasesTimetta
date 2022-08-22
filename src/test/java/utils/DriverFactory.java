package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.logging.Level;

public class DriverFactory {
    private final static String chromeDriverPath = "./webDrivers/winChromedriver.exe";

    public static WebDriver getDriver(Browser browser) {
        switch (browser) {
            case CHROME:
                DesiredCapabilities capabilitiesForChrom = DesiredCapabilities.chrome();
                LoggingPreferences logPrefs = new LoggingPreferences();
                logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
                capabilitiesForChrom.setCapability("goog:loggingPrefs", logPrefs);

                System.setProperty("webdriver.chrome.driver", chromeDriverPath);
                return new ChromeDriver(capabilitiesForChrom);
            default:
                throw new RuntimeException("Unsupported browser " + browser);
        }
    }
}
