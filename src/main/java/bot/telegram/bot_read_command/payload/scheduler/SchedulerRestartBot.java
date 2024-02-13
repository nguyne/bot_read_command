package bot.telegram.bot_read_command.payload.scheduler;

import bot.telegram.bot_read_command.payload.botTele.MyBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Component
public class SchedulerRestartBot {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${telegram.chatId}")
    private Long chatId;

    @Autowired
    private MyBot myBot;

    @Scheduled(cron = "45 0 0 * * ?", zone = "Asia/Ho_Chi_Minh")
    public String RestartDyno() {
        String apiKey = System.getenv("HEROKU_API_KEY");
        String appName = "bot-read-command";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/vnd.heroku+json; version=3");

        Map<String, Object> requestBody = Collections.emptyMap();

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.heroku.com/apps/" + appName + "/dynos",
                HttpMethod.DELETE,
                entity,
                String.class
        );
        if (response.getStatusCode() == HttpStatus.ACCEPTED) {
            myBot.sendTextMessage(chatId,"Dyno restart initiated successfully.");
            return "Dyno restart initiated successfully.";
        } else {
            myBot.sendTextMessage(chatId,"Failed to initiate dyno restart. Status code: " + response.getStatusCode());
            return "Failed to initiate dyno restart. Status code: " + response.getStatusCode();
        }
    }
}
