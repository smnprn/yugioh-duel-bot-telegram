/*
 * This class implements a life points counter.
 * More info at: https://yugioh.fandom.com/wiki/LP
 */

package org.smnrpn.commands;

import org.smnrpn.handlers.LifePointsDBHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import redis.clients.jedis.JedisPooled;

public class LifePointsCounter extends TelegramLongPollingBot {
    private LifePointsDBHandler lifePointsHandler = new LifePointsDBHandler();

    private JedisPooled checkLPCommand = new JedisPooled("localhost", 6379);
    private JedisPooled wasCounterDisplayed = new JedisPooled("localhost", 6379);


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
                checkLPCommand.hset("checkLPCommand", id.toString(), "true");
                wasCounterDisplayed.hset("wasCounterDisplayed", id.toString(), "false");

                if (!lifePointsHandler.isPresent(id)) {
                    lifePointsHandler.addUser(id);
                } else {
                    lifePointsHandler.setUserLP(id, 8000);
                    lifePointsHandler.setOpponentLP(id, 8000);
                }
            }

            /*
             * If the user sends a command different from /lifepoints:
             * reset the counter.
             */

            if (!message.getText().equals("/lifepoints")) {
                resetCounter(id);
            }
        }

        /*
         * The user input is split in two, the first part contains "me", "op" or "both"
         * to decide where subtract the LP. The second part contains the number
         * of LP to be subtracted.
         */

        String[] messageComponents = message.getText().split(" ");

        if (checkLPCommand.hget("checkLPCommand", id.toString()).equals("true")) {                     // If the user used /lifepoints...
            if (wasCounterDisplayed.hget("wasCounterDisplayed", id.toString()).equals("true")) {     //...and the LP counter was already displayed at least once

                /*
                 * There's a chance the user won't input a valid command in two circumstances:
                 * the second word in the message is not a number, so it produces a NumberFormatException
                 * or if the message is longer than two words.
                 * In both cases the bot sends and error message to the user.
                 */

                int value = 0;
                boolean exceptionCaught = false;

                try {
                    value = Integer.parseInt(messageComponents[1]);
                } catch (NumberFormatException e) {
                    sendErrorMessage(id);
                    exceptionCaught = true;
                } finally {
                    if (messageComponents.length == 2 || exceptionCaught) {
                        updateLP(id, value, messageComponents);
                    } else {
                        sendErrorMessage(id);
                    }
                }
            }

            displayLP(id);
            wasCounterDisplayed.hset("wasCounterDisplayed", id.toString(), "true");
        }
    }


    public void displayLP(Long id) {
        SendMessage sendMessage = new SendMessage();

        String printLP = "Your LP: " + lifePointsHandler.getUserLP(id) + "\n"
                       + "Opponent LP: " + lifePointsHandler.getOpponentLP(id) + "\n" + "\n";

        if (lifePointsHandler.getUserLP(id) > 0 && lifePointsHandler.getOpponentLP(id) > 0) {
            sendMessage.setChatId(id.toString());
            sendMessage.setText(printLP);
        } else if (lifePointsHandler.getUserLP(id) == 0 && lifePointsHandler.getOpponentLP(id) > 0) {
            sendMessage.setChatId(id.toString());
            sendMessage.setText(printLP + "Your opponent wins the duel!");

            resetCounter(id);
        } else if (lifePointsHandler.getOpponentLP(id) == 0 && lifePointsHandler.getUserLP(id) > 0) {
            sendMessage.setChatId(id.toString());
            sendMessage.setText(printLP + "You win the duel!");

            resetCounter(id);
        } else if (lifePointsHandler.getUserLP(id) == 0 && lifePointsHandler.getOpponentLP(id) == 0){
            sendMessage.setChatId(id.toString());
            sendMessage.setText(printLP + "The duel is a tie");

            resetCounter(id);
        } else if (lifePointsHandler.getUserLP(id) < 0 && lifePointsHandler.getOpponentLP(id) > 0) {
            lifePointsHandler.setUserLP(id, 0);
            printLP = "Your LP: " + lifePointsHandler.getUserLP(id) + "\n"
                    + "Opponent LP: " + lifePointsHandler.getOpponentLP(id) + "\n" + "\n";

            sendMessage.setChatId(id.toString());
            sendMessage.setText(printLP + "Your opponent wins the duel!");

            resetCounter(id);
        } else if (lifePointsHandler.getUserLP(id) < 0 && lifePointsHandler.getOpponentLP(id) == 0) {
            lifePointsHandler.setUserLP(id, 0);
            printLP = "Your LP: " + lifePointsHandler.getUserLP(id) + "\n"
                    + "Opponent LP: " + lifePointsHandler.getOpponentLP(id) + "\n" + "\n";

            sendMessage.setChatId(id.toString());
            sendMessage.setText(printLP + "The duel is tie!");

            resetCounter(id);
        } else if (lifePointsHandler.getUserLP(id) > 0 && lifePointsHandler.getOpponentLP(id) < 0) {
            lifePointsHandler.setOpponentLP(id, 0);
            printLP = "Your LP: " + lifePointsHandler.getUserLP(id) + "\n"
                    + "Opponent LP: " + lifePointsHandler.getOpponentLP(id) + "\n" + "\n";

            sendMessage.setChatId(id.toString());
            sendMessage.setText(printLP + "You win the duel!");

            resetCounter(id);
        } else if (lifePointsHandler.getUserLP(id) == 0 && lifePointsHandler.getOpponentLP(id) < 0) {
            lifePointsHandler.setOpponentLP(id, 0);
            printLP = "Your LP: " + lifePointsHandler.getUserLP(id) + "\n"
                    + "Opponent LP: " + lifePointsHandler.getOpponentLP(id) + "\n" + "\n";

            sendMessage.setChatId(id.toString());
            sendMessage.setText(printLP + "The duel is tie!");

            resetCounter(id);
        } else if (lifePointsHandler.getUserLP(id) < 0 && lifePointsHandler.getOpponentLP(id) < 0) {
            lifePointsHandler.setUserLP(id, 0);
            lifePointsHandler.setOpponentLP(id, 0);
            printLP = "Your LP: " + lifePointsHandler.getUserLP(id) + "\n"
                    + "Opponent LP: " + lifePointsHandler.getOpponentLP(id) + "\n" + "\n";

            sendMessage.setChatId(id.toString());
            sendMessage.setText(printLP + "The duel is tie!");

            resetCounter(id);
        }

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void updateLP(Long id, int variation, String[] playerAndValue) {
        if (playerAndValue[0].equalsIgnoreCase("me")) {
            lifePointsHandler.updateUserLP(id, variation);
        }

        if (playerAndValue[0].equalsIgnoreCase("op")) {
            lifePointsHandler.updateOpponentLP(id, variation);
        }

        if (playerAndValue[0].equalsIgnoreCase("both")) {
            lifePointsHandler.updateUserLP(id, variation);
            lifePointsHandler.updateOpponentLP(id, variation);
        }
    }

    public void resetCounter(Long id) {
        checkLPCommand.hset("checkLPCommand", id.toString(), "false");
        wasCounterDisplayed.hset("wasCounterDisplayed", id.toString(), "false");
        lifePointsHandler.setUserLP(id, 8000);
        lifePointsHandler.setOpponentLP(id, 8000);
    }

    public void sendErrorMessage(Long id) {
        SendMessage sendErrorMessage = new SendMessage(id.toString(), "Not a valid input, try again!\n" + "If you need, use /help");

        try {
            execute(sendErrorMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
