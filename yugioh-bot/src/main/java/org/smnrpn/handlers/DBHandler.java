package org.smnrpn.handlers;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DBHandler {
    private final String URL = System.getenv("CARD_DB_URL");
    private final String USER = System.getenv("CARD_DB_USER");
    private final String PASSWORD = System.getenv("CARD_DB_PASSWORD");

    public Connection connect() {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }
}
