package org.smnrpn.bot;

import org.smnrpn.commands.CardDatabase;
import org.smnrpn.commands.GlobalCommands;
import org.smnrpn.commands.LifePointsCounter;
import org.smnrpn.commands.UsefulLinks;
import org.smnrpn.commands.Prices;
import org.smnrpn.commands.RollDice;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Bot extends TelegramLongPollingBot {
    GlobalCommands globalCommands = new GlobalCommands();
    UsefulLinks usefulLinks = new UsefulLinks();
    RollDice rollDice = new RollDice();
    LifePointsCounter lifePointsCounter = new LifePointsCounter();
    CardDatabase cardDatabase = new CardDatabase();
    Prices cardPrices = new Prices();

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
        globalCommands.onUpdateReceived(update);
        usefulLinks.onUpdateReceived(update);
        rollDice.onUpdateReceived(update);
        lifePointsCounter.onUpdateReceived(update);
        cardDatabase.onUpdateReceived(update);
        cardPrices.onUpdateReceived(update);
    }
}
