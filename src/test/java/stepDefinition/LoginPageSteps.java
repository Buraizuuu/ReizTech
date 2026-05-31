package stepDefinition;

import io.cucumber.java.en.*;
import pages.LoginPage;
import utility.BrowserDriver;
import utility.ConfigReader;
import static org.junit.Assert.assertTrue;

public class LoginPageSteps {

    private final LoginPage loginPage = new LoginPage(BrowserDriver.getDriver());

    @Given("User navigates to login page")
    public void userNavigatesToLoginPage() {
        loginPage.navigate(ConfigReader.get("BASE_URL"));
    }

    @When("User populates username and password")
    public void userPopulatesUsernameAndPassword() {
        loginPage.enterUsername(ConfigReader.get("USERNAME"));
        loginPage.enterPassword(ConfigReader.get("PASSWORD"));
    }

    @And("Clicks login button")
    public void clicksLoginButton() {
        loginPage.clickLogin();
    }

    @Then("Verify if user successfully logged-in")
    public void verifyIfUserSuccessfullyLoggedIn() {
        assertTrue("Dashboard not visible after login", loginPage.isDashboardVisible());
    }
}
