package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utility.BasePage;

public class LoginPage extends BasePage {

    private static final By txtUsername = By.xpath("//input[@name='username']");
    private static final By txtPassword = By.xpath("(//input[@name='password'])[1]");
    private static final By btnLogin    = By.xpath("(//button[@type='submit'])[1]");
    private static final By lblDashboard = By.xpath("//h6[text()='Dashboard']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void navigate(String url) {
        driver.get(url);
    }

    public void enterUsername(String username) {
        type(txtUsername, username);
    }

    public void enterPassword(String password) {
        type(txtPassword, password);
    }

    public void clickLogin() {
        click(btnLogin);
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public boolean isDashboardVisible() {
        return isVisible(lblDashboard);
    }
}
