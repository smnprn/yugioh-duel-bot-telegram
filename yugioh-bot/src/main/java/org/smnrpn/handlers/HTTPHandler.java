package org.smnrpn.handlers;

/*
 * This class handles http requests, generates parameters for the endpoints
 * and create an error message if the query can't find a card.
 */

import com.google.gson.Gson;
import org.smnrpn.cards.Card;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HTTPHandler {
    private Card card;
    // This is the actual endpoint used to retrieve data.
    String ENDPOINT;

    public void httpRequest() throws URISyntaxException, IOException, InterruptedException {
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
     * This method generates the final part of the endpoint URL
     * creating a String with "%20" instead of spaces.
     */
    public String apiParameterGenerator(String[] messageComponents) {
        StringBuilder parameter = new StringBuilder();

        if (messageComponents.length > 1) {
            for (int i = 0; i < messageComponents.length - 1; i++) {
                parameter.append(messageComponents[i]).append("%20");
            }
        }

        parameter.append(messageComponents[messageComponents.length - 1]);

        /*
         * Some cards contain double quotes in their name (e.g. Maxx "C"),
         * therefore it's necessary to replace those quotes with their percent-encoding equivalent (%22)
         * in order to avoid a URISyntaxException.
         */

        if (parameter.toString().contains("\"")) {
            return parameter.toString().replace("\"", "%22");
        }

        return parameter.toString();
    }

    public String cardmarketParameterGenerator(String[] messageComponents) {
        StringBuilder parameter = new StringBuilder();

        if (messageComponents.length > 1) {
            for (int i = 0; i < messageComponents.length - 1; i++) {
                parameter.append(messageComponents[i]).append("-");
            }
        }

        parameter.append(messageComponents[messageComponents.length - 1]);

        if (parameter.toString().contains("\"")) {
            return parameter.toString().replace("\"", "");
        }

        // Cardmarket URLs can't contain columns, hyphens or commas
        if (parameter.toString().contains(":")) {
            return parameter.toString().replace(":", "");
        }

        if (parameter.toString().contains("--")) {
            return parameter.toString().replace("--", "");
        }

        if (parameter.toString().contains(",")) {
            return parameter.toString().replace(",", "");
        }

        return parameter.toString();
    }

    public String tcgplayerParameterGenerator(String [] messageComponents) {
        StringBuilder parameter = new StringBuilder();

        if (messageComponents.length > 1) {
            for (int i = 0; i < messageComponents.length - 1; i++) {
                parameter.append(messageComponents[i]).append("+");
            }
        }

        parameter.append(messageComponents[messageComponents.length - 1]);

        if (parameter.toString().contains("\"")) {
            return parameter.toString().replace("\"", "%22");
        }

        return parameter.toString();
    }

    public void createEndpoint(String [] messageComponents) {
        // This is the base Card Info endpoint. You can pass parameters to this endpoint to filter the info retrieved.
        String CARD_INFO_ENDPOINT = "https://db.ygoprodeck.com/api/v7/cardinfo.php";

        /*
        * 1) If the card name is just one word the URL is https://db.ygoprodeck.com/api/v7/cardinfo.php?name=MONSTER_NAME
        * 2) If the card name has more than a word the apiParameterGenerator() method is needed.
        */
        if (messageComponents.length == 1) {
            ENDPOINT = CARD_INFO_ENDPOINT + "?name=" + messageComponents[0];
        } else {
            ENDPOINT = CARD_INFO_ENDPOINT + "?name=" + apiParameterGenerator(messageComponents);
        }
    }

    public SendMessage sendErrorMessage(Long id) {
        String errorMessage = "Card not found! Make sure you searched the exact name on the card.";

        SendMessage sendCardMessage = new SendMessage();
        sendCardMessage.setChatId(id.toString());
        sendCardMessage.setParseMode("html");
        sendCardMessage.setText(errorMessage);

        return sendCardMessage;
    }

    public Card getCard() {
        return card;
    }

    public String getENDPOINT() {
        return ENDPOINT;
    }
}
