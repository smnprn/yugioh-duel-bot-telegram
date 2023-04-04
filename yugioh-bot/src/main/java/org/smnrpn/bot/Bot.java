package org.smnrpn.bot;

import org.smnrpn.database.CardDatabase;
import org.smnrpn.global.GlobalCommands;
import org.smnrpn.lifepoints.LifePointsCounter;
import org.smnrpn.links.UsefulLinks;
import org.smnrpn.prices.Prices;
import org.smnrpn.roll.RollDice;
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
