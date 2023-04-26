package database;

import org.assertj.db.type.Source;

public class DatabaseTest {
    protected final String URL = System.getenv("CARD_DB_URL");
    protected final String USER = System.getenv("CARD_DB_USER");
    protected final String PASSWORD = System.getenv("CARD_DB_PASSWORD");

    protected Source source = new Source(URL, USER, PASSWORD);
}
