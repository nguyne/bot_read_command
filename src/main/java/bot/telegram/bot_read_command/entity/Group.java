package bot.telegram.bot_read_command.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Group implements Serializable {

    private String nameGroup;
    private String adminId;
    private String groupId;
}
