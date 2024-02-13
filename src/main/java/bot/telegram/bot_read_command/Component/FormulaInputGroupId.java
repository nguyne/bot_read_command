package bot.telegram.bot_read_command.Component;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FormulaInputGroupId {

    private Map<Long, String> formulaInputInProgressMap = new HashMap<>();

    public String getFormulaInputInProgress(Long chatId) {
        return formulaInputInProgressMap.get(chatId);
    }

    public void setFormulaInputInProgress(Long chatId, String groupId) {
        formulaInputInProgressMap.put(chatId, groupId);
    }
}
