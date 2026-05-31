package stepDefinition;

import io.cucumber.java.en.*;
import pages.DirectoryPage;
import utility.BrowserDriver;
import utility.FakeDataUtil;

public class DirectoryPageSteps {

    private final DirectoryPage directoryPage = new DirectoryPage(BrowserDriver.getDriver());

    @And("User clicks directory menu")
    public void userClicksDirectoryMenu() {
        directoryPage.navigateToDirectory();
    }

    @Then("Search via name")
    public void searchViaName() {
        directoryPage.searchByName(FakeDataUtil.getFirstName());
    }
}
