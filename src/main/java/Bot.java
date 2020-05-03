import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
private final String rules = "Привет! Это бот для заучивание слов. \n " +
        "Сперва тебе необходимо написать сообщение с парой слов в формате \"Слово на другом языке - перевод \". \n" +
        "Когда ты запишешь все слова, которые тебе нужны, напиши мне \"Заучивать\", и я сразу дам тебе перевод одного из слов, а ты должен будешь написать его правильно. \n" +
        "Я отвечу прав ли ты или нет и потом дам следующее слово и так далее. Когда ты выучишь все слова просто напиши \"Конец\". \n" +
        "Так же ты можешь сохранить новые слова на будущее, нужно перед концом написать мне \"Сохранить все слова\"\n" +
        "Если захочешь повторить все свои слова, то напиши мне \"Добавить прошлые слова\"\n" +
        "Для удаления слов из памяти, напиши мне \"Удалить слово(которые нужно удалить)\"\n" +
        "Удачной учебы!";
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }


    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            setButtons(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("Как пользоваться"));
        keyboardFirstRow.add(new KeyboardButton("Заучивание"));
        keyboardFirstRow.add(new KeyboardButton("Конец"));
        keyboardFirstRow.add(new KeyboardButton("Сохранить все слова"));
        keyboardFirstRow.add(new KeyboardButton("Добавить прошлые слова"));
        keyboardFirstRow.add(new KeyboardButton("Вывести список слов"));


        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            String messageText = message.getText();
            if(messageText.equals("Как пользоваться")){
                sendMsg(message,rules);
            }
            else if(messageText.startsWith("Удалить")){
                sendMsg(message,"Удалил");
                try {
                    WorkWithDb.deleteWord(messageText);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            else if(messageText.equals("Сохранить все слова")){
                sendMsg(message,"Сохранил на будущее");
                try {
                    WorkWithDb.insertInDB();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            else if(messageText.equals("Добавить прошлые слова")){
                sendMsg(message,"Готово, можешь повторить все слова");
                try {
                    WorkWithDb.InsertWordsOfDbInMap();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(messageText.equals("Заучивание")){
                    sendMsg(message,"Начали");
                    sendMsg(message, Quizlet.randomWord());
            }
            else if(messageText.equals(Quizlet.messageEquals(messageText))){
                if(Quizlet.checkWords(messageText)){
                    sendMsg(message, "Правильно");
                }else{
                    sendMsg(message,"Неправильно");
                }
                sendMsg(message, Quizlet.randomWord());
            }
            else if(messageText.equals("Конец")){
                sendMsg(message,"Закончили. Хорошо поработал!");
                Quizlet.getWords().clear();
                Quizlet.getKeys().clear();
            }
            else if(messageText.equals("Вывести список слов")){
                sendMsg(message,Quizlet.showWords());

            }
            else if(messageText.indexOf(" - ") != -1) {
                try {
                    sendMsg(message,Quizlet.createWordsList(message.getText()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                sendMsg(message,"Ты написал слово неправильно");
                sendMsg(message, Quizlet.randomWord());
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "English Bot";
    }

    @Override
    public String getBotToken() {
        return "1246301877:AAEM6Umf2ANPjYRAHvMgrd4gP2OzU5LUJkw";
    }
}
