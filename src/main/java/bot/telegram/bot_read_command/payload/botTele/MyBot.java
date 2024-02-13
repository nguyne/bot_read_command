package bot.telegram.bot_read_command.payload.botTele;

import bot.telegram.bot_read_command.Component.*;
import bot.telegram.bot_read_command.entity.*;
import bot.telegram.bot_read_command.thread.TimeTask;
import bot.telegram.bot_read_command.utility.LoginWinGo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class MyBot extends TelegramLongPollingBot {

    @Autowired
    private FormulaInputStatusManager formulaInputStatusManager;

    @Autowired
    private FormulaDelete formulaDelete;

    @Autowired
    private FormulaDeleteGroupId formulaDeleteGroupId;

    @Autowired
    private FormulaInputGroupId formulaInputGroupId;

    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.bot.name}")
    private String name;

    @Value("${telegram.chatId}")
    private Long ID;

    private StringBuilder successMessageBuilder = new StringBuilder();

    private StringBuilder errorMessageBuilder = new StringBuilder();


    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            if(formulaDelete.isFormulaDelete(chatId)){
                setFormulaDelete(update, chatId);
                return;
            }
            if (formulaInputStatusManager.isFormulaInputInProgress(chatId)) {
                setFormula(update, chatId);
            } else {
                if (chatId.equals(ID)) {
                    if (update.getMessage().getText().startsWith("/addGroup")) {
                        addGroup(update, chatId);
                        return;
                    }

                    if (update.getMessage().getText().startsWith("/xoaGroup")) {
                        deleteGroup(update, chatId);
                        return;
                    }
                    if(update.getMessage().getText().startsWith("/login")){
                        LoginWinGo.loginWinGo();
                    }
                }
                List<Group> list = GroupContainer.getGroupList();
                boolean adminIdExists = list.stream().anyMatch(group -> chatId.equals(Long.valueOf(group.getAdminId())));
                if (adminIdExists || chatId.equals(ID)) {

                    if (update.getMessage().getText().startsWith("/start")) {
                        start(update, chatId);
                        return;
                    }

                    if (update.getMessage().getText().startsWith("/Group")) {
                        sendGroupListMessage(chatId);
                        return;
                    }

                    if(update.getMessage().getText().startsWith("/chot")){
                        chot(update, chatId);
                        return;
                    }
                    if(update.getMessage().getText().startsWith("/xemChot")){
                        xemChot(update, chatId);
                        return;
                    }
                    if(update.getMessage().getText().startsWith("/ketthuc")){
                        stopTime(update,chatId,list);
                        return;
                    }
                    if (update.getMessage().getText().startsWith("/addCT")) {
                        addFormula(update, chatId);
                        return;
                    }
                    if (update.getMessage().getText().startsWith("/xoaCT")) {
                        deleteFormula(update,chatId);
                        return;
                    }
                    if (update.getMessage().getText().startsWith("/xemCT")) {
                        xemCT(update, chatId);
                        return;
                    }
                    if(update.getMessage().getText().startsWith("/setTime")){
                        setTimeStart(update, chatId);
                        return;
                    }
                    if(update.getMessage().getText().startsWith("/getTime")){
                        getTimeStart(update, chatId);
                        return;
                    }
                    if (update.getMessage().getText().startsWith("/xoaTime")) {
                        deleteTime(update, chatId);
                        return;
                    }
                }
                if(update.getMessage().getText().startsWith("/help")){
                    sendHelpMessage(chatId, adminIdExists);
                }
            }
        }
    }

    /*
    *
    * tự động đọc lệnh cho all group
    * */
    @Scheduled(fixedRate = 60000)
    private void runScheduledTask() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        int currentHour = now.getHour();
        int currentMinute = now.getMinute();
        List<Group> groupList = GroupContainer.getGroupList();
        for (Group group : groupList) {
            checkAndPerformTask(group, currentHour, currentMinute);
        }
    }

    private void checkAndPerformTask(Group group, int currentHour, int currentMinute) {
        Long groupId = Long.valueOf(group.getGroupId());
        List<String> times = StartReadCommand.getAllTime(groupId);

        for (String time : times) {
            int hour = StartReadCommand.processHour(groupId, times.indexOf(time));
            int minute = StartReadCommand.processMinute(groupId, times.indexOf(time));
            if (minute >= 5) {
                if (isTimeWithinRange(currentHour, currentMinute, hour, minute - 5)) {
                    sendTextMessage(Long.valueOf(group.getGroupId()), "Vào ca !!!\n" +
                            "Đến hẹn lại lên nào cả nhà  \uD83D\uDCE2 . Mọi người chuẩn bị vốn ss lên ca nhé ❤\uFE0F❤\uFE0F❤\uFE0F. Nhớ chia vốn 8 tay để theo CG nhé \uD83D\uDE0B");
                    sendTextMessage(Long.valueOf(group.getAdminId()), "Thông báo SS nhóm: "+group.getNameGroup());
                }
            } else {
                if (isTimeWithinRange(currentHour, currentMinute, hour - 1, 60 + (minute - 5))) {
                    sendTextMessage(Long.valueOf(group.getGroupId()), "Vào ca !!!\n" +
                            "Đến hẹn lại lên nào cả nhà  \uD83D\uDCE2 . Mọi người chuẩn bị vốn ss lên ca nhé ❤\uFE0F❤\uFE0F❤\uFE0F. Nhớ chia vốn 8 tay để theo CG nhé \uD83D\uDE0B");
                    sendTextMessage(Long.valueOf(group.getAdminId()), "Thông báo SS nhóm: "+group.getNameGroup());
                }
            }
            if (isTimeWithinRange(currentHour, currentMinute, hour, minute)) {
                sendTextMessage(Long.valueOf(group.getAdminId()), "Thực hiện công việc cho nhóm " + group.getNameGroup() + "\n Bây giờ là: " + time);

                CompletableFuture.runAsync(() -> {
                    TimeTask timeTaskInstance = ApplicationContextProvider.getApplicationContext().getBean(TimeTask.class);
                    TimeTaskInstance.setTimeTaskInstanceMap(String.valueOf(groupId), timeTaskInstance);
                    timeTaskInstance.setGroup(group);
                    timeTaskInstance.run();
                });
            }
        }
    }
    private void stopTime(Update update, Long chatId, List<Group> list){
        String[] parts = update.getMessage().getText().split("_");
        if(parts.length == 2){
            String groupId = parts[1];
            Optional<Group> groupToId = list.stream()
                    .filter(group -> groupId.equals(group.getGroupId()) && chatId.equals(Long.valueOf(group.getAdminId())))
                    .findFirst();

            if (groupToId.isPresent()) {
                Group group = groupToId.get();
                TimeTask timeTask = TimeTaskInstance.getTimeTask(group);
                if(timeTask != null){
                    timeTask.stop();
                    sendTextMessage(chatId, "Đã tắt đọc lệnh cho Group có GroupId: " + groupToId);
                }else {
                    sendTextMessage(chatId, "Chưa mở bot cho Group có GroupId: " + groupToId);
                }
            } else {
                sendTextMessage(chatId, "Bạn không có quyền cho Group có GroupId: " + groupToId+".\nVui lòng /Group để biết thêm thông tin chi tiết.");
            }
        }
    }
    public boolean isTimeWithinRange(int currentHour, int currentMinute, int targetHour, int targetMinute){
        return currentHour == targetHour && currentMinute == targetMinute;
    }
    /*
    * kết thúc
    * */

    /*
    * Hàm start là để ra lệnh bot đọc lệnh
    * có thể bỏ. và hiện tại chưa cài đặt đúng mục đích*/
    private void start(Update update, Long chatId){
        String[] parts = update.getMessage().getText().split("_");
        List<Group> list = GroupContainer.getGroupList();
        if(parts.length == 2){
            String groupId = parts[1];
            Optional<Group> groupToId = list.stream()
                    .filter(group -> groupId.equals(group.getGroupId()) && chatId.equals(Long.valueOf(group.getAdminId())))
                    .findFirst();

            if (groupToId.isPresent()) {
                Group group = groupToId.get();
                sendTextMessage(chatId,"kích hoạt bot đọc lệnh cho group: "+group.getNameGroup()+" Thành công");
                CompletableFuture.runAsync(() -> {
                    TimeTask timeTaskInstance = ApplicationContextProvider.getApplicationContext().getBean(TimeTask.class);
                    TimeTaskInstance.setTimeTaskInstanceMap(groupId, timeTaskInstance);
                    timeTaskInstance.setGroup(group);
                    timeTaskInstance.run();
                });
            } else {
                sendTextMessage(chatId, "Bạn không có quyền cho Group có GroupId: " + groupToId+".\nVui lòng /Group để biết thêm thông tin chi tiết.");
            }
        }
    }

    /*
    * thêm Group theo groupId*/
    private void addGroup(Update update, Long chatId){

        String[] parts = update.getMessage().getText().split("_");
        if (parts.length == 4) {
            String groupId = parts[3];
            List<Group> list = GroupContainer.getGroupList();
            boolean groupIdExists = list.stream().anyMatch(group -> groupId.equals(group.getGroupId()));

            if (groupIdExists) {
                sendTextMessage(chatId, "GroupId đã tồn tại trong danh sách.");
            }else {
                String nameGroup = parts[1];
                String adminId = parts[2];


                Group newGroup = new Group();
                newGroup.setNameGroup(nameGroup);
                newGroup.setAdminId(adminId);
                newGroup.setGroupId(groupId);

                GroupContainer.addGroup(newGroup);
                sendTextMessage(chatId, "Thêm Thành Công!!");
            }
        }else {
            sendTextMessage(chatId, "Không đúng định dạng!!");
        }

    }
    /*
     * kết thúc thêm group*/
    /*
    * Tổng hợp các hàm thêm công thức cho 1 group
    * chat/addCT_<groupId>
    */
    private void addFormula(Update update, Long chatId) {
        String[] parts = update.getMessage().getText().split("_");
        if (parts.length == 2) {
            String groupId = parts[1];
            List<Group> list = GroupContainer.getGroupList();
            boolean groupIdExists = list.stream()
                    .anyMatch(group -> groupId.equals(group.getGroupId()) && chatId.equals(Long.valueOf(group.getAdminId())));
            if (groupIdExists || chatId.equals(ID)) {
                // Kiểm tra xem có đang trong quá trình nhập công thức không
                boolean formulaInputInProgress = formulaInputStatusManager.isFormulaInputInProgress(chatId);

                if (!formulaInputInProgress) {
                    Optional<Group> groupToId = list.stream()
                            .filter(group -> groupId.equals(group.getGroupId()))
                            .findFirst();
                    // Nếu không, bắt đầu quá trình nhập công thức và gửi thông báo
                    sendTextMessage(chatId, "Bắt đầu nhập công thức. Kết thúc bằng /end");
                    formulaInputStatusManager.setFormulaInputInProgress(Long.valueOf(groupToId.get().getAdminId()), true);
                    formulaInputGroupId.setFormulaInputInProgress(Long.valueOf(groupToId.get().getAdminId()), groupId);
                }
            } else {
                sendTextMessage(chatId, "Không có quyền thêm công thức vào group này.");
            }
        }
    }
    //sau đó hàm này xử lí điền công thức
    private void setFormula(Update update, Long chatId) {
        if (update.getMessage().getText().equalsIgnoreCase("/end")) {
            sendTextMessage(chatId, "Đã kết thúc nhập công thức.");
            formulaInputStatusManager.setFormulaInputInProgress(chatId, false);
        } else {
            String[] formulaParts = update.getMessage().getText().split("\n");

            String groupId = formulaInputGroupId.getFormulaInputInProgress(chatId);

            for (String formulaPart : formulaParts) {
                processFormulaLine(groupId, formulaPart);
            }
            sendFormulaMessages(chatId);
        }
    }
    //kiểm tra và thêm
    private void processFormulaLine(String groupId, String formulaLine) {
        String[] parts = formulaLine.split("_");

        if (parts.length == 2) {
            String formula = parts[0];
            String result = parts[1];

            // Kiểm tra xem công thức đã tồn tại chưa
            Optional<Formula> existingFormula = FormulaContainer.findFormula(groupId, formula);

            if (!existingFormula.isPresent()) {
                // Công thức chưa tồn tại, thêm mới vào danh sách
                Formula newFormula = new Formula();
                newFormula.setGroupId(groupId);
                newFormula.setFormula(formula);
                newFormula.setResult(result);
                FormulaContainer.addFormula(groupId, newFormula);
                // Thêm thông báo vào StringBuilder
                successMessageBuilder.append("Đã thêm công thức mới: ").append(formula).append("\n");
            } else {
                // Công thức đã tồn tại, thêm thông báo vào StringBuilder
                int position = FormulaContainer.getPositionInFormulaList(groupId, formula);
                errorMessageBuilder.append("Công thức ").append(formula)
                        .append(" đã tồn tại (CT").append(position + 1).append(")\n");
            }
        } else {
            // Định dạng không hợp lệ, thêm thông báo vào StringBuilder
            errorMessageBuilder.append("Định dạng công thức không hợp lệ: ").append(formulaLine).append("\n");
        }
    }
    //gửi thông điệp từ bot đến người dùng
    private void sendFormulaMessages(Long chatId) {
        if (successMessageBuilder.length() > 0) {
            sendTextMessage(chatId, successMessageBuilder.toString());
        }
        if (errorMessageBuilder.length() > 0) {
            sendTextMessage(chatId, errorMessageBuilder.toString());
        }
        // Reset StringBuilder sau khi gửi
        successMessageBuilder.setLength(0);
        errorMessageBuilder.setLength(0);
    }
    /*
    * Kết thúc thêm công thức
    * */

    /*
    * Xóa Công thức
    * */
    private void deleteFormula(Update update, Long chatId) {
        String[] parts = update.getMessage().getText().split("_");

        if (parts.length == 2) {
            String groupId = parts[1];
            List<Group> list = GroupContainer.getGroupList();
            boolean groupIdExists = list.stream()
                    .anyMatch(group -> groupId.equals(group.getGroupId()) && chatId.equals(Long.valueOf(group.getAdminId())));

            if (groupIdExists || chatId.equals(ID)) {
                // Kiểm tra xem có đang trong quá trình xóa công thức không
                boolean formulaDeleteInProgress = formulaDelete.isFormulaDelete(chatId);

                if (!formulaDeleteInProgress) {
                    Optional<Group> groupToId = list.stream()
                            .filter(group -> groupId.equals(group.getGroupId()))
                            .findFirst();
                    // Nếu không, bắt đầu quá trình xóa công thức và gửi thông báo
                    sendTextMessage(chatId, "Bắt đầu xóa công thức. Kết thúc bằng /endDelete");
                    formulaDelete.setFormulaDelete(Long.valueOf(groupToId.get().getAdminId()), true);
                    formulaDeleteGroupId.setFormulaDeleteInProgress(Long.valueOf(groupToId.get().getAdminId()), groupId);
                }
            } else {
                sendTextMessage(chatId, "Không có quyền xóa công thức trong group này.");
            }
        }
    }

    private void setFormulaDelete(Update update, Long chatId) {
        if (update.getMessage().getText().equalsIgnoreCase("/endDelete")) {
            endDeleteFormula(chatId);
        } else {
            String[] formulaParts = update.getMessage().getText().split("\n");

            String groupId = formulaDeleteGroupId.getFormulaDeleteInProgress(chatId);

            for (String formulaPart : formulaParts) {
                processDeleteFormulaLine(groupId, formulaPart);
            }
            if (successMessageBuilder.length() > 0) {
                sendTextMessage(chatId, successMessageBuilder.toString());
            }
            if (errorMessageBuilder.length() > 0) {
                sendTextMessage(chatId, errorMessageBuilder.toString());
            }
            successMessageBuilder.setLength(0);
            errorMessageBuilder.setLength(0);
        }
    }
    private void processDeleteFormulaLine(String groupId, String formulaLine) {
        String[] parts = formulaLine.split("_");

        if (parts.length == 2) {
            String formula = parts[0];
            // Kiểm tra xem công thức có tồn tại không
            Optional<Formula> existingFormula = FormulaContainer.findFormula(groupId, formula);

            if (existingFormula.isPresent()) {
                // Nếu tồn tại, xóa công thức khỏi danh sách
                FormulaContainer.deleteFormula(groupId, formula);
                successMessageBuilder.append("Đã xóa công thức: ").append(formulaLine).append("\n");
            } else {
                // Nếu không tồn tại, thêm thông báo vào StringBuilder
                errorMessageBuilder.append("Công thức ").append(formulaLine).append(" không tồn tại\n");
            }
        }
    }

    private void endDeleteFormula(Long chatId) {
        // Kết thúc quá trình xóa và gửi thông báo
        formulaDelete.setFormulaDelete(chatId, false);
    }
    /*
    * Kết thúc*/

    /*
    * Hiển thị thời gian đã cài đặt cho group
    * chat /getTime_<groupId>
    */
    private void getTimeStart(Update update, Long chatId) {
        String[] parts = update.getMessage().getText().split("_");
        if (parts.length == 2) {
            String groupId = parts[1];
            List<Group> list = GroupContainer.getGroupList();
            boolean groupIdExists = list.stream()
                    .anyMatch(group -> groupId.equals(group.getGroupId()) && chatId.equals(Long.valueOf(group.getAdminId())));

            if (groupIdExists) {
                List<String> allTime = StartReadCommand.getAllTime(Long.valueOf(groupId));
                if (!allTime.isEmpty()) {
                    Optional<Group> group = list.stream()
                            .filter(groups -> groupId.equals(groups.getGroupId()))
                            .findFirst();
                    StringBuilder messageBuilder = new StringBuilder("Thời gian đã cài đặt cho Group <b>" + group.get().getNameGroup() + "</b> là:\n");
                    for (String time : allTime) {
                        messageBuilder.append(time).append("\n");
                    }
                    sendTextMessage(chatId, messageBuilder.toString());
                } else {
                    sendTextMessage(chatId, "Chưa có thời gian nào được cài đặt cho Group " + groupId);
                }
            } else {
                sendTextMessage(chatId, "Không có quyền xem cài đặt thời gian của group này.");
            }
        } else {
            sendTextMessage(chatId, "Vui lòng dùng đúng cú pháp /getTime_groupId");
        }
    }
    /*
    * kết thúc hàm hiển thị thời gian
    * */
    /*
    * setup time cho group
    * chat /setTime_<groupId>*/
    private void setTimeStart(Update update, Long chatId){
        String[] parts = update.getMessage().getText().split("_");
        if(parts.length == 3){
            String groupId = parts[1];
            List<Group> list = GroupContainer.getGroupList();
            boolean groupIdExists = list.stream()
                    .anyMatch(group -> groupId.equals(group.getGroupId()) && chatId.equals(Long.valueOf(group.getAdminId())));

            if(groupIdExists || chatId.equals(ID)){
                String[] time = parts[2].split(":");
                if(time.length == 2){
                    if(StartReadCommand.setTime(Long.valueOf(groupId),parts[2])) {
                        sendTextMessage(chatId, "Cài đặt thời gian thành công!");
                    }else {
                        sendTextMessage(chatId, "Thời gian này đã tồn tai /getTime_"+groupId+" để xem thời gian!");
                    }
                }else {
                    sendTextMessage(chatId, "Vui lòng dùng đúng cú pháp /setTime_groupId_H:m");
                }
            }
            else {
                sendTextMessage(chatId, "Không có quyền cài đặt thời gian cho group này.");
            }
        }else {
            sendTextMessage(chatId, "Vui lòng dùng đúng cú pháp /setTime_groupId_H:m");
        }
    }
    /*
     * kết thúc hàm setup time
     * */

    private void deleteTime(Update update, Long chatId){
        String[] parts = update.getMessage().getText().split("_");
        if (parts.length == 3) {
            String groupId = parts[1];
            String time = parts[2];
            List<Group> lists = GroupContainer.getGroupList();
            boolean groupIdExists = lists.stream()
                    .anyMatch(group -> groupId.equals(group.getGroupId()) && chatId.equals(Long.valueOf(group.getAdminId())));
            if (groupIdExists || chatId.equals(ID)) {
                // Gọi hàm xóa thời gian
                boolean removed = StartReadCommand.removeTime(Long.valueOf(groupId), time);
                if (removed) {
                    sendTextMessage(chatId, "Đã xóa thời gian " + time + " cho nhóm " + groupId);
                } else {
                    sendTextMessage(chatId, "Không tìm thấy thời gian " + time + " để xóa cho nhóm " + groupId);
                }
            } else {
                sendTextMessage(chatId, "Không có quyền trong nhóm này.");
            }
        }
    }
    /*
    * Xóa group
    * */
    private void deleteGroup(Update update, Long chatId){
        String[] parts = update.getMessage().getText().split("_");
        if (parts.length == 2) {
            String groupIdToRemove = parts[1];
            List<Group> list = GroupContainer.getGroupList();

            // Tìm phần tử cần xóa và xóa nó
            Optional<Group> groupToRemove = list.stream()
                    .filter(group -> groupIdToRemove.equals(group.getGroupId()))
                    .findFirst();

            if (groupToRemove.isPresent()) {
                Group removedGroup = groupToRemove.get();
                list.remove(removedGroup);
                GroupContainer.updateFileAfterRemoval();
                sendTextMessage(chatId, "Đã xóa Group có GroupId: " + groupIdToRemove);
            } else {
                sendTextMessage(chatId, "Không tìm thấy Group có GroupId: " + groupIdToRemove);
            }
        }
    }
    /*
    * kết thúc hàm delete
    * */
    private void xemCT(Update update, Long chatId){
        String[] parts = update.getMessage().getText().split("_");
        if (parts.length == 2) {
            String groupId = parts[1];
            List<Formula> formulas = FormulaContainer.getFormulasByGroup(groupId);

            if (!formulas.isEmpty()) {
                StringBuilder formulaMessage = new StringBuilder("Công thức:\n");
                int i=0;
                for (Formula formula : formulas) {
                    formulaMessage.append("CT"+ ++i).append(": ")
                            .append(formula.getFormula() + "_" + formula.getResult()).append("\n");
                }
                sendTextMessage(chatId, formulaMessage.toString());
            } else {
                sendTextMessage(chatId, "Hiện tại không có công thức nào cho nhóm này.");
            }
        } else {
            sendTextMessage(chatId, "Định dạng lệnh không hợp lệ.");
        }
    }
    /*
    * Chốt lãi
    * */
    private void chot(Update update, Long chatId){
        String[] parts = update.getMessage().getText().split("_");
        if (parts.length == 3) {
            String groupId = parts[1];
            int chotGame = Integer.parseInt(parts[2]);
            List<Group> lists = GroupContainer.getGroupList();
            boolean groupIdExists = lists.stream()
                    .anyMatch(group -> groupId.equals(group.getGroupId()) && chatId.equals(Long.valueOf(group.getAdminId())));
            if (groupIdExists) {
                ResultInt.setResultIntInProgress(Long.valueOf(groupId), chotGame);
            } else {
                sendTextMessage(chatId, "Không có quyền trong group này.");
            }
        }
    }

    private void xemChot(Update update, Long chatId) {
        String[] parts = update.getMessage().getText().split("_");
        if (parts.length == 2) {
            String groupId = parts[1];
            int chotGame = ResultInt.getResultIntInProgress(Long.valueOf(groupId));
            sendTextMessage(chatId, "Chốt lãi "+chotGame+" Game WIN");
        }
    }
    /*
    * định dạng gửi tin nhắn từ bot*/

    private void sendHelpMessage(Long chatId, boolean adminIdExists){
        StringBuilder messageBuilder = new StringBuilder();
        if(adminIdExists){
            messageBuilder.append("<b style=\"color: red;\">Chào</b> <b style=\"color: green;\">Mừng</b> <b style=\"color: blue;\">Bạn</b> <b style=\"color: orange;\">đến</b> <b style=\"color: darkorchid;\">với</b> <b style=\"color: gold;\">Bot đọc lệnh game</b>").append("\n").append("\n").append("\n")
                    .append("<b>Hướng dẫn sử dụng lệnh Bot!!</b>").append("\n")
                    .append("<b>/start_groupID lệnh này ngay lập tức sẽ kích hoạt đọc lệnh cho group</b>").append("\n")
                    .append("<b>/ketthuc_groupID lệnh này ngay lập tức sẽ tắt đọc lệnh cho group</b>").append("\n")
                    .append("<b>/Group</b> Xem thông tin các nhóm đã được kích hoạt mà tk ADM này quản lí").append("\n")
                    .append("<b>/chot_groupID_sogame</b> Cài đặt số game win để chốt lãi cho 1 group dựa vào groupID.").append("\n")
                    .append("Nếu muốn thay đổi số game chốt = cách nhập lại lệnh trên là được.").append("\n")
                    .append("<b>/addCT_groupID</b> Cài đặt công thức cho 1 group.").append("\n")
                    .append("Đợi có thông báo nhập công thức").append("\n")
                    .append("Định dạng công thức như sau: dk_kq. Ví dụ: dk: l-n-n kq: l. thì l-n-n_l").append("\n")
                    .append("Nếu nhiều công thức trong 1 tin nhắn thì mỗi công thức cần phải xuống dòng.").append("\n")
                    .append("Sau khi kết thúc thì chat /end để kết thúc quá trình (tránh sinh ra lỗi không mong muốn)").append("\n")
                    .append("<b>/xoaCT_groupID</b> Xóa công thức trình tự giống như thêm công thức (/endDelete khi hoàn tất quá trình.)").append("\n")
                    .append("<b>/xemCT_groupID</b> Lệnh này để xem các công thức đã setup cho 1 group").append("\n")
                    .append("<b>/setTime_groupID_HH:mm</b> Lệnh này để setup thời gian bắt đầu đọc lệnh cho 1 group.").append("\n")
                    .append("Theo giờ việt nam và định dạng 24h. bot sẽ báo ss trước 5p trong nhóm.").append("\n")
                    .append("Lưu ý <b>Lần đầu setup</b> khi thời gian hiện tại là 9:57 mà setup vào 10:00 thì nó sẽ bỏ qua thông báo ss trên nhóm.").append("\n")
                    .append("<b>/getTime_groupID</b> Xem thời gian đã cài đặt trên group").append("\n")
                    .append("<b>/xoaTime_groupID_HH:mm</b> Xóa thời gian đã cài đặt trên group. Nhập thời gian đúng với thời gian cần xóa").append("\n")
                    .append("<i style=\"color: orange;\">Lưu ý: groupID sẽ có dấu - phía trước không được bỏ đi</i>").append("\n").append("\n").append("\n")
                    .append("!======================!").append("\n").append("\n").append("\n").append("\n")
                    .append("<b>Hướng dẫn setup!!</b>").append("\n")
                    .append("<b>Bước 1</b>: Thêm IDBot vào nhóm chat cần đọc lệnh (cấp quyền admin và bật chế độ gửi ẩn danh cho IDBot)").append("\n")
                    .append("Đây là link bot: https://t.me/myidbot").append("\n")
                    .append("Sau khi thêm IDBot vào nhóm thì chat /getgroupid@myidbot để nhận được id của group.").append("\n")
                    .append("Tiếp tục chat /getid@myidbot để lấy Id của nick (nên là nick admin. Vì nick này sẽ tương tác với bot)").append("\n")
                    .append("<b>Bước 2</b>: Thêm Bot của chung tôi vào nhóm, và gửi Group ID, Your own ID và tên nhóm cho Admin kích hoạt").append("\n")
                    .append("Link Bot: cập nhật sau. (Vui lòng cấp quyền giống với IDBot)").append("\n")
                    .append("<b>Đã Xong Phần setup Bot. Đợi Admin Kích hoạt nhóm của bạn</b>");
            sendTextMessage(chatId, messageBuilder.toString());
        }else{
            messageBuilder.append("<b style=\"color: red;\">Chào</b> <b style=\"color: green;\">Mừng</b> <b style=\"color: blue;\">Bạn</b> <b style=\"color: orange;\">đến</b> <b style=\"color: darkorchid;\">với</b> <b style=\"color: gold;\">Bot đọc lệnh game</b>").append("\n").append("\n").append("\n")
                    .append("<b>Hướng dẫn setup!!</b>").append("\n")
                    .append("<b>Bước 1</b>: Thêm IDBot vào nhóm chat cần đọc lệnh (cấp quyền admin và bật chế độ gửi ẩn danh cho IDBot)").append("\n")
                    .append("Đây là link bot: https://t.me/myidbot").append("\n")
                    .append("Sau khi thêm IDBot vào nhóm thì chat /getgroupid@myidbot để nhận được id của group.").append("\n")
                    .append("Tiếp tục chat /getid@myidbot để lấy Id của nick (nên là nick admin. Vì nick này sẽ tương tác với bot)").append("\n")
                    .append("<b>Bước 2</b>: Thêm Bot của chung tôi vào nhóm, và gửi Group ID, Your own ID và tên nhóm cho Admin kích hoạt").append("\n")
                    .append("Link Bot: cập nhật sau. (Vui lòng cấp quyền giống với IDBot)").append("\n")
                    .append("<b>Đã Xong Phần setup Bot. Đợi Admin Kích hoạt nhóm của bạn</b>");
            sendTextMessage(chatId, messageBuilder.toString());
        }
    }
    public void sendTextMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.enableHtml(true);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            if (e instanceof TelegramApiRequestException) {
                TelegramApiRequestException telegramApiException = (TelegramApiRequestException) e;
                // Lấy mã lỗi từ ngoại lệ
                int errorCode = telegramApiException.getErrorCode();

                // Kiểm tra xem có phải là mã lỗi 403 không
                if (errorCode == 403) {
                    String groupId = String.valueOf(chatId);
                    List<Group> list = GroupContainer.getGroupList();
                    Optional<Group> groupToId = list.stream()
                            .filter(group -> groupId.equals(group.getGroupId()))
                            .findFirst();
                    Group group = groupToId.get();
                    TimeTask timeTask = TimeTaskInstance.getTimeTask(group);
                    if(timeTask != null){
                        timeTask.stop();
                        sendTextMessage(Long.valueOf(groupToId.get().getAdminId()), "Bot bị loại bỏ khỏi nhóm. Đã dừng");
                    }
                }
            }
        }
    }

    public void sendGroupListMessage(Long chatId) {
        List<Group> groupList = GroupContainer.getGroupList();

        if (ID.equals(chatId) || isChatIdAdmin(chatId, groupList)) {
            List<Group> filteredGroups = ID.equals(chatId) ? groupList : filterGroupsByAdminId(chatId, groupList);

            if (filteredGroups.isEmpty()) {
                sendTextMessage(chatId, "<b>Danh sách trống rỗng. 😔</b>");
                return;
            }

            StringBuilder messageBuilder = new StringBuilder("<b>Danh sách Admin và nhóm: 🌟</b>\n");
            for (Group group : filteredGroups) {
                messageBuilder.append("<b>Tên nhóm:</b> ").append(group.getNameGroup())
                        .append(", <b>Admin ID:</b> ").append(group.getAdminId())
                        .append(", <b>Group ID:</b> ").append(group.getGroupId())
                        .append("\n");
            }

            sendTextMessage(chatId, messageBuilder.toString());
        } else {
            sendTextMessage(chatId, "<b>Bạn không có quyền xem danh sách nhóm.</b>");
        }
    }
    /*
    * Kết thúc định dạng Message
    * */
    private boolean isChatIdAdmin(Long chatId, List<Group> groupList) {
        return groupList.stream().anyMatch(group -> chatId.equals(Long.valueOf(group.getAdminId())));
    }
    private List<Group> filterGroupsByAdminId(Long adminId, List<Group> groupList) {
        return groupList.stream().filter(group -> adminId.equals(Long.valueOf(group.getAdminId()))).collect(Collectors.toList());
    }
}
