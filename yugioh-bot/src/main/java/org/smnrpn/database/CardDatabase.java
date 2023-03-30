/*
 * This class implements a command to search and retrieve
 * card information using the YGOPRODECK API.
 * More info at: https://ygoprodeck.com/api-guide/
 */

package org.smnrpn.database;

import com.google.gson.Gson;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CardDatabase extends TelegramLongPollingBot {
    private String ENDPOINT;  // This is the actual endpoint used to retrieve data.
    private Card card;
    private int checkCommand = 0;  // This variable check if the user called the /database command.

    PostgresDBHandler database = new PostgresDBHandler();

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
         * the variable checkCommand is set to 1 and the code below is executed.
         * Otherwise, checkCommand is set again to 0 and nothing happens.
         */

        if (message.isCommand()) {
            if (message.getText().equals("/database")) {
                sendSearchInstructions(id);
                checkCommand = 1;
            }

            if (!message.getText().equals("/database")) {
                checkCommand = 0;
            }
        }

        // Split the card name the user previously typed in order to build the endpoint URL.
        String[] messageComponents = message.getText().split(" ");

        /*
         * If the /database command was previously used and the user did not use any other command
         * the endpoint URL is built.
         * 1) If the card name is just one word the URL is https://db.ygoprodeck.com/api/v7/cardinfo.php?name=MONSTER_NAME
         * 2) If the card name has more than a word the parameterGenerator() method is needed.
         */

        if (!message.isCommand() && checkCommand == 1) {
            // This is the base Card Info endpoint. You can pass parameters to this endpoint to filter the info retrieved.
            String CARD_INFO_ENDPOINT = "https://db.ygoprodeck.com/api/v7/cardinfo.php";

            if (messageComponents.length == 1) {
                ENDPOINT = CARD_INFO_ENDPOINT + "?name=" + messageComponents[0];
            } else {
                ENDPOINT = CARD_INFO_ENDPOINT + "?name=" + parameterGenerator(messageComponents);
            }

            if (!message.getText().isEmpty()) {
                try {
                    startSearch(id, ENDPOINT);
                } catch (Exception e) {
                    sendErrorMessage(id);
                    e.printStackTrace();
                }
            }
        }
    }

    /*
     * This method sends a GET request to the endpoint and retrieves the information
     * inside the JSON file sent as response.
     */
    public void httpRequest(String ENDPOINT) throws URISyntaxException, IOException, InterruptedException {
        Gson gson = new Gson();

        HttpRequest getRequest = HttpRequest.newBuilder()
            .uri(new URI(ENDPOINT))
            .GET()
            .build();

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

        card = gson.fromJson(getResponse.body(), Card.class);
    }

    /*
     * This method is used inside the startSearch() method to build and send a message
     * to the user with the card info retrieved.
     */
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
            throw new RuntimeException(e);
        }
    }

    /*
     * This method is used inside the startSearch() method
     * to send the card image to the user.
     */
    public void sendCardImage(Long id) throws IOException {
        File imageFile = new File(database.getImagePath(card.getId()));

        InputFile cardImage = new InputFile();
        cardImage.setMedia(imageFile);

        SendPhoto sendCardImage = new SendPhoto();
        sendCardImage.setChatId(id.toString());
        sendCardImage.setPhoto(cardImage);

        try {
            execute(sendCardImage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void startSearch(Long id, String endpoint) {
        try {
            httpRequest(endpoint);

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
        } catch (URISyntaxException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * This method is used to send a message with the search instructions
     * when the user uses /database.
     */
    public void sendSearchInstructions(Long id) {
        String searchInstructions = "Type a card name to search through the database (the name must be exact):";

        SendMessage sendSearchInstructions = new SendMessage();
        sendSearchInstructions.setChatId(id.toString());
        sendSearchInstructions.setText(searchInstructions);

        try {
            execute(sendSearchInstructions);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * This method generates the final part of the endpoint URL
     * creating a String with "%20" instead of spaces.
     */
    public String parameterGenerator(String[] messageComponents) {
        StringBuilder parameter = new StringBuilder();

        if (messageComponents.length > 1) {
            for (int i = 0; i < messageComponents.length - 1; i++) {
                parameter.append(messageComponents[i]).append("%20");
            }
        }

        parameter.append(messageComponents[messageComponents.length - 1]);

        /*
         * Some cards contain double quotes in their name (e.g. Contact "C"),
         * therefore it's necessary to replace those quotes with their percent-encoding equivalent (%22)
         * in order to avoid a URISyntaxException.
         */

        if (parameter.toString().contains("\"")) {
            return parameter.toString().replace("\"", "%22");
        }

        return parameter.toString();
    }

    public void sendErrorMessage(Long id) {
        String errorMessage = "Card not found! Make sure you searched the exact name on the card.";

        SendMessage sendCardMessage = new SendMessage();
        sendCardMessage.setChatId(id.toString());
        sendCardMessage.setParseMode("html");
        sendCardMessage.setText(errorMessage);

        try {
            execute(sendCardMessage);
        } catch (Exception e) {
            System.out.println("Error in CardDatabase.sendCardMessage()");
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
