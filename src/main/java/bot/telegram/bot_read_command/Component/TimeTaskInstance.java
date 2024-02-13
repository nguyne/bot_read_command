package bot.telegram.bot_read_command.Component;

import bot.telegram.bot_read_command.entity.Group;
import bot.telegram.bot_read_command.thread.TimeTask;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TimeTaskInstance {

    private static Map<String, TimeTask> timeTaskInstanceMap = new ConcurrentHashMap<>();

    public static void setTimeTaskInstanceMap(String groupId, TimeTask timeTaskInstance){
        timeTaskInstanceMap.put(groupId, timeTaskInstance);

    }

    public static TimeTask getTimeTask(Group group){
        return timeTaskInstanceMap.get(group.getGroupId());
    }

    public static TimeTask removeTimeTask(Group group){
        return timeTaskInstanceMap.remove(group.getGroupId());
    }
}
