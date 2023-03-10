package org.smnrpn;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Bot extends TelegramLongPollingBot {
    GlobalCommands globalCommands = new GlobalCommands();
    UsefulLinks usefulLinks = new UsefulLinks();
    RollDice rollDice = new RollDice();
    LifePointsCounter lifePointsCounter = new LifePointsCounter();

    @Override
    public String getBotUsername() {
        return "yugiohdueling_bot";
    }

    @Override
    public String getBotToken() {
        String TOKEN = System.getenv("YUGIOH_BOT_TOKEN");
        return TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        globalCommands.onUpdateReceived(update);
        usefulLinks.onUpdateReceived(update);
        rollDice.onUpdateReceived(update);
        lifePointsCounter.onUpdateReceived(update);
    }
}
