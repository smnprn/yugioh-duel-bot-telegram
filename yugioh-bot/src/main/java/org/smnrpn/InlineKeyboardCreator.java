package org.smnrpn;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboardCreator {
    InlineKeyboardMarkup markupInline;
    List<List<InlineKeyboardButton>> inlineKeyboard;

    public InlineKeyboardCreator() {
        this.markupInline = new InlineKeyboardMarkup(); // Create the Inline Keyboard Markup
        this.inlineKeyboard = new ArrayList<>(); // Create the Inline Keyboard;

        markupInline.setKeyboard(inlineKeyboard);
    }

    public void createRows(int quantity) {
        for (int i = 0; i < quantity; i++) {
            List<InlineKeyboardButton> keyboardButtonRow = new ArrayList<>(); // Create a row of buttons
            inlineKeyboard.add(keyboardButtonRow);
        }
    }

    public void createButton(String text, String url, int row) {
        InlineKeyboardButton button = new InlineKeyboardButton(text);
        button.setUrl(url);

        inlineKeyboard.get(row).add(button);
    }

    public InlineKeyboardMarkup getMarkupInline() {
        return markupInline;
    }
}
