package bot.telegram.bot_read_command.entity;

import java.io.*;
import java.util.*;

public class FormulaContainer {
    private static Map<String, List<Formula>> formulasByGroup = new HashMap<>();
    private static final String FILE_PATH = "formulaList.dat";

    static {
        if (fileExists(FILE_PATH)) {
            readDataFromFile();
        }
    }

    private static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static Optional<Formula> findFormula(String groupId, String formulaNameToFind ) {
        List<Formula> formulas = formulasByGroup.get(groupId);
        if (formulas != null) {
            return formulas.stream()
                    .filter(formula -> formulaNameToFind.equals(formula.getFormula()))
                    .findFirst();
        }
        return Optional.empty();
    }

    public static String getResult(String groupId, String formulaName) {
        Optional<Formula> formulaOptional = findFormula(groupId, formulaName);

        if (formulaOptional.isPresent()) {
            Formula formula = formulaOptional.get();
            return formula.getResult();
        } else {
            return null;
        }
    }

    public static List<Formula> getFormulasByGroup(String groupId) {
        return formulasByGroup.getOrDefault(groupId, new ArrayList<>());
    }

    public static int getPositionInFormulaList(String groupId, String formula) {
        List<Formula> formulas = formulasByGroup.get(groupId);
        if (formulas != null) {
            for (int i = 0; i < formulas.size(); i++) {
                if (formula.equals(formulas.get(i).getFormula())) {
                    return i;
                }
            }
        }
        return -1;
    }


    public static void addFormula(String groupId, Formula formula) {
        List<Formula> formulas = formulasByGroup.computeIfAbsent(groupId, k -> new ArrayList<>());
        formulas.add(formula);
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

    public static void deleteFormula(String groupId, String formulaName) {
        List<Formula> formulas = formulasByGroup.get(groupId);

        if (formulas != null) {
            formulas.removeIf(formula -> formulaName.equals(formula.getFormula()));
            saveDataToFile();
        }
    }

    private static void saveDataToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(formulasByGroup);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readDataFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            Object obj = ois.readObject();
            if (obj instanceof Map) {
                formulasByGroup = (Map<String, List<Formula>>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
