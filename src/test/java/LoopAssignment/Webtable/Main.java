package LoopAssignment.Webtable;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Main {
    public static void main(String args[]) {
        WebDriver driver = new ChromeDriver();
        WebDriverManager.chromedriver().setup();

        driver.get("https://app.tryloop.ai/login/password");

        driver.manage().window().maximize();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

        // Login
        WebElement username = driver.findElement(By.id(":r1:"));
        username.sendKeys("qa-engineer-assignment@test.com");

        WebElement password = driver.findElement(By.id(":r2:"));
        password.sendKeys("QApassword123$");

        WebElement logbtn = driver.findElement(By.xpath("//button[normalize-space()='Login']"));
        logbtn.click();

        WebElement skip = driver.findElement(By.xpath("//button[normalize-space()='Skip for now']"));
        skip.click();

        WebElement chargebacks = driver.findElement(By.xpath("//*[@id=\"main_start_app\"]/div/div/ul/div/div[4]/ul/li/div/div[2]"));
        chargebacks.click();

        WebElement historybystore = driver.findElement(By.xpath("//*[@id=\"main_start_app\"]/div/div/ul/div/div[4]/ul/div/div/div/div/a[3]/div/span"));
        historybystore.click();

        WebElement viewTable = driver.findElement(By.xpath("//div[@id='view-table-id']"));

        System.out.println("Is the table present? " + viewTable.isDisplayed());
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@id=\"main_start_app\"]/main/div/div/div[4]/div/div[1]"), 0));

        List<WebElement> tableRows = driver.findElements(By.xpath("//tbody/tr"));

        System.out.println("Number of rows found: " + tableRows.size());

        double[] sumOfColumns = new double[7];

        for (int rowIndex = 0; rowIndex < tableRows.size() - 1; rowIndex++) {
            WebElement row = tableRows.get(rowIndex);
            List<WebElement> cells = row.findElements(By.xpath("td/h6[1]"));

            for (int columnIndex = 0; columnIndex < 7; columnIndex++) {
                String cellValue = cells.get(columnIndex).getText().trim().replaceAll("[^\\d.]+", "");

                if (!cellValue.isEmpty()) {
                    double cellDoubleValue = Double.parseDouble(cellValue);
                    sumOfColumns[columnIndex] += cellDoubleValue;
                }
            }
        }
        for (int i = 0; i < 7; i++) {
            System.out.println("Sum of Column " + (i + 1) + ": " + sumOfColumns[i]);
        }
        double[] expectedSums = {0.0, 0.0, 15563.04, 17658.08, 28508.62, 28288.51, 2441.69};

        for (int i = 0; i < 7; i++) {
            assert sumOfColumns[i] == expectedSums[i] : "Sum of Column " + (i + 1) + " does not match the expected value.";
        }
        driver.quit();
    }
}
