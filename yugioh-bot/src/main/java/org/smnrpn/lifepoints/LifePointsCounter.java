/*
 * This class implements a life points counter.
 * More info at: https://yugioh.fandom.com/wiki/LP
 */

package org.smnrpn.lifepoints;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class LifePointsCounter extends TelegramLongPollingBot {
    private int userLP = 8000;
    private int opponentLP = 8000;

    private int timesCounterDisplayed = 0; // Counts how many times the LP are displayed
    private int checkCommand = 0;  // Checks if the /lifepoints command was already called

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
            if (message.getText().equals("/lifepoints")) {
                checkCommand = 1;
            }

            /*
             * If the user sends a command different from /lifepoints:
             * reset the counter.
             */

            if (!message.getText().equals("/lifepoints")) {
                resetCounter();
            }
        }

        /*
         * The user input is split in two, the first part contains "me", "op" or "both"
         * to decide where subtract the LP. The second part contains the number
         * of LP to be subtracted.
         */

        String[] messageComponents = message.getText().split(" ");

        if (checkCommand == 1) {                // If the user used /lifepoints...
            if (timesCounterDisplayed  > 0) {   //...and the LP counter was already displayed at least once

                /*
                 * There's a chance the user won't input a valid command,
                 * so it's necessary to catch the exception and return
                 * an error message.
                 */

                try {
                    int value = Integer.valueOf(messageComponents[1]);
                    updateLP(value, messageComponents);
                } catch (Exception e) {
                    if (message.isCommand()) {
                        System.out.println("New game");
                    } else {
                        sendErrorMessage(id);
                    }
                }

            }

            displayLP(id);
            timesCounterDisplayed++;
        }
    }


    public void displayLP(Long id) {
        SendMessage sendMessage = new SendMessage();

        String printLP = "Your LP: " + userLP + "\n"
                       + "Opponent LP: " + opponentLP + "\n" + "\n";

        if (userLP > 0 && opponentLP > 0) {
            sendMessage.setChatId(id.toString());
            sendMessage.setText(printLP);
        } else if (userLP == 0 && opponentLP > 0) {
            sendMessage.setChatId(id.toString());
            sendMessage.setText(printLP + "Your opponent wins the duel!");

            resetCounter();
        } else if (opponentLP == 0 && userLP > 0) {
            sendMessage.setChatId(id.toString());
            sendMessage.setText(printLP + "You win the duel!");

            resetCounter();
        } else if (userLP == 0 && opponentLP == 0){
            sendMessage.setChatId(id.toString());
            sendMessage.setText(printLP + "The duel is a tie");

            resetCounter();
        } else if (userLP < 0 && opponentLP > 0) {
            userLP = 0;
            printLP = "Your LP: " + userLP + "\n"
                    + "Opponent LP: " + opponentLP + "\n" + "\n";

            sendMessage.setChatId(id.toString());
            sendMessage.setText(printLP + "Your opponent wins the duel!");

            resetCounter();
        } else if (userLP < 0 && opponentLP == 0) {
            userLP = 0;
            printLP = "Your LP: " + userLP + "\n"
                    + "Opponent LP: " + opponentLP + "\n" + "\n";

            sendMessage.setChatId(id.toString());
            sendMessage.setText(printLP + "The duel is tie!");

            resetCounter();
        } else if (userLP > 0 && opponentLP < 0) {
            opponentLP = 0;
            printLP = "Your LP: " + userLP + "\n"
                    + "Opponent LP: " + opponentLP + "\n" + "\n";

            sendMessage.setChatId(id.toString());
            sendMessage.setText(printLP + "You win the duel!");

            resetCounter();
        } else if (userLP == 0 && opponentLP < 0) {
            opponentLP = 0;
            printLP = "Your LP: " + userLP + "\n"
                    + "Opponent LP: " + opponentLP + "\n" + "\n";

            sendMessage.setChatId(id.toString());
            sendMessage.setText(printLP + "The duel is tie!");

            resetCounter();
        } else if (userLP < 0 && opponentLP < 0) {
            userLP = 0;
            opponentLP = 0;
            printLP = "Your LP: " + userLP + "\n"
                    + "Opponent LP: " + opponentLP + "\n" + "\n";

            sendMessage.setChatId(id.toString());
            sendMessage.setText(printLP + "The duel is tie!");

            resetCounter();
        }

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw  new RuntimeException(e);
        }
    }

    public void updateLP(int variation, String[] playerAndValue) {
        if (playerAndValue[0].equalsIgnoreCase("me")) {
            userLP += variation;
        }

        if (playerAndValue[0].equalsIgnoreCase("op")) {
            opponentLP += variation;
        }

        if (playerAndValue[0].equalsIgnoreCase("both")) {
            userLP += variation;
            opponentLP += variation;
        }
    }

    public void resetCounter() {
        checkCommand = 0;
        timesCounterDisplayed = 0;
        userLP = 8000;
        opponentLP = 8000;
    }

    public void sendErrorMessage(Long id) {
        SendMessage sendErrorMessage = new SendMessage(id.toString(), "Not a valid input, try again!\n" +
                                                                            "If you need, use /help");

        try {
            execute(sendErrorMessage);
        } catch (TelegramApiException e) {
            throw  new RuntimeException(e);
        }
    }
}
