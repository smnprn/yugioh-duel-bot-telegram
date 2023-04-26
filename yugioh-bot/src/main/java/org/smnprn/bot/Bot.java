package org.smnprn.bot;

import org.smnprn.commands.CardDatabase;
import org.smnprn.commands.GlobalCommands;
import org.smnprn.commands.LifePointsCounter;
import org.smnprn.commands.UsefulLinks;
import org.smnprn.commands.Prices;
import org.smnprn.commands.RollDice;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Bot extends TelegramLongPollingBot {
    private GlobalCommands globalCommands = new GlobalCommands();
    private UsefulLinks usefulLinks = new UsefulLinks();
    private RollDice rollDice = new RollDice();
    private LifePointsCounter lifePointsCounter = new LifePointsCounter();
    private CardDatabase cardDatabase = new CardDatabase();
    private Prices cardPrices = new Prices();

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
