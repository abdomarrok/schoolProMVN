package com.marrok.schoolmanagermvn.util;

import com.marrok.schoolmanagermvn.model.*;
import com.marrok.schoolmanagermvn.model.Module;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Student getStudentById(int studentId) {
        Student student = null;
        String query = "SELECT * FROM student WHERE stud_ID = ?"; // Adjust query to match your table schema

        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("stud_ID");
                    String fname = rs.getString("fname");
                    String lname = rs.getString("lname");
                    int year = rs.getInt("year");
                    int contact = rs.getInt("contact");
                    boolean gender = rs.getBoolean("gender");
                    int classRooms = rs.getInt("class_rooms");

                    student = new Student(id, fname, lname, year, contact, gender, classRooms);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Log error or handle it accordingly
        }

        return student;
    }

    public int getStudentIdByName(String name) {
        String query = "SELECT stud_ID FROM student WHERE CONCAT(fname, ' ', lname) = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("stud_ID");
                } else {
                    throw new SQLException("No student found with the name: " + name);
                }
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ObservableList<String> getStudentNames() {
        ObservableList<String> studentNames = FXCollections.observableArrayList();
        String query = "SELECT CONCAT(fname, ' ', lname) AS full_name FROM student"; // Adjust based on your schema

        try (PreparedStatement stmt = this.cnn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String fullName = rs.getString("full_name");
                studentNames.add(fullName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception
        }

        return studentNames;
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

    public int  getTotalInscriptionCount()throws SQLException {
        String query = "SELECT COUNT(*) AS total FROM student_inscription";
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
        String query = "SELECT COUNT(DISTINCT id) FROM course_session"; // Adjust query as needed
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

    public ObservableList<Session_model> getSessions() {
        ObservableList<Session_model> sessions = FXCollections.observableArrayList();
        String query = "SELECT id, module_ID, teacher_ID FROM course_session"; // Replace 'session' with the actual table name if different

        try (PreparedStatement stmt = this.cnn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int moduleId = rs.getInt("module_ID");
                int teacherId = rs.getInt("teacher_ID");

                Session_model session = new Session_model(id, moduleId, teacherId);
                sessions.add(session);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }

        return sessions;
    }
    public Map<String, Integer> getSessionNames() {
        Map<String, Integer> sessionMap = new HashMap<>();
        String query =
                "SELECT cs.id, m.name AS module_name, t.fname, t.lname " +
                        "FROM course_session cs " +
                        "JOIN module m ON cs.module_ID = m.module_id " +
                        "JOIN teacher t ON cs.teacher_ID = t.teacher_id"; // Adjust table names and fields as necessary

        try (PreparedStatement stmt = this.cnn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int sessionId = rs.getInt("id");
                String moduleName = rs.getString("module_name");
                String teacherName = rs.getString("fname") + " " + rs.getString("lname");
                String displayName = "Session " + sessionId + ": " + moduleName + " with " + teacherName;
                sessionMap.put(displayName, sessionId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }

        return sessionMap;
    }


    public int getSessionIdByName(String name) {
        String query =
                "SELECT cs.id " +
                        "FROM course_session cs " +
                        "JOIN module m ON cs.module_ID = m.module_id " +
                        "WHERE m.name = ?"; // Adjust table names and fields as necessary

        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    GeneralUtil.showAlert(Alert.AlertType.ERROR,"Session not Found","No session found with the name: " + name);
                   // throw new SQLException("No session found with the name: " + name);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return -1;
    }





    public boolean addSession(int moduleId, int teacherId) {
        String query = "INSERT INTO course_session (module_ID, teacher_ID) VALUES (?, ?)";
        try (PreparedStatement stmt = cnn.prepareStatement(query)) {
            stmt.setInt(1, moduleId);
            stmt.setInt(2, teacherId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Return true if insertion was successful
        } catch (SQLException e) {
            e.printStackTrace();
            GeneralUtil.showAlert(Alert.AlertType.ERROR,"ERROR",e.getMessage());
            // Handle exception or throw a custom exception
            return false;
        }
    }

    public boolean updateSession(int sessionId, int moduleId, int teacherId)  {
        String query = "UPDATE course_session SET module_ID = ?, teacher_ID = ? WHERE id = ?";
        try (PreparedStatement stmt = cnn.prepareStatement(query)) {
            stmt.setInt(1, moduleId);
            stmt.setInt(2, teacherId);
            stmt.setInt(3, sessionId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                throw e;
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    public boolean deleteSession(int sessionId) throws SQLException {
        String query = "DELETE FROM course_session WHERE id = ?";
        try (PreparedStatement stmt = cnn.prepareStatement(query)) {
            stmt.setInt(1, sessionId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public ObservableList<String> getAllModuleNames() throws SQLException {
        String query = "SELECT name FROM module";
        ObservableList<String> moduleNames = FXCollections.observableArrayList();
        try (
                PreparedStatement stmt = cnn.prepareStatement(query);
                ResultSet rs= stmt.executeQuery(query)
        ) {
            while (rs.next()) {
                moduleNames.add(rs.getString("name"));
            }
        }
        return moduleNames;
    }

    public ObservableList<String> getAllTeacherNames() throws SQLException {
        String query = "SELECT CONCAT(fname, ' ', lname) AS full_name FROM teacher";
        ObservableList<String> teacherNames = FXCollections.observableArrayList();
        try ( PreparedStatement stmt = cnn.prepareStatement(query);
              ResultSet rs= stmt.executeQuery(query)) {
            while (rs.next()) {
                teacherNames.add(rs.getString("full_name"));
            }
        }
        return teacherNames;
    }

    public int getModuleIdByName(String moduleName) throws SQLException {
        String query = "SELECT module_id FROM module WHERE name = ?";
        try (PreparedStatement stmt = cnn.prepareStatement(query)) {
            stmt.setString(1, moduleName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("module_id");
                }
            }
        }
        throw new SQLException("Module not found: " + moduleName);
    }

    public int getTeacherIdByName(String teacherName) throws SQLException {
        String query = "SELECT teacher_ID FROM teacher WHERE CONCAT(fname, ' ', lname) = ?";
        try (PreparedStatement stmt = cnn.prepareStatement(query)) {
            stmt.setString(1, teacherName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("teacher_ID");
                }
            }
        }
        throw new SQLException("Teacher not found: " + teacherName);
    }

    public String getModuleById(int moduleId) {
        String query = "SELECT name FROM module WHERE module_id = ?";
        PreparedStatement stmt = null;
        try {
            stmt = cnn.prepareStatement(query);
            stmt.setInt(1, moduleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "";
    }





    public String getTeacherFullNameById(int teacherId) {
        String query = "SELECT CONCAT(fname, ' ', lname) AS full_name FROM teacher WHERE teacher_ID = ?";
        PreparedStatement stmt = null;
        try {
            stmt = cnn.prepareStatement(query);
            stmt.setInt(1, teacherId);
            ResultSet rs = stmt.executeQuery() ;
            if (rs.next()) {
                return rs.getString("full_name");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return "";
    }

    public boolean addInscription(int studentId, int sessionId, LocalDate registrationDate, String price) {
        String query = "INSERT INTO student_inscription (student_ID, session_id, registration_date, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = cnn.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, sessionId);
            stmt.setDate(3, java.sql.Date.valueOf(registrationDate));
            stmt.setString(4, price);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateInscription(int inscriptionId, int studentId, int sessionId, LocalDate registrationDate, String price) {
        String query = "UPDATE student_inscription SET student_ID = ?, session_id = ?, registration_date = ?, price = ? WHERE inscription_ID = ?";
        try (PreparedStatement stmt = cnn.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, sessionId);
            stmt.setDate(3, java.sql.Date.valueOf(registrationDate));
            stmt.setString(4, price);
            stmt.setInt(5, inscriptionId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteInscription(int inscriptionId) {
        String query = "DELETE FROM student_inscription WHERE inscription_ID = ?";
        try (PreparedStatement stmt = cnn.prepareStatement(query)) {
            stmt.setInt(1, inscriptionId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public Map<String, Object> getInscriptionById(int inscriptionId) throws SQLException {
        String query = "SELECT i.*, CONCAT(s.fname,' ',s.lname) AS student_name, " +
                "CONCAT('Session ', cs.id, ': ', m.name, ' with ', t.fname, ' ', t.lname) AS session_display_name " +
                "FROM student_inscription i " +
                "JOIN student s ON i.student_ID = s.stud_ID " +
                "JOIN course_session cs ON i.session_id = cs.id " +
                "JOIN module m ON cs.module_ID = m.module_id " +
                "JOIN teacher t ON cs.teacher_ID = t.teacher_ID " +
                "WHERE i.inscription_ID = ?";

        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setInt(1, inscriptionId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("student_name", rs.getString("student_name"));
                    result.put("session_display_name", rs.getString("session_display_name"));
                    result.put("registration_date", rs.getDate("registration_date") != null ? rs.getDate("registration_date").toLocalDate() : null);
                    result.put("price", rs.getString("price"));

                    return result;
                } else {
                    // Handle the case where no record is found
                    System.out.println("No inscription found with ID: " + inscriptionId);
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error occurred while fetching inscription: " + e.getMessage());
            throw e; // Re-throw the exception to be handled by the caller
        }
    }

    public ObservableList<StudentInscription> getAllInscriptions() {
        String query = "SELECT * FROM student_inscription";
        ObservableList<StudentInscription> inscriptions = FXCollections.observableArrayList();
        try (PreparedStatement stmt = cnn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                StudentInscription inscription = new StudentInscription(
                        rs.getInt("inscription_ID"),
                        rs.getInt("student_ID"),
                        rs.getInt("session_id"),
                        rs.getDate("registration_date").toLocalDate(),
                        rs.getString("price")
                );
                inscriptions.add(inscription);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inscriptions;
    }


    public ObservableList<StudentInscription> getInscriptionsByStudent(int studentId) {
        String query = "SELECT * FROM student_inscription WHERE student_ID = ?";
        ObservableList<StudentInscription> inscriptions = FXCollections.observableArrayList();
        try (PreparedStatement stmt = cnn.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    StudentInscription inscription = new StudentInscription(
                            rs.getInt("inscription_ID"),
                            rs.getInt("student_ID"),
                            rs.getInt("session_id"),
                            rs.getDate("registration_date").toLocalDate(),
                            rs.getString("price")
                    );
                    inscriptions.add(inscription);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inscriptions;
    }
    public ObservableList<StudentInscription> getAllInscriptionsWithDetails() {
        String query = "SELECT si.inscription_ID, " +
                "si.student_ID, " + // Include student ID
                "s.stud_ID, CONCAT(s.fname, ' ', s.lname) AS full_name, " +
                "si.session_ID, " + // Include session ID
                "cs.id, CONCAT(m.name, ' (', t.teacher_ID, ')') AS session_info, " +
                "si.registration_date, si.price " +
                "FROM student_inscription si " +
                "JOIN student s ON si.student_ID = s.stud_ID " +
                "JOIN course_session cs ON si.session_ID = cs.id " +
                "JOIN module m ON cs.module_ID = m.module_id " +
                "JOIN teacher t ON cs.teacher_ID = t.teacher_ID";

        ObservableList<StudentInscription> inscriptions = FXCollections.observableArrayList();
        try (PreparedStatement stmt = cnn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                // Extract the student ID and session ID from the result set
                int studentId = rs.getInt("student_ID");
                int sessionId = rs.getInt("session_ID");

                StudentInscription inscription = new StudentInscription(
                        rs.getInt("inscription_ID"), // Inscription ID
                        studentId, // Student ID
                        sessionId, // Session ID
                        rs.getDate("registration_date").toLocalDate(), // Registration date
                        rs.getString("price"), // Price
                        rs.getString("full_name"), // Full name
                        rs.getString("session_info") // Session details
                );
                inscriptions.add(inscription);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inscriptions;
    }



    public ObservableList<StudentInscription> getInscriptionsBySession(int sessionId) {
        String query = "SELECT * FROM student_inscription WHERE session_id = ?";
        ObservableList<StudentInscription> inscriptions = FXCollections.observableArrayList();
        try (PreparedStatement stmt = cnn.prepareStatement(query)) {
            stmt.setInt(1, sessionId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    StudentInscription inscription = new StudentInscription(
                            rs.getInt("inscription_ID"),
                            rs.getInt("student_ID"),
                            rs.getInt("session_id"),
                            rs.getDate("registration_date").toLocalDate(),
                            rs.getString("price")
                    );
                    inscriptions.add(inscription);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inscriptions;
    }



}




