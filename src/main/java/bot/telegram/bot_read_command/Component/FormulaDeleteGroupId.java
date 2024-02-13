package bot.telegram.bot_read_command.Component;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
@Component
public class FormulaDeleteGroupId {

    private Map<Long, String> formulaDeleteGroupMap = new HashMap<>();

    public String getFormulaDeleteInProgress(Long chatId) {
        return formulaDeleteGroupMap.get(chatId);
    }

    public void setFormulaDeleteInProgress(Long chatId, String groupId) {
        formulaDeleteGroupMap.put(chatId, groupId);
    }
}
