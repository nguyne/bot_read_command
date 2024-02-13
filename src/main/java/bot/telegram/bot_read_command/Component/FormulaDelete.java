package bot.telegram.bot_read_command.Component;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FormulaDelete {

    private Map<Long, Boolean> formulaDeleteMap = new HashMap<>();

    public boolean isFormulaDelete(Long chatId) {
        return formulaDeleteMap.getOrDefault(chatId, false);
    }

    public void setFormulaDelete(Long chatId, boolean inProgress) {
        formulaDeleteMap.put(chatId, inProgress);
    }
}
