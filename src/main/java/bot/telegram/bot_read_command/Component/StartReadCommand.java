package bot.telegram.bot_read_command.Component;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class StartReadCommand {

    private static Map<Long, List<String>> startReadCommandMap = new HashMap<>();

    public static int processHour(Long groupId, int index) {
        return getTime(groupId, index, 0);
    }

    public static int processMinute(Long groupId, int index) {
        return getTime(groupId, index, 1);
    }

    public static int getTime(Long groupId, int index, int value) {
        List<String> times = startReadCommandMap.get(groupId);
        if (times != null && index < times.size()) {
            String time = times.get(index);
            String[] parts = time.split(":");
            if (parts.length == 2) {
                return Integer.parseInt(parts[value]);
            }
        }
        return -1;
    }

    public static List<String> getAllTime(Long groupId) {
        return startReadCommandMap.getOrDefault(groupId, Collections.emptyList());
    }

    public static boolean setTime(Long groupId, String time) {
        if (!startReadCommandMap.containsKey(groupId)) {
            startReadCommandMap.put(groupId, new ArrayList<>());
        }

        List<String> times = startReadCommandMap.get(groupId);

        if (!times.contains(time)) {
            times.add(time);
            return true;
        } else {
            return false;
        }
    }

    public static boolean removeTime(Long groupId, String time) {
        List<String> times = startReadCommandMap.get(groupId);
        if (times != null) {
            if (times.remove(time)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

}
