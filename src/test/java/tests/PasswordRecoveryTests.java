package tests;

import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import pages.PasswordRecoveryPage;
import utils.PropertyReader;

@RunWith(DataProviderRunner.class)
public class PasswordRecoveryTests extends BaseTest {
    @Test
    @UseDataProvider("correctEmail")
    public void positiveTest(String email) {
        PasswordRecoveryPage passwordRecoveryPage = new PasswordRecoveryPage();
        passwordRecoveryPage.open();
        passwordRecoveryPage.assertThatPasswordRecoveryPageIsOpened(PropertyReader.getPasswordRecoveryPageAddress());

        passwordRecoveryPage.fillEmailField(email);
        passwordRecoveryPage.pressPasswordRecoveryButton();
        passwordRecoveryPage.assertThatNotificationsAreDisplayed(PropertyReader.getPasswordRecoveryNotification1(),
                PropertyReader.getPasswordRecoveryNotification2());

        passwordRecoveryPage.assertThatEmailFieldIsNotDisplayed();
        passwordRecoveryPage.assertThatPasswordRecoveryButtonIsNotDisplayed();
        passwordRecoveryPage.assertThatLoginPageLinkChangedStyle();
        passwordRecoveryPage.assertThatRecoveryRequestWorked(email);
    }

    @Test
    @UseDataProvider("incorrectEmail")
    public void negativeTest(String email) throws Exception {
        PasswordRecoveryPage passwordRecoveryPage = new PasswordRecoveryPage();
        passwordRecoveryPage.open();
        passwordRecoveryPage.assertThatPasswordRecoveryPageIsOpened(PropertyReader.getPasswordRecoveryPageAddress());

        passwordRecoveryPage.fillEmailField(email);
        passwordRecoveryPage.pressPasswordRecoveryButton();

        Thread.sleep(500);
        passwordRecoveryPage.assertThatWarningIsDisplayedInEmailField();
    }
}

