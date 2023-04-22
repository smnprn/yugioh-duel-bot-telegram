/*
 * This class implements a list of useful links for Yu-Gi-Oh! players.
 */

package org.smnrpn.commands;

import org.smnrpn.InlineKeyboardCreator;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class UsefulLinks extends TelegramLongPollingBot {
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
            if (message.getText().equals("/links")) {
                sendLinksMessage(id);
            }
        }
    }

    public void sendLinksMessage(Long id) {
        String LINE_BREAK = "\n\n";

        String startMessage = "Here's a list of useful links:" + LINE_BREAK +
                              "<b>Yu-Gi-Oh! Card Database</b>\n" + "<i>The official Yu-Gi-Oh! card database by Konami</i>\n" +
                              "To find all the information you need about every card in the game." + LINE_BREAK +
                              "<b>YGOPRODECK</b>\n" + "<i>A Yu-Gi-Oh! community</i>\n" +
                              "Here you can find many decklists, a pack opener simulator and other useful things." + LINE_BREAK +
                              "<b>Yugipedia</b>\n" + "<i>A fan-made wiki for Yu-Gi-Oh!</i>\n" +
                              "A free repository on every aspect of the franchise." + LINE_BREAK +
                              "<b>Cardmarket</b>\n" + "<i>A site to sell and buy cards</i>\n" +
                              "Europe's largest online marketplace for TCGs!" + LINE_BREAK +
                              "<b>TCGPlayer</b>\n" + "<i>Another site for sell and buy cards</i>\n" +
                              "The largest online TCGs marketplace in the US.";

        SendMessage sendLinksMessage = new SendMessage();
        sendLinksMessage.setChatId(id.toString());
        sendLinksMessage.setParseMode("html");
        sendLinksMessage.setText(startMessage);

        sendLinksMessage.setReplyMarkup(createInlineKeyboard());

        try {
            execute(sendLinksMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public InlineKeyboardMarkup createInlineKeyboard() {
        InlineKeyboardCreator inlineKeyboardCreator = new InlineKeyboardCreator();

        inlineKeyboardCreator.createRows(3);

        inlineKeyboardCreator.createButton("Official Yu-Gi-Oh! Card Database", "https://www.db.yugioh-card.com/yugiohdb/?request_locale=en", 1);
        inlineKeyboardCreator.createButton("YGOPRODECK", "https://ygoprodeck.com/", 2);
        inlineKeyboardCreator.createButton("Yugipedia", "https://yugipedia.com/wiki/Yugipedia", 2);
        inlineKeyboardCreator.createButton("Cardmarket", "https://www.cardmarket.com/en", 3);
        inlineKeyboardCreator.createButton("TCGPlayer", "https://www.tcgplayer.com/", 3);

        return  inlineKeyboardCreator.getMarkupInline();
    }
}
