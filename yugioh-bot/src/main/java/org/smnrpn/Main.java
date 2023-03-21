/*
 * Yu-Gi-Oh! Duel Bot
 * Copyright (c) 2023 Simone Perna
 *
 * This is a Telegram bot that helps Yu-Gi-Oh! players
 * with many useful functions.
 *
 * More info at: https://en.wikipedia.org/wiki/Yu-Gi-Oh!_Trading_Card_Game
 */

package org.smnrpn;

import org.smnrpn.bot.Bot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) throws TelegramApiException {
        org.apache.log4j.BasicConfigurator.configure(); // Necessary, otherwise an error is returner

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

        Bot bot = new Bot();

        botsApi.registerBot(bot);
    }
}