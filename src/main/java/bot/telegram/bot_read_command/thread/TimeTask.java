package bot.telegram.bot_read_command.thread;

import bot.telegram.bot_read_command.Component.TimeTaskInstance;
import bot.telegram.bot_read_command.entity.Formula;
import bot.telegram.bot_read_command.entity.FormulaContainer;
import bot.telegram.bot_read_command.entity.Group;
import bot.telegram.bot_read_command.entity.ResultInt;
import bot.telegram.bot_read_command.payload.botTele.MyBot;
import bot.telegram.bot_read_command.utility.LoginWinGo;
import bot.telegram.bot_read_command.utility.WebDriverUtility;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TimeTask implements Runnable{

    @Autowired
    private MyBot myBot;

    private Group group;

    private Long followGroup = -1002114729246L;

    private volatile boolean isRunning = true;

    public void setGroup(Group group) {
        this.group = group;
    }

    int gameWin = 0;
    int gamePlay= 1;
    String formulaResult = null;
    boolean isCheck = false;
    int seconds = 0;

    private static boolean hasLoggedIn = false;
    private static final Object loginLock = new Object();

    @Override
    public void run() {
        try {
            int chotWin = ResultInt.getResultIntInProgress(Long.valueOf(group.getGroupId()));
            performTask(chotWin, gameWin, gamePlay, formulaResult, isCheck, seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void performTask(int chotWin, int gameWin, int gamePlay, String formulaResult, boolean isCheck, int seconds) throws InterruptedException {
        hasLoggedIn = false;
        try{
            WebDriver driver = WebDriverUtility.getDriver();
            WebDriverWait wait = WebDriverUtility.getWebDriverWait();
            while (isRunning) {
                //5 game win thì dừng.
                if(gameWin == chotWin){
                    stop();
                    myBot.sendTextMessage(Long.valueOf(group.getGroupId()), "\uD83C\uDFF5 Chốt lãi thành công "+gameWin+" Game WIN . \uD83C\uDF3E\uD83C\uDF3E\uD83C\uDF3E Lúa về rồi mọi người báo lãi lên nào \uD83E\uDD70 chờ ca tới tiếp tục nhé !!!");
                    myBot.sendTextMessage(Long.valueOf(group.getAdminId()), "Đã Xong "+group.getNameGroup());
                    return;
                }
                WebElement timeLeftElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("TimeLeft__C")));

                String timeText = timeLeftElement.findElement(By.className("TimeLeft__C-time")).getText();

                // Tách phút và giây từ đoạn text thời gian
                String[] timeParts = timeText.split(":");
                String secondsString = timeParts[1].replaceAll("\\D", "");

                seconds = Integer.parseInt(secondsString) == 0 ? 59 : Integer.parseInt(secondsString);
                int timeDoi = 0;
                //kiểm tra kết quả
                if(isCheck){
                    if(seconds <= 59){

                        //lấy kết quả đầu tiên của game
                        List<WebElement> recordElements = driver.findElements(By.className("GameRecord__C-body"));
                        Document doc = Jsoup.parse(recordElements.get(0).getAttribute("innerHTML"));
                        Elements divs = doc.getElementsByClass("van-col van-col--5");
                        String result = divs.get(0).text().equals("Nhỏ") ? "n" : "l";
                        if(formulaResult.equals(result)) {
                            isCheck = false;
                            ++gameWin;
                            gamePlay = 1;
                            myBot.sendTextMessage(Long.valueOf(group.getGroupId()), "HÚP "+ gameWin+" \uD83E\uDD17");
                            myBot.sendTextMessage(followGroup, group.getNameGroup() + " "+gameWin +" game win");
                        }else {
                            isCheck = false;
                            ++gamePlay;
                            int gameLost = gamePlay -1;
                            myBot.sendTextMessage(Long.valueOf(group.getGroupId()), "G "+ gameLost);
                            myBot.sendTextMessage(followGroup, group.getNameGroup() + " "+gameLost +" game lost");
                            if(gameLost == 8){
                                myBot.sendTextMessage(Long.valueOf(group.getGroupId()), "Ca này tạm giữ lại sàn \uD83D\uDE0E Không sao cả CG đã cố gắng rồi. Chuẩn bị vốn tốt để những ca sau chắc chắn sẽ lấy lại. Thắng không kiêu - Bại không nản \uD83E\uDD11\uD83E\uDD11\uD83E\uDD11");
                                myBot.sendTextMessage(Long.valueOf(group.getAdminId()), "Đã Xong "+group.getNameGroup());
                                stop();
                                return;
                            }
                        }
                        timeDoi = seconds - 49;
                        if(timeDoi > 0)
                            Thread.sleep(timeDoi*1000);
                        else
                            timeDoi = 0;
                    }
                }
                else if(seconds >= 30){
                    String finalString = getFinalString(driver);
                    Optional<Formula> isFormulaFound = FormulaContainer.findFormula(group.getGroupId(),finalString);
                    while (!isFormulaFound.isPresent() && finalString.length() > 1) {
                        finalString = finalString.substring(2);
                        isFormulaFound = FormulaContainer.findFormula(group.getGroupId(),finalString);
                    }
                    if (isFormulaFound.isPresent()) {
                        formulaResult = FormulaContainer.getResult(group.getGroupId(),finalString);
                        myBot.sendTextMessage(Long.valueOf(group.getGroupId()), (formulaResult.equals("n") ? "Nx"+ gamePlay: "Lx"+gamePlay));
                        isCheck = true;
                        Thread.sleep((seconds - timeDoi + 2)*1000);
                    } else {
                        myBot.sendTextMessage(Long.valueOf(group.getGroupId()), "Bỏ");
                        Thread.sleep((seconds - timeDoi + 11)*1000);
                    }
                }else {
                    Thread.sleep((seconds + 2)*1000);
                }
            }
        }catch (Exception e){
            System.out.println(e);
            if(e instanceof TimeoutException){
                synchronized (loginLock) {
                    if (!hasLoggedIn ) {
                        hasLoggedIn = true;
                        LoginWinGo.loginWinGo();
                    }
                }
                myBot.sendTextMessage(Long.valueOf(group.getAdminId()), "Đã mở lại "+group.getNameGroup());
                performTask(chotWin, gameWin, gamePlay, formulaResult, isCheck, seconds);
            }
            else if (e instanceof NoSuchSessionException) {
                System.out.println("Session is not valid. Trying to reinitialize WebDriver...");
                synchronized (loginLock) {
                    if (!hasLoggedIn ) {
                        hasLoggedIn = true;
                        LoginWinGo.loginWinGo();
                    }
                }
                myBot.sendTextMessage(Long.valueOf(group.getAdminId()), "Đã tạo lại WebDriver");
                performTask(chotWin, gameWin, gamePlay, formulaResult, isCheck, seconds);
            } else {
                myBot.sendTextMessage(Long.valueOf(group.getAdminId()), "Group: " + group.getNameGroup() + " bị lỗi: " + e.getMessage());
                stop();
            }
        }
    }

    private String getFinalString(WebDriver driver) {
        // Tạo danh sách để lưu trữ kết quả
        List<String> resultList = new ArrayList<>();
        // Lấy danh sách các thẻ con
        List<WebElement> recordElements = driver.findElements(By.className("GameRecord__C-body"));
        Document doc = Jsoup.parse(recordElements.get(0).getAttribute("innerHTML"));
        Elements divs = doc.getElementsByClass("van-col van-col--5");
        for (Element div : divs) {
            resultList.add(div.text());
        }

        // Đảo ngược danh sách
        List<String> reversedList = resultList.stream()
                .map(s -> s.equals("Nhỏ") ? "n" : "l")
                .collect(Collectors.toList());
        Collections.reverse(reversedList);

        // Ghép chuỗi
        return String.join("-", reversedList);
    }

    public void stop(){

        isRunning = false;
        TimeTaskInstance.removeTimeTask(group);
    }
}
