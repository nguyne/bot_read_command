package bot.telegram.bot_read_command.Component;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FormulaInputStatusManager {

    private Map<Long, Boolean> formulaInputInProgressMap = new HashMap<>();

    public boolean isFormulaInputInProgress(Long chatId) {
        return formulaInputInProgressMap.getOrDefault(chatId, false);
    }

    public void setFormulaInputInProgress(Long chatId, boolean inProgress) {
        formulaInputInProgressMap.put(chatId, inProgress);
    }
}
