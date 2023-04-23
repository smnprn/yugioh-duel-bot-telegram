/*
 * This class implements the global commands that Telegram
 * asks to support in every bot: /start and /help.
 */

package org.smnprn.commands;

import org.apache.log4j.Logger;
import org.smnprn.bot.InlineKeyboardCreator;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class GlobalCommands extends TelegramLongPollingBot {
    private final Logger logger = Logger.getLogger(GlobalCommands.class);

    private final String LINE_BREAK = "\n\n";

    @Override
    public String getBotUsername() {
        return "yugiohdueling_bot";
    }

    @Override
    public String getBotToken() {
        return System.getenv("YUGIOH_BOT_TOKEN");
    }

    @Override
    public void onUpdateReceived(Update update) {
        var message = update.getMessage();
        var user = message.getFrom();
        var id = user.getId();

        if (message.isCommand()) {
            if (message.getText().equals("/start")) {
                sendStartMessage(id);
            }

            if (message.getText().equals("/help")) {
                sendHelpMessage(id);
            }
        }
    }


    public void sendStartMessage(Long id) {
        String startMessage = "Welcome to the Yu-Gi-Oh! Duel Bot!" + LINE_BREAK +
                              "Use /help to show commands or report a bug." + LINE_BREAK +
                              "If you like this bot you can tip or contribute to the project using the buttons below.";

        SendMessage sendStartMessage = new SendMessage();
        sendStartMessage.setChatId(id.toString());
        sendStartMessage.setText(startMessage);

        sendStartMessage.setReplyMarkup(createStartInlineKeyboard());

        try {
            execute(sendStartMessage);
        } catch (TelegramApiException e) {
            logger.error(e.getMessage());
        }
    }

    public void sendHelpMessage(Long id) {
        String helpMessage = "<b>Use the button below if you experienced a bug!</b>" + LINE_BREAK +
                             "List of commands:" + LINE_BREAK +
                             "/start - Show a welcome message with links to the project's official page, GitHub and Ko-Fi." + LINE_BREAK +
                             "/database - Search in the card database. The name must be the exact name on the card and it's not case-sensitive." + LINE_BREAK +
                             "/links - Show links to many useful resources with a brief description." + LINE_BREAK +
                             "/roll - Roll a die to choose who go first." + LINE_BREAK +
                             "/lifepoints - Start a LP counter to use in your duels." + LINE_BREAK +
                             "To use the counter you must type <b>'me'</b>, <b>'op'</b> or <b>'both'</b> without quotes " +
                             "(to change your LP, your opponent's LP or both players' LP) " +
                             "followed by <b>+number_of_LP</b> or <b>-number_of_LP</b> (with no space between the symbol and the number).\n" +
                             "e.g. 'me -1500' or 'op +700'";

        SendMessage sendHelpMessage = new SendMessage();
        sendHelpMessage.setChatId(id.toString());
        sendHelpMessage.setParseMode("html");
        sendHelpMessage.setText(helpMessage);

        sendHelpMessage.setReplyMarkup(createHelpInlineKeyboard());

        try {
            execute(sendHelpMessage);
        } catch (TelegramApiException e) {
            logger.error(e.getMessage());
        }
    }

    public InlineKeyboardMarkup createStartInlineKeyboard() {
        InlineKeyboardCreator inlineKeyboardCreator = new InlineKeyboardCreator();

        inlineKeyboardCreator.createRows(2);

        inlineKeyboardCreator.createButton("Yu-Gi-Oh! Duel Bot official page", "https://github.com/smnprn", 1);
        inlineKeyboardCreator.createButton("GitHub  \uD83D\uDDA5", "https://github.com/smnprn/yugioh-duel-bot-telegram", 2);
        inlineKeyboardCreator.createButton("Donate \uD83D\uDCB0", "https://ko-fi.com/", 2);

        return inlineKeyboardCreator.getMarkupInline();
    }

    public InlineKeyboardMarkup createHelpInlineKeyboard() {
        InlineKeyboardCreator inlineKeyboardCreator = new InlineKeyboardCreator();

        inlineKeyboardCreator.createRows(1);

        inlineKeyboardCreator.createButton("Report a bug \uD83E\uDEB2", "https://github.com/smnprn/yugioh-duel-bot-telegram/issues", 1);

        return inlineKeyboardCreator.getMarkupInline();
    }
}
