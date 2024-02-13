package bot.telegram.bot_read_command;

import bot.telegram.bot_read_command.Component.StartReadCommand;
import bot.telegram.bot_read_command.entity.ResultInt;
import bot.telegram.bot_read_command.utility.LoginWinGo;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@EnableScheduling
@SpringBootApplication
public class BotReadCommandApplication {


	public static void main(String[] args) {
		SpringApplication.run(BotReadCommandApplication.class, args);

		WebDriverManager.chromedriver().setup();

		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
		LoginWinGo.loginWinGo();
	}
}
