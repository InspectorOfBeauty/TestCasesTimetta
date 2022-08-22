package pages;

import com.fasterxml.jackson.databind.JsonNode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.FindBy;
import tests.BaseTest;
import utils.JsonUtils;
import utils.PropertyReader;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PasswordRecoveryPage extends BasePage {
    @FindBy(xpath = "//div[@class = 'card-body']/form/div/input")
    private WebElement emailField;

    @FindBy(xpath = "//div[@class = 'card-body']/form/div/div/button")
    private WebElement passwordRecoveryButton;

    @FindBy(xpath = "//div[@class = 'card-body']/form/a")
    private WebElement loginPagelink;

    @FindBy(xpath = "//div[@class = 'card-body']/form/p")
    private List<WebElement> notifications;

    public void fillEmailField(String email) {
        emailField.click();
        emailField.sendKeys(email);
    }

    public void open() {
        BaseTest.getDriver().get(PropertyReader.getPasswordRecoveryPageAddress());
    }

    public void assertThatPasswordRecoveryPageIsOpened(String address) {
        assertThat(BaseTest.getDriver().getCurrentUrl()).as("Incorrect page is opened!").isEqualTo(address);
    }
    public void pressPasswordRecoveryButton() {
        passwordRecoveryButton.click();
    }

    public void assertThatNotificationsAreDisplayed(String notice1, String notice2) {
        assertThat(notifications.get(0).getText()).as("Notification 1 is not displayed").isEqualTo(notice1);
        assertThat(notifications.get(1).getText()).as("Notification 2 is not displayed").isEqualTo(notice2);
    }

    public void assertThatEmailFieldIsNotDisplayed() {
        emailField = BaseTest.getDriver().findElement(By.xpath(("//div[@class = 'card-body']/form/input[@value = 'InputEmail']")));
        assertThat(emailField.getAttribute("type")).as("Email field is displayed!").isEqualTo("hidden");
    }

    public void assertThatPasswordRecoveryButtonIsNotDisplayed() {
        assertThat(BaseTest.getDriver().findElements(By.xpath("//div[@class = 'card-body']/form/div/div/button")).isEmpty())
                .as("Password recovery button is displayed!").isTrue();
    }

    public void assertThatLoginPageLinkChangedStyle() {
        loginPagelink = BaseTest.getDriver().findElement(By.xpath(("//div[@class = 'card-body']/form/div/div/a")));
        assertThat(loginPagelink.getAttribute("class")).as("Login page link did't change its style!").isEqualTo("btn btn-primary");
    }

    public void assertThatRecoveryRequestWorked(String email) {
        String requestId = null;
        String status = null;

        List<LogEntry> entries = BaseTest.getDriver().manage().logs().get(LogType.PERFORMANCE).getAll();
        int pos = 0;

        for (; pos < entries.size(); pos++) {
            JsonNode root = JsonUtils.parseJson(entries.get(pos).getMessage()); //Message - вся запись в логе.
            JsonNode message = root.get("message"); //Конкретная часть лога: содержимое лога.
            if (message == null) continue;
            String logMethod = JsonUtils.getStringValue(message, "method");
            if (logMethod == null) continue;
            String requestMethod = JsonUtils.getStringValue(message, "params", "request", "method");
            if (requestMethod == null) continue;
            String url = JsonUtils.getStringValue(message, "params", "request", "url");
            if (url == null) continue;

            if (logMethod.equals("Network.requestWillBeSent")
                    && requestMethod.equals("POST")
                    && url.equals("https://auth.timetta.com/password-recovery")) {
                String postData = JsonUtils.getStringValue(message, "params", "request", "postData");
                assertThat(postData).as("Email is not correct!").contains("Email=" + email.replace("@", "%40"));
                requestId = JsonUtils.getStringValue(message, "params", "requestId");
                break;
            }
        }
        assertThat(requestId).as("Request wasn't sent!").isNotNull();

        for (; pos < entries.size(); pos++) {
            JsonNode root = JsonUtils.parseJson(entries.get(pos).getMessage());
            JsonNode message = root.get("message");
            if (message == null) continue;
            String logMethod = JsonUtils.getStringValue(message, "method");
            if (logMethod == null) continue;
            String url = JsonUtils.getStringValue(message, "params", "response", "url");
            if (url == null) continue;
            String request_Id = JsonUtils.getStringValue(message, "params", "requestId");
            if (request_Id == null) continue;

            if (logMethod.equals("Network.responseReceived")
                    && url.equals("https://auth.timetta.com/password-recovery")
                    && request_Id.equals(requestId)) {
                String code = JsonUtils.getStringValue(message, "params", "response", "status");
                assertThat(code).as("Status is not 200!").isEqualTo("200");
                status = code;
                break;
            }
        }
        assertThat(status).as("Status of response is not 200!").isNotNull();
    }

    public void assertThatWarningIsDisplayedInEmailField() {
        assertThat(BaseTest.getDriver().findElement(By.xpath("//div[@class = 'card-body']/form/div/input"))
                .getCssValue("background-image"))
                .as("Warning is not displayed")
                .isEqualTo("url(\"data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' " +
                        "width='12' height='12' fill='none' stroke='%23dc3545' viewBox='0 0 12 12'%3e%3ccircle " +
                        "cx='6' cy='6' r='4.5'/%3e%3cpath stroke-linejoin='round' d='M5.8 3.6h.4L6 6.5z'/%3e%3ccircle " +
                        "cx='6' cy='8.2' r='.6' fill='%23dc3545' stroke='none'/%3e%3c/svg%3e\")");

        assertThat(BaseTest.getDriver().findElement(By.xpath("//div[@class = 'card-body']/form/div/input"))
                .getCssValue("border-color"))
                .as("Border is not red!").isEqualTo("rgb(220, 53, 69)");
    }
}
