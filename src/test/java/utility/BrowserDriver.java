package utility;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class BrowserDriver {

    public static WebDriver driver;

    private static final String BROWSER   = System.getProperty("browser",
            System.getenv().getOrDefault("BROWSER", "chrome"));
    private static final boolean HEADLESS = Boolean.parseBoolean(
            System.getProperty("headless", System.getenv().getOrDefault("HEADLESS", "false")));

    public static void setupDriver() {
        if (driver == null) {
            switch (BROWSER.toLowerCase()) {
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver();
                    break;
                case "edge":
                    WebDriverManager.edgedriver().setup();
                    driver = new EdgeDriver();
                    break;
                case "chrome":
                default:
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions options = new ChromeOptions();
                    if (HEADLESS) {
                        options.addArguments("--headless=new", "--disable-gpu",
                                "--window-size=1920,1080",
                                "user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
                    }
                    driver = new ChromeDriver(options);
                    if (!HEADLESS) driver.manage().window().maximize();
                    break;
            }
            driver.manage().deleteAllCookies();
        }
    }

    public static void openUrl(String url) {
        if (driver == null) setupDriver();
        driver.get(url);
    }

    public static void closeDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    public static WebDriver getDriver() {
        return driver;
    }
}
