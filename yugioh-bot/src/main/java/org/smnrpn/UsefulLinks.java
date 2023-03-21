/*
 * This class implements a list of useful links for Yu-Gi-Oh! players.
 */

package org.smnrpn;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class UsefulLinks extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "yugiohdueling_bot";
    }

    @Override
    public String getBotToken() {
        return String TOKEN = System.getenv("YUGIOH_BOT_TOKEN");
        return TOKEN;
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
        String LINE_BREAK = "\n" + "\n";

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
            throw  new RuntimeException(e);
        }
    }

    public InlineKeyboardMarkup createInlineKeyboard() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup(); // Create the Inline Keyboard Markup
        List<List<InlineKeyboardButton>> inlineKeyboard = new ArrayList<>(); // Create the Inline Keyboard
        List<InlineKeyboardButton> keyboardButtonsFirstRow = new ArrayList<>(); // Create a row of buttons
        List<InlineKeyboardButton> keyboardButtonsSecondRow = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsThirdRow = new ArrayList<>();

        // Create buttons
        InlineKeyboardButton yugiohDB = new InlineKeyboardButton("Official Yu-Gi-Oh! Card Database");
        yugiohDB.setUrl("https://www.db.yugioh-card.com/yugiohdb/?request_locale=en");

        InlineKeyboardButton ygoProDeck = new InlineKeyboardButton("YGOPRODECK");
        ygoProDeck.setUrl("https://ygoprodeck.com/");

        InlineKeyboardButton yugipedia = new InlineKeyboardButton("Yugipedia");
        yugipedia.setUrl("https://yugipedia.com/wiki/Yugipedia");

        InlineKeyboardButton cardmarket = new InlineKeyboardButton("Cardmarket");
        cardmarket.setUrl("https://www.cardmarket.com/en");

        InlineKeyboardButton tcgPlayer = new InlineKeyboardButton("TCGPlayer");
        tcgPlayer.setUrl("https://www.tcgplayer.com/");

        markupInline.setKeyboard(inlineKeyboard); // Add the keyboard to the markup

        // Add the rows to the keyboard
        inlineKeyboard.add(keyboardButtonsFirstRow);
        inlineKeyboard.add(keyboardButtonsSecondRow);
        inlineKeyboard.add(keyboardButtonsThirdRow);

        // Add the buttons to the row
        keyboardButtonsFirstRow.add(yugiohDB);
        keyboardButtonsSecondRow.add(ygoProDeck);
        keyboardButtonsSecondRow.add(yugipedia);
        keyboardButtonsThirdRow.add(cardmarket);
        keyboardButtonsThirdRow.add(tcgPlayer);

        return markupInline;
    }
}
