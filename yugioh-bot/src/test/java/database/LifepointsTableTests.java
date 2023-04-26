package database;

import org.assertj.db.type.Source;
import org.assertj.db.type.Table;
import org.assertj.db.type.ValueType;
import org.junit.jupiter.api.Test;

import static org.assertj.db.api.Assertions.assertThat;

public class LifepointsTableTests extends DatabaseTest {
    private Table table = new Table(source, "lifepoints");

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
    void areUserLPConstraintsCorrect() {
        assertThat(table).column(1).hasColumnName("user_lp");
        assertThat(table).column("user_lp").hasOnlyNotNullValues();
        assertThat(table).column("user_lp").isOfType(ValueType.NUMBER, true);
    }

    @Test
    void areOpponentLPConstraintsCorrect() {
        assertThat(table).column(2).hasColumnName("opponent_lp");
        assertThat(table).column("opponent_lp").hasOnlyNotNullValues();
        assertThat(table).column("opponent_lp").isOfType(ValueType.NUMBER, true);
    }
}
