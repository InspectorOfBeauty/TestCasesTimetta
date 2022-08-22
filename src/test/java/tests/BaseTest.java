package tests;

import com.tngtech.java.junit.dataprovider.DataProvider;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import utils.Browser;
import utils.DriverFactory;

public abstract class BaseTest {
    private static WebDriver driver;
    public static WebDriver getDriver() {
        return driver;
    }

    @BeforeClass
    public static void setUp() {
        driver = DriverFactory.getDriver(Browser.CHROME);
        driver.manage().window().maximize();
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }

    @DataProvider
    public static Object[][] correctEmail() {
        return new Object[][]{
                {"TEST@TEST.TEST"}, // Тест-кейс 1.
                {"test@test.test"}, // Тест-кейс 2.
                {"test1234567890@test.test"}, // Тест-кейс 3.
                {"test@test12345.test67890"}, // Тест-кейс 4.
                {"test-test@test.test"}, // Тест-кейс 5.
                {"test_test@test.test"}, // Тест-кейс 6.
                {"test.test@test.test"}, // Тест-кейс 7.
                {"test@te.test"}, // Тест-кейс 8.
                {"test@testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttes.test"}, // Тест-кейс 9.
                {"test@test.te"}, // Тест-кейс 10.
                {"test@test.testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttes"} // Тест-кейс 11.
        };
    }

    @DataProvider
    public static Object[][] incorrectEmail() {
        return new Object[][]{
                {""}, // Тест-кейс 1.
                {"         "}, // Тест-кейс 2.
                {"test@test-.-test"}, // Тест-кейс 3.
                {"test@test_._test"}, // Тест-кейс 4.
                {"test@test..test"}, // Тест-кейс 5.
                {"test@testtest"}, // Тест-кейс 6.
                {"testtest.test"}, // Тест-кейс 7.
                {"test test@test.test"}, // Тест-кейс 8.
                {"test@test test"}, // Тест-кейс 9.
                {"@test.test"}, // Тест-кейс 10.
                {"test@"}, // Тест-кейс 11.
                {"test@t.test"}, // Тест-кейс 12.
                {"test@testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttest.test"}, // Тест-кейс 13.
                {"test@test.t"}, // Тест-кейс 14.
                {"test@test.testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttest"} // Тест-кейс 15.
        };
    }
}
