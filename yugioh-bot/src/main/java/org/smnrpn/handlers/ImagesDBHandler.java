/*
 * YGOPRODECK asks developers to avoid hotlinking images
 * every time you make an API call. I store images locally
 * and add card id, name and image path to a PostgreSQL database.
 */

package org.smnrpn.handlers;

import java.sql.*;

public class ImagesDBHandler extends DBHandler {
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
        } catch (Exception e) {
            e.printStackTrace();
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
        } catch (Exception e) {
            e.printStackTrace();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
