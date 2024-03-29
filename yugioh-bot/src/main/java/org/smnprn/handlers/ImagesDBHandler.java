/*
 * YGOPRODECK asks developers to avoid hotlinking images
 * every time you make an API call. I store images locally
 * and add card id, name and image path to a PostgreSQL database.
 */

package org.smnprn.handlers;

import org.apache.log4j.Logger;

import java.sql.*;

public class ImagesDBHandler extends DBHandler {
    private final Logger logger = Logger.getLogger(ImagesDBHandler.class);

    public boolean isPresent(int cardId) {
        try {
            Statement statement = connect().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id FROM cards WHERE id=" + cardId);
            resultSet.next();

            if (resultSet.getInt(1) == cardId) {
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

    public String getImagePath(int cardId) {
        String imagePath = "";

        try {
            Statement statement = connect().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT image_path FROM cards WHERE id=" + cardId);
            resultSet.next();

            imagePath = resultSet.getString(1);

            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return imagePath;
    }

    public void addCard(int cardId, String cardName, String imagePath) {
        try {
            String sql = "INSERT INTO cards VALUES (?, ?, ?)";

            PreparedStatement statement = connect().prepareStatement(sql);
            statement.setInt(1, cardId);
            statement.setString(2, cardName);
            statement.setString(3, imagePath);

            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public void removeCard(int cardId) {
        try {
            Statement statement = connect().createStatement();
            statement.execute("DELETE FROM cards WHERE id = " + cardId);

            statement.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
}
