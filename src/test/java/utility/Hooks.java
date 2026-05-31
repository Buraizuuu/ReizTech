package utility;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Hooks {

    @Before
    public void setUp() {
        BrowserDriver.setupDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed() && BrowserDriver.getDriver() != null) {
            byte[] screenshot = ((TakesScreenshot) BrowserDriver.getDriver())
                    .getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName());

            try {
                String ts = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("MMddyyyy_HHmmss"));
                String safeName = scenario.getName().replaceAll("[^a-zA-Z0-9]", "_");
                Path dir = Path.of("target/screenshots");
                Files.createDirectories(dir);
                Files.write(dir.resolve(ts + "_" + safeName + ".png"), screenshot);
                System.out.println("Screenshot saved: " + ts + "_" + safeName + ".png");
            } catch (IOException e) {
                System.err.println("Failed to save screenshot: " + e.getMessage());
            }
        }
        BrowserDriver.closeDriver();
    }
}
