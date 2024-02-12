package LoopAssignment.Webtable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class WebTableToCSV {
	public static void main(String[] args) {
	    WebDriver driver = new ChromeDriver();
	    WebDriverManager.chromedriver().setup();

	    login(driver);
	    WebElement chargebacks = driver.findElement(By.xpath("//*[@id=\"main_start_app\"]/div/div/ul/div/div[4]/ul/li/div/div[2]"));
	    chargebacks.click();

	    WebElement transactionsLink = driver.findElement(By.xpath("//span[contains(text(), 'Transactions')]"));
	    transactionsLink.click();

	    applyFilters(driver);

	    List<List<String>> tableData = extractTableData(driver);

	    String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String filePath = "target/output_" + timestamp + ".csv";
	    generateCSV(tableData, filePath);

	    driver.quit();
	}


    private static void login(WebDriver driver) {
        driver.get("https://app.tryloop.ai/login/password");

        driver.manage().window().maximize();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

        WebElement username = driver.findElement(By.id(":r1:"));
        username.sendKeys("qa-engineer-assignment@test.com");

        WebElement password = driver.findElement(By.id(":r2:"));
        password.sendKeys("QApassword123$");

        WebElement logbtn = driver.findElement(By.xpath("//button[normalize-space()='Login']"));
        logbtn.click();

        WebElement skip = driver.findElement(By.xpath("//button[normalize-space()='Skip for now']"));
        skip.click();
    }

    private static void applyFilters(WebDriver driver) {
      
    }

    private static List<List<String>> extractTableData(WebDriver driver) {
        WebElement table = driver.findElement(By.xpath("//div[@class='MuiBox-root css-mycntp']"));

        List<WebElement> rows = table.findElements(By.xpath(".//tbody/tr"));

        List<List<String>> tableData = new ArrayList<>();

        for (WebElement row : rows) {
            List<String> rowData = new ArrayList<>();
            List<WebElement> cells = row.findElements(By.tagName("td"));

            for (WebElement cell : cells) {
                boolean isStale = true;
                int attempts = 0;

                while (isStale && attempts < 3) {
                    try {
                        rowData.add(cell.getText().replaceAll("\\n", " "));
                        isStale = false;
                    } catch (org.openqa.selenium.StaleElementReferenceException e) {
                        attempts++;
                    }
                }
            }

            tableData.add(rowData);
        }

        return tableData;
    }



    private static void generateCSV(List<List<String>> data, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Order ID,Location,Order State,Type,Lost Sale,Net Payout,Payout ID,Payout Date\n");

            for (List<String> row : data) {
                writer.write(String.join(",", row) + "\n");
            }

            System.out.println("CSV file generated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
