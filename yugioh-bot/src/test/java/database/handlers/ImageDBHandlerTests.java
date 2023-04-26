package database.handlers;

import database.DatabaseTest;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.smnprn.handlers.DBHandler;
import org.smnprn.handlers.ImagesDBHandler;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.db.api.Assertions.assertThat;

public class ImageDBHandlerTests extends DatabaseTest {
    static ImagesDBHandler dbHandler = new ImagesDBHandler();
    private Table table = new Table(source, "cards");

    @BeforeAll
    static void addTestCard() {
        dbHandler.addCard(1, "Test", "assets/1.jpg");
    }

    @Test
    void isImageDBHandlerStructureCorrect() {
        assertThat(dbHandler).isInstanceOf(DBHandler.class);
        assertThat(dbHandler).hasNoNullFieldsOrProperties();
    }

    @Test
    void isACardAdded() {
        assertThat(table).hasNumberOfRowsGreaterThan(0);
    }

    @Test
    void isTheRightCardAdded() {
        assertThat(dbHandler.isPresent(1)).isTrue();
    }

    @Test
    void isImagePathCorrect() {
        assertThat(dbHandler.getImagePath(1)).isEqualTo("assets/1.jpg");
    }

    @AfterAll
    static void removeTestCard() {
        dbHandler.removeCard(1);
    }
}
