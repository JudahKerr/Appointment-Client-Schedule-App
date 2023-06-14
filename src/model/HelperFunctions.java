package model;

import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HelperFunctions {

    public static void showAlert(String type, String title, String message) {
        Alert.AlertType alertType;

        switch (type.toUpperCase()) {
            case "ERROR":
                alertType = Alert.AlertType.ERROR;
                break;
            case "INFORMATION":
                alertType = Alert.AlertType.INFORMATION;
                break;
            case "CONFIRMATION":
                alertType = Alert.AlertType.CONFIRMATION;
                break;
            case "WARNING":
                alertType = Alert.AlertType.WARNING;
                break;
            default:
                alertType = Alert.AlertType.NONE;
                break;
        }

        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

   public enum SearchType {
        BY_ID, BY_NAME
    }

    public static String contactLookup(SearchType searchType, String value) throws SQLException {
        String sql;
        String returnValue = null;

        if (searchType == SearchType.BY_ID) {
            sql = "SELECT Contact_Name FROM CONTACTS WHERE Contact_ID = ?";
        } else {
            sql = "SELECT Contact_ID FROM CONTACTS WHERE Contact_Name = ?";
        }

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setString(1, value);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                returnValue = rs.getString(1);
            } else {
                returnValue = "No Contact Found";
            }
        }
        return returnValue;
    }

    public static String userLookup(SearchType searchType, String value) throws SQLException {
        String sql;
        String returnValue = null;

        if (searchType == SearchType.BY_ID) {
            sql = "SELECT User_Name FROM USERS WHERE User_ID = ?";
        } else {
            sql = "SELECT User_ID FROM USERS WHERE User_Name = ?";
        }

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setString(1, value);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                returnValue = rs.getString(1);
            } else {
                returnValue = "No User Found";
            }
        }
        return returnValue;
    }

    public static String divisionLookup(SearchType searchType, String value) throws SQLException {
        String sql;
        String returnValue = null;

        if (searchType == SearchType.BY_ID) {
            sql = "SELECT Division FROM FIRST_LEVEL_DIVISIONS WHERE Division_ID = ?";
        } else {
            sql = "SELECT Division_ID FROM FIRST_LEVEL_DIVISIONS WHERE Division = ?";
        }

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setString(1, value);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                returnValue = rs.getString(1);
            } else {
                returnValue = "No Division Found";
            }
        }
        return returnValue;
    }


}
