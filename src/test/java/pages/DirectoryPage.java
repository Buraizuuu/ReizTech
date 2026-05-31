package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utility.BasePage;

public class DirectoryPage extends BasePage {

    private static final By btnDirectory = By.xpath("//span[text()='Directory']");
    private static final By txtName      = By.xpath("//input[@placeholder='Type for hints...']");

    public DirectoryPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToDirectory() {
        click(btnDirectory);
    }

    public void searchByName(String name) {
        type(txtName, name);
    }
}
