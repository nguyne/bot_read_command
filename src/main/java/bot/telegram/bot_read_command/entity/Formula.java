package bot.telegram.bot_read_command.entity;

import java.io.Serializable;

public class Formula implements Serializable {
    private static final long serialVersionUID = 1L;

    private String groupId;
    private String formula;
    private String result;

    public Formula() {
    }


    public Formula(String groupId, String formula, String result) {
        this.groupId = groupId;
        this.formula = formula;
        this.result = result;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
