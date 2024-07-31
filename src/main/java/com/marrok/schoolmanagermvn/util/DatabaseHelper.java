package com.marrok.schoolmanagermvn.util;

import com.marrok.schoolmanagermvn.model.Module;
import com.marrok.schoolmanagermvn.model.Student;
import com.marrok.schoolmanagermvn.model.Teacher;
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
    public boolean addStudent(Student student) {
        String query = "INSERT INTO student (fname, lname, year, contact, gender, class_rooms) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, student.getFname());
            stmt.setString(2, student.getLname());
            stmt.setInt(3, student.getYear());
            stmt.setInt(4, student.getContact());
            stmt.setBoolean(5, student.getGender()); // Assuming gender is stored as a boolean
            stmt.setInt(6, student.getClassRooms()); // Adjust if class_rooms is not used or needed

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Return true if at least one row was inserted
        } catch (SQLException e) {
            e.printStackTrace();
            // Log the exception or handle it as needed
            return false;
        }
    }

    // New method for updating a student record
    public boolean updateStudent(Student student) {
        String updateQuery = "UPDATE student SET fname = ?, lname = ?, year = ?, contact = ?, gender = ?, class_rooms = ? WHERE stud_ID = ?";

        try (PreparedStatement stmt = this.cnn.prepareStatement(updateQuery)) {
            stmt.setString(1, student.getFname());
            stmt.setString(2, student.getLname());
            stmt.setInt(3, student.getYear());
            stmt.setInt(4, student.getContact());
            stmt.setBoolean(5, student.getGender());
            stmt.setInt(6, student.getClassRooms());
            stmt.setInt(7, student.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Log error or handle it accordingly
            return false;
        }
    }

    public boolean deleteStudent(int studentId) {
        String query = "DELETE FROM student WHERE stud_ID = ?";

        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setInt(1, studentId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Return true if at least one row was deleted
        } catch (SQLException e) {
            e.printStackTrace();
            // Log the exception or handle it as needed
            return false;
        }
    }


    public int getTotalStudentCount() throws SQLException {
        String query = "SELECT COUNT(*) AS total FROM student";
        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
        }
        return 0; // Return 0 if no data is found
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
            String query = "SELECT DISTINCT class_rooms FROM student WHERE class_rooms IS NOT NULL";

            try (PreparedStatement preparedStatement = cnn.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    classrooms.add(resultSet.getInt("class_rooms"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle exception
            }

            return classrooms;
        }

    public int getTotalClasses() throws SQLException {
        String query = "SELECT COUNT(DISTINCT class_rooms) FROM student"; // Adjust query as needed
        try (PreparedStatement statement = cnn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return 0;
    }

    public int getTotalTeachers() throws SQLException {
        String query = "SELECT COUNT(*) FROM teacher"; // Adjust query as needed
        try (PreparedStatement statement = cnn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return 0;
    }


    public boolean addTeacher(Teacher teacher) {
        String query = "INSERT INTO teacher (fname, lname, phone, gender, address) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = cnn.prepareStatement(query)) {
            stmt.setString(1, teacher.getFname());
            stmt.setString(2, teacher.getLname());
            stmt.setInt(3, teacher.getPhone());
            stmt.setBoolean(4, teacher.getGender());
            stmt.setString(5, teacher.getAddress());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateTeacher(Teacher teacher) {
        String query = "UPDATE teacher SET fname = ?, lname = ?, phone = ?, gender = ?, address = ? WHERE teacher_ID  = ?";
        try (PreparedStatement stmt = cnn.prepareStatement(query)) {
            stmt.setString(1, teacher.getFname());
            stmt.setString(2, teacher.getLname());
            stmt.setInt(3, teacher.getPhone());
            stmt.setBoolean(4, teacher.getGender());
            stmt.setString(5, teacher.getAddress());
            stmt.setInt(6, teacher.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTeacher(int id) {
        String query = "DELETE FROM teacher WHERE teacher_ID = ?";
        try (PreparedStatement stmt = cnn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ObservableList<Teacher> getTeachers() throws SQLException {
        ObservableList<Teacher> teachers = FXCollections.observableArrayList();
        String query = "SELECT * FROM teacher";
        try (PreparedStatement stmt = cnn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Teacher teacher = new Teacher(
                        rs.getInt("teacher_ID"),
                        rs.getString("fname"),
                        rs.getString("lname"),
                        rs.getInt("phone"),
                        rs.getString("address"),
                        rs.getBoolean("gender")

                );
                teachers.add(teacher);
            }
        }
        return teachers;
    }

    public List<String> getRecommendedTeacherFirstNames() throws SQLException {
        List<String> names = new ArrayList<>();
        String query = "SELECT DISTINCT fname FROM teacher";
        try (PreparedStatement stmt = cnn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                names.add(rs.getString("fname"));
            }
        }
        return names;
    }

    public List<String> getRecommendedTeacherLastNames() throws SQLException {
        List<String> names = new ArrayList<>();
        String query = "SELECT DISTINCT lname FROM teacher";
        try (PreparedStatement stmt = cnn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                names.add(rs.getString("lname"));
            }
        }
        return names;
    }

    public ObservableList<Module> getModules() throws SQLException {
        ObservableList<Module> modules = FXCollections.observableArrayList();
        String query = "SELECT module_id , name FROM module"; // Ensure this matches your table name

        try (PreparedStatement stmt = this.cnn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("module_id");
                String name = rs.getString("name");
                modules.add(new Module(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
            throw e;
        }

        return modules;
    }

    public boolean addModule(Module module) throws SQLException {
        String query = "INSERT INTO module (name) VALUES (?)";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, module.getName());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
            throw e;
        }
    }

    public boolean updateModule(Module module) throws SQLException {
        String query = "UPDATE module SET name = ? WHERE module_id = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, module.getName());
            stmt.setInt(2, module.getId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
            throw e;
        }
    }

    public boolean deleteModule(int moduleId) throws SQLException {
        String query = "DELETE FROM module WHERE module_id = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setInt(1, moduleId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
            throw e;
        }
    }
}




