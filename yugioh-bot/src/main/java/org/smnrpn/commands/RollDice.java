/*
 * This class is used to roll a die,
 * useful to choose who go first in a game.
 */


package org.smnrpn.commands;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDice;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class RollDice extends TelegramLongPollingBot {
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
            if (message.getText().equals("/roll")) {
                rollDice(id);
            }
        }
    }

    public void rollDice(Long id) {
        SendDice sendDice = new SendDice();
        sendDice.setChatId(id.toString());

        try {
            execute(sendDice);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
