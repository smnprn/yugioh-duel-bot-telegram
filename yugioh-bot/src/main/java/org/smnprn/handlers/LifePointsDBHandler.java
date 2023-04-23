package org.smnprn.handlers;

import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LifePointsDBHandler extends DBHandler {
    private final Logger logger = Logger.getLogger(LifePointsDBHandler.class);

    public boolean isPresent(Long id) {
        try {
            Statement statement = connect().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id FROM lifepoints WHERE id=" + id);
            resultSet.next();

            if (resultSet.getLong(1) == id) {
                statement.close();
                resultSet.close();
                return true;
            }

            statement.close();
            resultSet.close();

            return false;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    public void addUser(Long userId) {
        String sql = "INSERT INTO lifepoints VALUES(?, 8000, 8000)";

        try {
            PreparedStatement statement = connect().prepareStatement(sql);
            statement.setLong(1, userId);

            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public int getUserLP(Long userId) {
        int userLP = 0;
        String sql = "SELECT user_lp FROM lifepoints WHERE id=?";

        try {
            PreparedStatement statement = connect().prepareStatement(sql);
            statement.setLong(1, userId);

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            userLP = resultSet.getInt(1);

            resultSet.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return  userLP;
    }

    public int getOpponentLP(Long userId) {
        int opponentLP = 0;
        String sql = "SELECT opponent_lp FROM lifepoints WHERE id=?";

        try {
            PreparedStatement statement = connect().prepareStatement(sql);
            statement.setLong(1, userId);

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            opponentLP = resultSet.getInt(1);

            resultSet.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return  opponentLP;
    }

    public void updateUserLP(Long userId, int value) {
        String sql = "UPDATE lifepoints SET user_lp=? WHERE id=?";

        try {
            PreparedStatement statement = connect().prepareStatement(sql);
            statement.setInt(1, getUserLP(userId) + value);
            statement.setLong(2, userId);

            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public void updateOpponentLP(Long userId, int value) {
        String sql = "UPDATE lifepoints SET opponent_lp=? WHERE id=?";

        try {
            PreparedStatement statement = connect().prepareStatement(sql);
            statement.setInt(1, getOpponentLP(userId) + value);
            statement.setLong(2, userId);

            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public void setUserLP(Long userId, int value) {
        String sql = "UPDATE lifepoints SET user_lp=? WHERE id=?";

        try {
            PreparedStatement statement = connect().prepareStatement(sql);
            statement.setInt(1, value);
            statement.setLong(2, userId);

            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public void setOpponentLP(Long userId, int value) {
        String sql = "UPDATE lifepoints SET opponent_lp=? WHERE id=?";

        try {
            PreparedStatement statement = connect().prepareStatement(sql);
            statement.setInt(1, value);
            statement.setLong(2, userId);

            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
}
