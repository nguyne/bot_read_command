package bot.telegram.bot_read_command.utility;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LoginWinGo {
    public static void loginWinGo(){
        WebDriverUtility.closeDriver();
        WebDriver driver = WebDriverUtility.getDriver();
        WebDriverWait wait = WebDriverUtility.getWebDriverWait();
        try {
            driver.get("https://vn168a.com/#/login");
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("userNumber")));
            element.sendKeys("398869150");
            WebElement passwordInput = driver.findElement(By.cssSelector("input[type='password']"));
            passwordInput.sendKeys("0398869150az");
            Thread.sleep(3*1000);
            WebElement loginButton = driver.findElement(By.cssSelector(".signIn__container-button button"));
            loginButton.click();
            System.out.println("Page title after login: " + driver.getTitle());
            Thread.sleep(2*1000);
            WebElement confirmButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".van-button__content")));
            String buttonText = confirmButton.getText();
            System.out.println("Text of the button: " + buttonText);
            confirmButton.click();

            WebElement winGoElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Win Go']")));
            winGoElement.click();
            Thread.sleep(1000);
            System.out.println("Đã Vào trang Win Go");
        }catch (Exception e){}

    }
}
