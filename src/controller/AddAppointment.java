package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


/**
 * This is the class for adding an appointment. It parses the text fields and combo boxes, does error checking, and then adds the appointment to the database.
 */
public class AddAppointment {

    @FXML
    TextField IDField;
    @FXML
    TextField titleField;
    @FXML
    TextField typeField;
    @FXML
    TextField descriptionField;
    @FXML
    TextField locationField;
    @FXML
    ComboBox<String> contactField;
    @FXML
    DatePicker startDateField;
    @FXML
    ComboBox<String> startTimeField;
    @FXML
    DatePicker endDateField;
    @FXML
    ComboBox<String> endTimeField;
    @FXML
    ComboBox<String> customerIDField;
    @FXML
    ComboBox<String> userIDField;

    @FXML
    public void initialize() throws SQLException {

        // Adding the Customer ID and Name list
        List<Customer> listOfCustomers = CustomerQuery.select();
        List<String> customerIDList = new ArrayList<>();
        listOfCustomers.forEach(customer -> {
            customerIDList.add(String.valueOf(customer.getId()) + " : " + customer.getName());
        });
        ObservableList<String> observableCustomerIDList = FXCollections.observableArrayList(customerIDList);
        customerIDField.setItems(observableCustomerIDList);


        // Adding the User ID and Name list
        List<User> listOfUsers = UserQuery.select();
        List<String> userList = new ArrayList<>();
        listOfUsers.forEach(user -> {
            userList.add(String.valueOf(user.getUserID()) + " : " + user.getUserName());
        });
        ObservableList<String> observableUserList = FXCollections.observableArrayList(userList);
        userIDField.setItems(observableUserList);

        // Setting the Times
        startTimeField.setItems(HelperFunctions.generateTimeList());
        endTimeField.setItems(HelperFunctions.generateTimeList());

        // Setting the contacts
        contactField.setItems(HelperFunctions.getContacts());
    }


    @FXML
    public void onSaveClick(ActionEvent event) throws IOException {
        try {
            if (titleField == null || titleField.getText() == null || titleField.getText().trim().isEmpty()) {
                HelperFunctions.showAlert("Error", "Error", "Title field cannot be empty.");
                return;
            }
            String title = titleField.getText();

            if (descriptionField == null || descriptionField.getText() == null || descriptionField.getText().trim().isEmpty()) {
                HelperFunctions.showAlert("Error", "Error", "Description field cannot be empty.");
                return;
            }
            String description = descriptionField.getText();

            if (locationField == null || locationField.getText() == null || locationField.getText().trim().isEmpty()) {
                HelperFunctions.showAlert("Error", "Error", "Location field cannot be empty.");
                return;
            }
            String location = locationField.getText();

            if (typeField == null || typeField.getText() == null || typeField.getText().trim().isEmpty()) {
                HelperFunctions.showAlert("Error", "Error", "Type field cannot be empty.");
                return;
            }
            String type = typeField.getText();

            LocalDate startDateValue = startDateField.getValue();
            String startTimeSelected = startTimeField.getSelectionModel().getSelectedItem();
            if (startDateValue == null || startTimeSelected == null) {
                HelperFunctions.showAlert("Error", "Error", "Please select start date and time.");
                return;
            }
            LocalDateTime start = startDateValue.atTime(LocalTime.parse(startTimeSelected, DateTimeFormatter.ofPattern("H:mm")));

            LocalDate endDateValue = endDateField.getValue();
            String endTimeSelected = endTimeField.getSelectionModel().getSelectedItem();
            if (endDateValue == null || endTimeSelected == null) {
                HelperFunctions.showAlert("Error", "Error", "Please select end date and time.");
                return;
            }
            LocalDateTime end = endDateValue.atTime(LocalTime.parse(endTimeSelected, DateTimeFormatter.ofPattern("H:mm")));

            ZonedDateTime startInUtc = start.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
            ZonedDateTime endInUtc = end.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));

            // Check if the appointment is within business hours (8:00 a.m. to 10:00 p.m. EST)
            ZonedDateTime estStart = start.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("America/New_York"));
            ZonedDateTime estEnd = end.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("America/New_York"));
            LocalTime businessStart = LocalTime.of(8, 0);
            LocalTime businessEnd = LocalTime.of(22, 0);

            if (estStart.toLocalTime().isBefore(businessStart) || estEnd.toLocalTime().isAfter(businessEnd)) {
                HelperFunctions.showAlert("Error", "Error", "Appointment should be scheduled between 8:00 a.m. to 10:00 p.m. EST.");
                return;
            }
            // Check if End time is before Start Time or Vice Versa
            if (estStart.toLocalTime().isAfter(estEnd.toLocalTime()) || estEnd.toLocalTime().isBefore(estStart.toLocalTime())) {
                HelperFunctions.showAlert("Error", "Error", "Appointment Start and End times must be in logical order.");
                return;
            }

            // Check if it's the weekend
            DayOfWeek dayOfWeek = estStart.getDayOfWeek();
            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                HelperFunctions.showAlert("Error", "Error", "Appointment cannot be scheduled on a weekend.");
                return;
            }


            String selectedContact = contactField.getSelectionModel().getSelectedItem();
            if (selectedContact == null) {
                HelperFunctions.showAlert("Error", "Error", "Contact field cannot be empty.");
                return;
            }
            int contactID = Integer.parseInt(HelperFunctions.contactLookup(HelperFunctions.SearchType.BY_NAME, selectedContact));

            String selectedCustomer = customerIDField.getSelectionModel().getSelectedItem();
            if (selectedCustomer == null || selectedCustomer.trim().isEmpty()) {
                HelperFunctions.showAlert("Error", "Error", "Customer field cannot be empty.");
                return;
            }
            int customerId = Integer.parseInt(selectedCustomer.substring(0, selectedCustomer.indexOf(" ")));
            System.out.println("Customer ID: " + customerId);

            String selectedUser = userIDField.getSelectionModel().getSelectedItem();
            if (selectedUser == null || selectedUser.trim().isEmpty()) {
                HelperFunctions.showAlert("Error", "Error", "User field cannot be empty.");
                return;
            }
            int userId = Integer.parseInt(selectedUser.substring(0, selectedUser.indexOf(" ")));

            String lastUpdatedBy = Directory.selectedUser.getUserName();
            String createdBy = Directory.selectedUser.getUserName();

            // Check for overlapping appointments
            List<Appointment> existingAppointments = AppointmentQuery.getAppointmentsForCustomer(customerId);
            for (Appointment existingAppointment : existingAppointments) {
                LocalDateTime existingStartUtc = existingAppointment.getStart();
                LocalDateTime existingEndUtc = existingAppointment.getEnd();
                if ((startInUtc.toLocalDateTime().isEqual(existingStartUtc) || (startInUtc.toLocalDateTime().isAfter(existingStartUtc) && startInUtc.toLocalDateTime().isBefore(existingEndUtc))) ||
                        (endInUtc.toLocalDateTime().isEqual(existingEndUtc) || (endInUtc.toLocalDateTime().isAfter(existingStartUtc) && endInUtc.toLocalDateTime().isBefore(existingEndUtc))) ||
                        (startInUtc.toLocalDateTime().isBefore(existingStartUtc) && endInUtc.toLocalDateTime().isAfter(existingEndUtc))) {
                    HelperFunctions.showAlert("Error", "Error", "Appointment overlaps with an existing appointment for that customer.");
                    return;
                }
            }


            int updatedRows = AppointmentQuery.insert(title, description, location, type, startInUtc.toLocalDateTime(), endInUtc.toLocalDateTime(), createdBy, lastUpdatedBy, customerId, userId, contactID);

            if (updatedRows > 0) {
                HelperFunctions.showAlert("information", "Updated", "Appointment successfully added.");
                Stage stage = (Stage) titleField.getScene().getWindow();
                if (stage != null) {
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Directory.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 1200, 900);
                    String css = this.getClass().getResource("/view/styles.css").toExternalForm();
                    scene.getStylesheets().add(css);
                    stage.setTitle("Client/Appointment Scheduler");
                    stage.setScene(scene);
                    stage.show();
                } else {
                    System.out.println("Failed to fetch the Stage.");
                }
            } else {

                System.out.println("Failed to add appointment.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while adding appointment.");
        }
        onCancelClick(event);
    }

    @FXML
    public void onCancelClick(ActionEvent event) throws IOException {
        Stage stage = (Stage) titleField.getScene().getWindow();

        if (stage != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Directory.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1200, 900);
            String css = this.getClass().getResource("/view/styles.css").toExternalForm();
            scene.getStylesheets().add(css);
            stage.setTitle("Client/Appointment Scheduler");
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("Failed to fetch the Stage.");
        }
    }


}

