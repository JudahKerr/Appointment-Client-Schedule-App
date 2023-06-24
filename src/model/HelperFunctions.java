package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class HelperFunctions {


    public static ButtonType showAlert(String type, String title, String content) {
        Alert alert;

        switch (type.toLowerCase()) {
            case "error":
                alert = new Alert(Alert.AlertType.ERROR);
                break;
            case "confirmation":
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                break;
            case "information":
                alert = new Alert(Alert.AlertType.INFORMATION);
                break;
            default:
                alert = new Alert(Alert.AlertType.NONE);
                break;
        }

        alert.setTitle(title);
        alert.setContentText(content);

        // This line waits for the user to click a button and returns the result
        return alert.showAndWait().orElse(ButtonType.CANCEL);
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

    public static DivisionResult divisionLookup(SearchType searchType, String value) throws SQLException {
        String sql;
        String divisionName = null;
        int countryCode = 0;

        if (searchType == SearchType.BY_ID) {
            sql = "SELECT Division, Country_ID FROM FIRST_LEVEL_DIVISIONS WHERE Division_ID = ?";
        } else {
            sql = "SELECT Division_ID, Country_ID FROM FIRST_LEVEL_DIVISIONS WHERE Division = ?";
        }

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setString(1, value);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                divisionName = rs.getString(1);
                countryCode = rs.getInt(2);
            } else {
                divisionName = "No Division Found";
            }
        }

        // Create a DivisionResult object and return it
        return new DivisionResult(divisionName, countryCode);
    }

    public static ObservableList<String> getStateDivisions() throws SQLException {
        ObservableList<String> states = FXCollections.observableArrayList();

        // SQL query to retrieve all states
        String sql = "SELECT Division FROM FIRST_LEVEL_DIVISIONS WHERE Country_ID = ?";

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, 1);
            ResultSet rs = ps.executeQuery();

            // Iterate through result set and create DivisionResult objects
            while (rs.next()) {
                String divisionName = rs.getString("Division");

                states.add(divisionName);
            }
        }

        return states;
    }

    public static ObservableList<String> getUKDivisions() throws SQLException {
        ObservableList<String> uk = FXCollections.observableArrayList();

        // SQL query to retrieve all states
        String sql = "SELECT Division FROM FIRST_LEVEL_DIVISIONS WHERE Country_ID = ?";

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, 2);
            ResultSet rs = ps.executeQuery();

            // Iterate through result set and create DivisionResult objects
            while (rs.next()) {
                String divisionName = rs.getString("Division");

                uk.add(divisionName);
            }
        }

        return uk;
    }

    public static ObservableList<String> getCanadaDivisions() throws SQLException {
        ObservableList<String> canada = FXCollections.observableArrayList();

        // SQL query to retrieve all states
        String sql = "SELECT Division FROM FIRST_LEVEL_DIVISIONS WHERE Country_ID = ?";

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, 3);
            ResultSet rs = ps.executeQuery();

            // Iterate through result set and create DivisionResult objects
            while (rs.next()) {
                String divisionName = rs.getString("Division");

                canada.add(divisionName);
            }
        }

        return canada;
    }

    public static ObservableList<String> getContacts() throws SQLException {
        ObservableList<String> contacts = FXCollections.observableArrayList();

        String sql = "SELECT Contact_Name FROM CONTACTS";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            // Iterate through result set and create DivisionResult objects
            while (rs.next()) {
                String contactName = rs.getString("Contact_Name");

                contacts.add(contactName);
            }
        }

        return contacts;
    }

    public static String getContactByID(int id) throws SQLException {
        String name = null;

        String sql = "SELECT Contact_Name FROM CONTACTS WHERE Contact_ID = ?";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            // Iterate through result set and create DivisionResult objects
            while (rs.next()) {


                name = rs.getString("Contact_Name");
            }
        }

        return name;
    }

    public static LocalDateTime convertUtcToLocal(LocalDateTime utcDateTime) {
        ZoneId localZoneId = ZoneId.systemDefault();
        return convertDateTimeBetweenZones(utcDateTime, ZoneId.of("UTC"), localZoneId);
    }

    public static LocalDateTime convertUtcToEastern(LocalDateTime utcDateTime) {
        ZoneId easternZoneId = ZoneId.of("America/New_York");
        return convertDateTimeBetweenZones(utcDateTime, ZoneId.of("UTC"), easternZoneId);
    }

    public static LocalDateTime convertSystemToUtc(LocalDateTime systemDateTime) {
        ZoneId localZoneId = ZoneId.systemDefault();
        return convertDateTimeBetweenZones(systemDateTime, localZoneId, ZoneId.of("UTC"));
    }

    public static LocalDateTime convertDateTimeBetweenZones(LocalDateTime dateTime, ZoneId fromZone, ZoneId toZone) {
        ZonedDateTime zonedFrom = dateTime.atZone(fromZone);
        ZonedDateTime zonedTo = zonedFrom.withZoneSameInstant(toZone);
        return zonedTo.toLocalDateTime();
    }

    public static ObservableList<String> generateTimeList() {
        ObservableList<String> timeList = FXCollections.observableArrayList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");

        LocalTime time = LocalTime.MIDNIGHT;
        for (int i = 0; i < (24 * 4); i++) {
            timeList.add(time.format(formatter));
            time = time.plusMinutes(15);
        }

        return timeList;
    }

    public enum SearchType {
        BY_ID, BY_NAME
    }

    public static class DivisionResult {
        private String divisionName;
        private int countryCode;

        public DivisionResult(String divisionName, int countryCode) {
            this.divisionName = divisionName;
            this.countryCode = countryCode;
        }

        public String getDivisionName() {
            return divisionName;
        }

        public int getCountryCode() {
            return countryCode;
        }
    }


}
