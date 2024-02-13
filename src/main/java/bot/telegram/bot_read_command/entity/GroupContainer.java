package bot.telegram.bot_read_command.entity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GroupContainer {

    private static List<Group> groupList = new ArrayList<>();
    private static final String FILE_PATH = "groupList.dat";

    static {
        if (fileExists(FILE_PATH)) {
            readDataFromFile();
        }
    }

    private static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static List<Group> getGroupList() {
        return groupList;
    }
//sửa hàm này..
    public static void addGroup(Group group) {
        groupList.add(group);
        ensureFileExists();
        saveDataToFile();
    }

    private static void ensureFileExists() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateFileAfterRemoval() {
        saveDataToFile();
    }

    private static void saveDataToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(groupList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readDataFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                groupList = (List<Group>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
