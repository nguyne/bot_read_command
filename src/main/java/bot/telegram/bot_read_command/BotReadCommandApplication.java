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

		//☘️ Đại Bàng ☘️ VN168 🔝
		StartReadCommand.setTime(-1001778715715L,"0:10");
		StartReadCommand.setTime(-1001778715715L,"1:00");
		StartReadCommand.setTime(-1001778715715L,"2:00");
		StartReadCommand.setTime(-1001778715715L,"3:00");
		StartReadCommand.setTime(-1001778715715L,"4:00");
		StartReadCommand.setTime(-1001778715715L,"5:00");
		StartReadCommand.setTime(-1001778715715L,"6:00");
		StartReadCommand.setTime(-1001778715715L,"7:00");
		StartReadCommand.setTime(-1001778715715L,"8:30");
		StartReadCommand.setTime(-1001778715715L,"9:30");
		StartReadCommand.setTime(-1001778715715L,"10:30");
		StartReadCommand.setTime(-1001778715715L,"11:30");
		StartReadCommand.setTime(-1001778715715L,"13:30");
		StartReadCommand.setTime(-1001778715715L,"15:30");
		StartReadCommand.setTime(-1001778715715L,"17:30");
		StartReadCommand.setTime(-1001778715715L,"19:30");
		StartReadCommand.setTime(-1001778715715L,"22:30");

		//TOP 1 FAMILY168
		StartReadCommand.setTime(-1001934901667L,"7:30");
		StartReadCommand.setTime(-1001934901667L,"8:30");
		StartReadCommand.setTime(-1001934901667L,"9:30");
		StartReadCommand.setTime(-1001934901667L,"10:30");
		StartReadCommand.setTime(-1001934901667L,"11:30");
		StartReadCommand.setTime(-1001934901667L,"12:30");
		StartReadCommand.setTime(-1001934901667L,"13:30");
		StartReadCommand.setTime(-1001934901667L,"14:30");
		StartReadCommand.setTime(-1001934901667L,"15:30");
		StartReadCommand.setTime(-1001934901667L,"16:30");
		StartReadCommand.setTime(-1001934901667L,"18:30");
		StartReadCommand.setTime(-1001934901667L,"19:30");
		StartReadCommand.setTime(-1001934901667L,"20:30");
		StartReadCommand.setTime(-1001934901667L,"21:30");
		StartReadCommand.setTime(-1001934901667L,"22:30");
		ResultInt.setResultIntInProgress(-1001934901667L,10);

		//LỤM LÚA 168
		StartReadCommand.setTime(-1002106198340L,"1:05");
		StartReadCommand.setTime(-1002106198340L,"1:40");
		StartReadCommand.setTime(-1002106198340L,"2:15");
		StartReadCommand.setTime(-1002106198340L,"2:40");
		StartReadCommand.setTime(-1002106198340L,"3:10");
		StartReadCommand.setTime(-1002106198340L,"3:45");
		StartReadCommand.setTime(-1002106198340L,"5:30");
		StartReadCommand.setTime(-1002106198340L,"6:30");
		StartReadCommand.setTime(-1002106198340L,"7:30");
		StartReadCommand.setTime(-1002106198340L,"8:30");
		StartReadCommand.setTime(-1002106198340L,"9:30");
		StartReadCommand.setTime(-1002106198340L,"10:30");
		StartReadCommand.setTime(-1002106198340L,"11:30");
		StartReadCommand.setTime(-1002106198340L,"12:30");
		StartReadCommand.setTime(-1002106198340L,"13:30");
		StartReadCommand.setTime(-1002106198340L,"14:30");
		StartReadCommand.setTime(-1002106198340L,"15:30");
		StartReadCommand.setTime(-1002106198340L,"16:30");
		StartReadCommand.setTime(-1002106198340L,"17:30");
		StartReadCommand.setTime(-1002106198340L,"18:30");
		StartReadCommand.setTime(-1002106198340L,"19:30");
		StartReadCommand.setTime(-1002106198340L,"20:30");
		StartReadCommand.setTime(-1002106198340L,"21:30");
		StartReadCommand.setTime(-1002106198340L,"22:10");
		StartReadCommand.setTime(-1002106198340L,"22:45");
		StartReadCommand.setTime(-1002106198340L,"23:20");
		ResultInt.setResultIntInProgress(-1002106198340L,10);

		//【𝐕𝐈𝐏】𝐌𝐎𝐍𝐄𝐘 𝐓𝐄𝐀𝐌🎖 (PL 26/01/2024)
		StartReadCommand.setTime(-1002050487355L,"0:10");
		StartReadCommand.setTime(-1002050487355L,"1:00");
		StartReadCommand.setTime(-1002050487355L,"2:00");
		StartReadCommand.setTime(-1002050487355L,"3:00");
		StartReadCommand.setTime(-1002050487355L,"4:00");
		StartReadCommand.setTime(-1002050487355L,"5:00");
		StartReadCommand.setTime(-1002050487355L,"6:00");
		StartReadCommand.setTime(-1002050487355L,"7:00");
		StartReadCommand.setTime(-1002050487355L,"8:00");
		StartReadCommand.setTime(-1002050487355L,"9:00");
		StartReadCommand.setTime(-1002050487355L,"11:00");
		StartReadCommand.setTime(-1002050487355L,"13:00");
		StartReadCommand.setTime(-1002050487355L,"14:00");
		StartReadCommand.setTime(-1002050487355L,"16:00");
		StartReadCommand.setTime(-1002050487355L,"17:00");
		StartReadCommand.setTime(-1002050487355L,"20:00");
		StartReadCommand.setTime(-1002050487355L,"21:00");
		StartReadCommand.setTime(-1002050487355L,"22:00");
		StartReadCommand.setTime(-1002050487355L,"23:00");

		//VIP LỘC PHÁT (HNam 15/01/2024)
		StartReadCommand.setTime(-1001963575619L,"7:30");
		StartReadCommand.setTime(-1001963575619L,"8:30");
		StartReadCommand.setTime(-1001963575619L,"9:30");
		StartReadCommand.setTime(-1001963575619L,"10:30");
		StartReadCommand.setTime(-1001963575619L,"11:30");
		StartReadCommand.setTime(-1001963575619L,"12:30");
		StartReadCommand.setTime(-1001963575619L,"13:30");
		StartReadCommand.setTime(-1001963575619L,"14:30");
		StartReadCommand.setTime(-1001963575619L,"15:30");
		StartReadCommand.setTime(-1001963575619L,"16:30");
		StartReadCommand.setTime(-1001963575619L,"19:30");
		StartReadCommand.setTime(-1001963575619L,"20:30");
		StartReadCommand.setTime(-1001963575619L,"21:30");
		StartReadCommand.setTime(-1001963575619L,"22:30");
		ResultInt.setResultIntInProgress(-1001963575619L,10);
	}
}
