package org.smnprn.commands;

import org.apache.log4j.Logger;
import org.smnprn.handlers.HTTPHandler;
import org.smnprn.cards.Card;
import org.smnprn.bot.InlineKeyboardCreator;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import redis.clients.jedis.JedisPooled;

import java.io.IOException;
import java.net.URISyntaxException;

public class Prices extends TelegramLongPollingBot {
    private final Logger logger = Logger.getLogger(Prices.class);

    private JedisPooled checkPricesCommand = new JedisPooled("localhost", 6379);
    private HTTPHandler httpHandler = new HTTPHandler();

    private Card card;
    String[] messageComponents = null;
    String LINE_BREAK = "\n\n";

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
            if (message.getText().equals("/prices")) {
                checkPricesCommand.hset("checkPricesCommand", id.toString(), "true");
                sendPriceMessage(id);
            }

            if (!message.getText().equals("/prices")) {
                checkPricesCommand.hset("checkPricesCommand", id.toString(), "false");
            }
        }

        messageComponents = message.getText().split(" ");

        if (!message.isCommand() && checkPricesCommand.hget("checkPricesCommand", id.toString()).equals("true")) {
            httpHandler.createEndpoint(messageComponents);

            try {
                httpHandler.httpRequest();
                card = httpHandler.getCard();

                if (card.getData() == null) {
                    cardNotFoundMessage(id);
                } else {
                    sendCardPrices(id);
                }
            } catch (TelegramApiException | URISyntaxException | IOException | InterruptedException e) {
                logger.error(e.getMessage());
            }
        }
    }

    public void sendCardPrices(Long id) throws TelegramApiException {
        String cardPricesInfo = displaySetsInfo() +
                                "___________________________________" + LINE_BREAK +
                                "Lowest price found across multiple versions of the card: " + LINE_BREAK +
                                "<b>Cardmarket:</b> " + card.getCardmarketPrices() + "€" + "\n" +
                                "<b>TCGPlayer:</b> " + card.getTCGPlayerPrices() + "$" + "\n" +
                                "<b>Ebay:</b> " + card.getEbayPrices() + "$" + "\n" +
                                "<b>Amazon:</b> " + card.getAmazonPrices() + "$" + "\n" +
                                "<b>CoolStuffInc:</b> " + card.getCoolstuffPrices() + "$";

        SendMessage sendCardPriceMessage = new SendMessage();
        sendCardPriceMessage.setChatId(id.toString());
        sendCardPriceMessage.setParseMode("html");
        sendCardPriceMessage.setText(cardPricesInfo);

        sendCardPriceMessage.setReplyMarkup(createMarketplacesKeyboard());

        try {
            execute(sendCardPriceMessage);
        } catch (TelegramApiException e) {
            logger.error(e.getMessage());
        }
    }

    public String displaySetsInfo() {
        String setInfo = "";

        /*
         * "Dark Magician" and "Blue-Eyes White Dragon" are the most famous and
         * most printed cards in the game. They have so many printings that is
         * necessary to limit how many the bot can show, otherwise the message
         * would break the Telegram maximum size.
         */
        if (card.getName().equalsIgnoreCase("Dark Magician") ||
            card.getName().equalsIgnoreCase("Blue-Eyes White Dragon")) {

            for (int i = 0; i < 54; i ++) {
                setInfo += "<i>" + card.getSetNames().get(i) + "</i>" + "\n" +
                        card.getSetCodes().get(i) + " - " + card.getSetRarities().get(i) + "\n" +
                        "<b>Price:</b> " + card.getSetPrices().get(i) + "$" + "\n\n";
            }

            return setInfo;
        }

        for (int i = 0; i < card.getSetNames().size(); i ++) {
            setInfo += "<i>" + card.getSetNames().get(i) + "</i>" + "\n" +
                        card.getSetCodes().get(i) + " - " + card.getSetRarities().get(i) + "\n" +
                        "<b>Price:</b> " + card.getSetPrices().get(i) + "$" + "\n\n";
        }

        return setInfo;
    }

    public void sendPriceMessage(Long id) {
        String priceMessage = "Type a card name to search its price (the name must be exact):" + LINE_BREAK +
                              "<i>Some prices may not be available!</i>";

        SendMessage sendPriceMessage = new SendMessage();
        sendPriceMessage.setChatId(id.toString());
        sendPriceMessage.setParseMode("html");
        sendPriceMessage.setText(priceMessage);

        try {
            execute(sendPriceMessage);
        } catch (TelegramApiException e) {
            logger.error(e.getMessage());
        }
    }

    public void cardNotFoundMessage(Long id) {
        try {
            execute(httpHandler.sendErrorMessage(id));
        } catch (TelegramApiException e) {
            logger.error(e.getMessage());
        }
    }

    public InlineKeyboardMarkup createMarketplacesKeyboard() {
        InlineKeyboardCreator inlineKeyboardCreator = new InlineKeyboardCreator();

        inlineKeyboardCreator.createRows(1);

        inlineKeyboardCreator.createButton("Cardmarket", "https://www.cardmarket.com/en/YuGiOh/Cards/" + httpHandler.cardmarketParameterGenerator(messageComponents), 1);
        inlineKeyboardCreator.createButton("TCGPlayer",
                                            "https://www.tcgplayer.com/search/yugioh/product?productName=" + httpHandler.tcgplayerParameterGenerator(messageComponents) + "&productLineName=yugioh&Language=English&view=grid",
                                            1);

        return inlineKeyboardCreator.getMarkupInline();
    }
}
