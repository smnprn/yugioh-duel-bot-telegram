/*
 * This class implements a command to search and retrieve
 * card information using the YGOPRODECK API.
 * More info at: https://ygoprodeck.com/api-guide/
 */

package org.smnrpn.database;

import org.smnrpn.cards.Card;
import org.smnrpn.controllers.HTTPHandler;
import org.smnrpn.controllers.ImagesDBHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import redis.clients.jedis.JedisPooled;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

public class CardDatabase extends TelegramLongPollingBot {
    private Card card;

    private HTTPHandler httpHandler = new HTTPHandler();
    private ImagesDBHandler database = new ImagesDBHandler();
    private JedisPooled checkDatabaseCommand = new JedisPooled("localhost", 6379);

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

        /*
         * If the user writes a command and the command is /database,
         * the variable checkCommand is set to true and the code below is executed.
         * Otherwise, checkCommand is set to false and nothing happens.
         */

        if (message.isCommand()) {
            if (message.getText().equals("/database")) {
                sendInstructions(id);
                checkDatabaseCommand.hset("checkDatabaseCommand", id.toString(), "true");
            }

            if (!message.getText().equals("/database")) {
                checkDatabaseCommand.hset("checkDatabaseCommand", id.toString(), "false");
            }
        }

        // Split the card name the user previously typed in order to build the endpoint URL.
        String[] messageComponents = message.getText().split(" ");

        /*
         * If the /database command was previously used and the user did not use any other command
         * the endpoint URL is built.
         */

        if (!message.isCommand() && checkDatabaseCommand.hget("checkDatabaseCommand", id.toString()).equals("true")) {
            httpHandler.createEndpoint(messageComponents);

            try {
                startSearch(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendCardInfo(Long id) {
        String cardInfo;

        CardInfo cardInfoMaker = new CardInfo(card);

        /*
         * Since different types of cards need to display different information
         * the following cases are used to distinguish between
         * link monsters, pendulum monsters, spell/traps and other monster cards.
         */

        cardInfo = switch (card.getType()) {
            case "Link Monster" -> cardInfoMaker.makeLink();
            case "Pendulum Effect Monster",
                 "Pendulum Normal Monster",
                 "Pendulum Effect Ritual Monster",
                 "Pendulum Flip Effect Monster",
                 "Pendulum Tuner Effect Monster",
                 "Pendulum Effect Fusion Monster" -> cardInfoMaker.makePendulum();
            default -> cardInfoMaker.makeMonster();
        };

        // If a card has no attribute is spell or a trap.
        if (card.getAttribute() == null) {
            cardInfo = cardInfoMaker.makeSpellTrap();
        }

        SendMessage sendCardMessage = new SendMessage();
        sendCardMessage.setChatId(id.toString());
        sendCardMessage.setParseMode("html");
        sendCardMessage.setText(cardInfo);

        try {
            execute(sendCardMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendCardImage(Long id) {
        File imageFile = new File(database.getImagePath(card.getId()));

        InputFile cardImage = new InputFile();
        cardImage.setMedia(imageFile);

        SendPhoto sendCardImage = new SendPhoto();
        sendCardImage.setChatId(id.toString());
        sendCardImage.setPhoto(cardImage);

        try {
            execute(sendCardImage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void startSearch(Long id) throws TelegramApiException {
        try {
            httpHandler.httpRequest();
            card = httpHandler.getCard();

            /*
             * If a card is already present in the database send the card image to the user,
             * otherwise download the image, add it to the database and then send it.
             */

            if (database.isPresent(card.getId())) {
                sendCardImage(id);
            } else {
                downloadImg();
                database.addCard(card.getId(), card.getName(), "yugioh-bot/assets/" + card.getId() + ".jpg");
                sendCardImage(id);
            }

            sendCardInfo(id);
        } catch (Exception e) {
            execute(httpHandler.sendErrorMessage(id));
            e.printStackTrace();
        }
    }

    /*
     * This method is used to send a message with the search instructions
     * when the user uses /database.
     */
    public void sendInstructions(Long id) {
        String searchInstructions = "Type a card name to search through the database (the name must be exact):";

        SendMessage sendSearchInstructions = new SendMessage();
        sendSearchInstructions.setChatId(id.toString());
        sendSearchInstructions.setText(searchInstructions);

        try {
            execute(sendSearchInstructions);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void downloadImg() {
        try {
            URL url = new URL(card.getImage_url_cropped());
            BufferedImage image = ImageIO.read(url);
            File file = new File("yugioh-bot/assets/" + card.getId() + ".jpg");

            ImageIO.write(image, "jpg", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
