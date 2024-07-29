package com.marrok.schoolmanagermvn.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHelper {

        public Connection cnn;

        public DatabaseHelper() throws SQLException {
            this.cnn = DatabaseConnection.getInstance().getConnection();
        }

        public String getUserNameById(int userId) {
            String query = "SELECT username FROM user WHERE id = ?";
            String username = null;

            try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    username = resultSet.getString("username");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle exception
            }

            return username;
        }

        public int getUserIdByName(String username) {
            String query = "SELECT id FROM user WHERE username = ?";
            int userId = -1; // Return -1 if the user is not found

            try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    userId = resultSet.getInt("id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle exception
            }

            return userId;
        }


    public boolean validateLogin(String username, String password) {
        String query = "SELECT id FROM user WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("id");
                    SessionManager.setActiveUserId(userId); // Set the active user ID
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return false;
    }

}
