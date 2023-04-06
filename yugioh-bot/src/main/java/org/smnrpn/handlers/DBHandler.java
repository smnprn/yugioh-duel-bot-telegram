package org.smnrpn.handlers;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBHandler {
    private final String URL = System.getenv("CARD_DB_URL");
    private final String USER = System.getenv("CARD_DB_USER");
    private final String PASSWORD = System.getenv("CARD_DB_PASSWORD");

    public Connection connect() {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }
}
