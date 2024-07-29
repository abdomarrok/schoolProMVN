package com.marrok.schoolmanagermvn.util;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.util.Duration;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.regex.Pattern;

public class GeneralUtil {
    public static boolean isValidPassword(String password) {
//        Password should not contain any space.
//        Password should contain at least one digit(0-9).
//        Password length should be between 8 to 15 characters.
//        Password should contain at least one lowercase letter(a-z).
//        Password should contain at least one uppercase letter(A-Z).
//        Password should contain at least one special character ( @, #, %, &, !, $, etc….).

        // for checking if password length
        // is between 8 and 15
        if (!((password.length() >= 8)
                && (password.length() <= 15))) {
            return false;
        }

        // to check space
        if (password.contains(" ")) {
            return false;
        }
        if (true) {
            int count = 0;

            // check digits from 0 to 9
            for (int i = 0; i <= 9; i++) {

                // to convert int to string
                String str1 = Integer.toString(i);

                if (password.contains(str1)) {
                    count = 1;
                }
            }
            if (count == 0) {
                return false;
            }
        }

        // for special characters
        if (!(password.contains("@") || password.contains("#")
                || password.contains("!") || password.contains("~")
                || password.contains("$") || password.contains("%")
                || password.contains("^") || password.contains("&")
                || password.contains("*") || password.contains("(")
                || password.contains(")") || password.contains("-")
                || password.contains("+") || password.contains("/")
                || password.contains(":") || password.contains(".")
                || password.contains(", ") || password.contains("<")
                || password.contains(">") || password.contains("?")
                || password.contains("|"))) {
            return false;
        }

        if (true) {
            int count = 0;

            // checking capital letters
            for (int i = 65; i <= 90; i++) {

                // type casting
                char c = (char) i;

                String str1 = Character.toString(c);
                if (password.contains(str1)) {
                    count = 1;
                }
            }
            if (count == 0) {
                return false;
            }
        }

        if (true) {
            int count = 0;

            // checking small letters
            for (int i = 90; i <= 122; i++) {

                // type casting
                char c = (char) i;
                String str1 = Character.toString(c);

                if (password.contains(str1)) {
                    count = 1;
                }
            }
            if (count == 0) {
                return false;
            }
        }

        // if all conditions fails
        return true;
    }

    public static boolean isDiffString(String a, String b) {
        return !a.equals(b);
    }

    public static String hash(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        str = str.replaceAll("\\s+", "");
        int end = str.length() > 32 ? 32 : str.length();
        str = str.substring(0, end);
        byte[] bytesOfMessage = str.getBytes("UTF-8");

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(bytesOfMessage);
//        return new String(digest, StandardCharsets.UTF_8);

        System.out.println(Base64.getEncoder().encodeToString(digest).replaceAll("\\/",""));
        return Base64.getEncoder().encodeToString(digest).replaceAll("\\/","");
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static boolean containsIgnoreCase(String str, String subString) {
        return str.toLowerCase().contains(subString.toLowerCase());
    }

    public static void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        if (alertType == Alert.AlertType.INFORMATION) {
            // Create a timeline to close the alert after the specified timeout
            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.millis(500),
                    event -> alert.close()
            ));
            timeline.setCycleCount(1);
            timeline.play();
        }
        alert.showAndWait();
    }
}
