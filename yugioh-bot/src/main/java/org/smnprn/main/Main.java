/*
 * Yu-Gi-Oh! Duel Bot
 * Copyright (c) 2023 Simone Perna
 *
 * This is a Telegram bot that helps Yu-Gi-Oh! players
 * with many useful functions.
 *
 * More info at: https://en.wikipedia.org/wiki/Yu-Gi-Oh!_Trading_Card_Game
 */

package org.smnprn.main;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.smnprn.bot.Bot;
import org.smnprn.log.LogSetup;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) throws TelegramApiException {
        LogSetup log = new LogSetup();
        log.configureLog();

        Logger logger = Logger.getLogger(Main.class);

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

        Bot bot = new Bot();

        botsApi.registerBot(bot);

        logger.info("Bot started successfully!");
    }
}
