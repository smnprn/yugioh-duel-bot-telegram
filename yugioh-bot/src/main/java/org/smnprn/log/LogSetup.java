package org.smnprn.log;

import org.apache.log4j.*;

public class LogSetup {
    ConsoleAppender consoleAppender;
    FileAppender fileAppender;

    public void createConsoleAppender() {
        consoleAppender = new ConsoleAppender();
        consoleAppender.setThreshold(Level.DEBUG);
        consoleAppender.setLayout(new PatternLayout("%d [%p | %c | %C{1}] %m%n"));
        consoleAppender.activateOptions();
    }

    public void createFileAppender() {
        fileAppender = new FileAppender();
        fileAppender.setThreshold(Level.WARN);
        fileAppender.setLayout(new PatternLayout("%d [%p | %c | %C{1}] %m%n"));
        fileAppender.setFile("src/main/java/org/smnprn/log/files/bot_log.txt");
        fileAppender.activateOptions();
    }

    public void addAppenders() {
        Logger.getRootLogger().addAppender(consoleAppender);
        Logger.getRootLogger().addAppender(fileAppender);
    }

    public void configureLog() {
        createConsoleAppender();
        createFileAppender();
        addAppenders();
    }
}
