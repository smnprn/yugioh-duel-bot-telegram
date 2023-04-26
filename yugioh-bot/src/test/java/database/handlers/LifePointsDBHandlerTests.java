package database.handlers;

import database.DatabaseTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.smnprn.handlers.LifePointsDBHandler;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LifePointsDBHandlerTests extends DatabaseTest {
    static LifePointsDBHandler lifePointsDBHandler = new LifePointsDBHandler();

    @BeforeAll
    static void addTestUser() {
        lifePointsDBHandler.addUser(1L);
    }

    @Test
    void isLifePointsDBHandlerStructureCorrect() {
        assertThat(lifePointsDBHandler).isInstanceOf(LifePointsDBHandler.class);
        assertThat(lifePointsDBHandler).hasNoNullFieldsOrProperties();
    }

    @Test
    void isUserAddedCorrectly() {
        assertThat(lifePointsDBHandler.isPresent(1L)).isTrue();
        assertThat(lifePointsDBHandler.getUserLP(1L)).isEqualTo(8000);
        assertThat(lifePointsDBHandler.getOpponentLP(1L)).isEqualTo(8000);
    }

    @Test
    void areLifePointsUpdatedCorrectly () {
        lifePointsDBHandler.updateUserLP(1L, -1000);
        lifePointsDBHandler.updateOpponentLP(1L, 1000);

        assertThat(lifePointsDBHandler.getUserLP(1L)).isEqualTo(7000);
        assertThat(lifePointsDBHandler.getOpponentLP(1L)).isEqualTo(9000);
    }

    @Test
    void areLifePointsSetCorrectly() {
        lifePointsDBHandler.setUserLP(1L, 12000);
        lifePointsDBHandler.setOpponentLP(1L, 5000);

        assertThat(lifePointsDBHandler.getUserLP(1L)).isEqualTo(12000);
        assertThat(lifePointsDBHandler.getOpponentLP(1L)).isEqualTo(5000);
    }

    @AfterAll
    static void removeTestUser() {
        lifePointsDBHandler.removeUser(1L);
    }
}
