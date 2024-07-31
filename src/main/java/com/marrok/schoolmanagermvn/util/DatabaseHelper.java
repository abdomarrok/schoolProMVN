package com.marrok.schoolmanagermvn.util;

import com.marrok.schoolmanagermvn.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public ObservableList<Student> getStudents() {
        ObservableList<Student> students = FXCollections.observableArrayList();
        String query = "SELECT * FROM student"; // Adjust query to match your table schema

        try (PreparedStatement stmt = this.cnn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("stud_ID");
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                int year = rs.getInt("year");
                int contact = rs.getInt("contact");
                boolean gender = rs.getBoolean("gender");
                int classRooms = rs.getInt("class_rooms");

                Student student = new Student(id, fname, lname, year, contact, gender, classRooms);
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Log error or handle it accordingly
        }

        return students;
    }



        // Existing connection and methods...

        public List<String> getRecommendedFirstNames() {
            List<String> recommendedNames = new ArrayList<>();
            String query = "SELECT DISTINCT fname FROM student WHERE fname IS NOT NULL";

            try (PreparedStatement preparedStatement = cnn.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    recommendedNames.add(resultSet.getString("fname"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle exception
            }

            return recommendedNames;
        }

        public List<String> getRecommendedLastNames() {
            List<String> recommendedNames = new ArrayList<>();
            String query = "SELECT DISTINCT lname FROM student WHERE lname IS NOT NULL";

            try (PreparedStatement preparedStatement = cnn.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    recommendedNames.add(resultSet.getString("lname"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle exception
            }

            return recommendedNames;
        }

        public List<Integer> getClassrooms() {
            List<Integer> classrooms = new ArrayList<>();
            String query = "SELECT DISTINCT classRooms FROM student WHERE classRooms IS NOT NULL";

            try (PreparedStatement preparedStatement = cnn.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    classrooms.add(resultSet.getInt("classRooms"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle exception
            }

            return classrooms;
        }
    }

