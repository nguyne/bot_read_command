package bot.telegram.bot_read_command.entity;

import java.util.HashMap;
import java.util.Map;

public class ResultInt {
    private static Map<Long, Integer> resultIntMap = new HashMap<>();

    public static Integer getResultIntInProgress(Long groupId) {
        return resultIntMap.getOrDefault(groupId, 15);
    }

    public static void setResultIntInProgress(Long groupId, int resultInt) {
        resultIntMap.put(groupId, resultInt);
    }
}
