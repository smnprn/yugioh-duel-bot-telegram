package database;

import org.assertj.db.type.Source;
import org.assertj.db.type.Table;
import org.assertj.db.type.ValueType;
import org.junit.jupiter.api.Test;

import static org.assertj.db.api.Assertions.assertThat;

public class CardsTableTests extends DatabaseTest {
    private Table table = new Table(source, "cards");

    @Test
    void areTableConstraintsCorrect() {
        assertThat(table).exists();
        assertThat(table).hasNumberOfColumns(3);
    }

    @Test
    void areIdConstraintsCorrect() {
        assertThat(table).column(0).hasColumnName("id");
        assertThat(table).column("id").hasOnlyNotNullValues();
        assertThat(table).column("id").isOfType(ValueType.NUMBER, true);
    }

    @Test
    void areNameConstraintsCorrect() {
        assertThat(table).column(1).hasColumnName("name");
        assertThat(table).column("name").isOfType(ValueType.TEXT, true);
    }

    @Test
    void areImagePathConstraintsCorrect() {
        assertThat(table).column(2).hasColumnName("image_path");
        assertThat(table).column("image_path").hasOnlyNotNullValues();
        assertThat(table).column("image_path").isOfType(ValueType.TEXT, true);
    }
}
