package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

public class UpdateAppointment {
    private static Appointment selectedAppointment = null;
    @FXML
    private TextField IDField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField locationField;
    @FXML
    private ComboBox<String> contactField;
    @FXML
    private DatePicker startDateField;
    @FXML
    private ComboBox<String> startTimeField;
    @FXML
    private DatePicker endDateField;
    @FXML
    private ComboBox<String> endTimeField;
    @FXML
    private ComboBox<String> customerIDField;
    @FXML
    private ComboBox<String> userIDField;
    @FXML
    private Button saveButton;

    @FXML
    public void initialize() throws SQLException {
        // Populating the fields
        IDField.setText(String.valueOf(selectedAppointment.getId()));
        titleField.setText(selectedAppointment.getTitle());
        typeField.setText(selectedAppointment.getType());
        descriptionField.setText(selectedAppointment.getDescription());
        locationField.setText(selectedAppointment.getLocation());
        contactField.setItems(HelperFunctions.getContacts());
        contactField.getSelectionModel().select(HelperFunctions.getContactByID(selectedAppointment.getContact_id()));
        startDateField.setValue(selectedAppointment.getStart().toLocalDate());
        startTimeField.setItems(HelperFunctions.generateTimeList());
        startTimeField.getSelectionModel().select(selectedAppointment.getStart().format(DateTimeFormatter.ofPattern("H:mm")));
        endDateField.setValue(selectedAppointment.getEnd().toLocalDate());
        endTimeField.setItems(HelperFunctions.generateTimeList());
        endTimeField.getSelectionModel().select(selectedAppointment.getEnd().format(DateTimeFormatter.ofPattern("H:mm")));

        // Adding the Customer ID and Name list
        List<Customer> listOfCustomers = CustomerQuery.select();
        List<String> customerIDList = new ArrayList<>();
        listOfCustomers.forEach(customer -> {
            customerIDList.add(String.valueOf(customer.getId()) + " : " + customer.getName());
        });
        ObservableList<String> observableCustomerIDList = FXCollections.observableArrayList(customerIDList);
        customerIDField.setItems(observableCustomerIDList);

        int selectedCustomerId = selectedAppointment.getCustomer_id();
        int customerIndexToSelect = -1;
        for (int i = 0; i < observableCustomerIDList.size(); i++) {
            String item = observableCustomerIDList.get(i);
            int customerIdInItem = Integer.parseInt(item.substring(0, item.indexOf(" ")));
            if (customerIdInItem == selectedCustomerId) {
                customerIndexToSelect = i;
                break;
            }
        }
        if (customerIndexToSelect != -1) {
            customerIDField.getSelectionModel().select(customerIndexToSelect);
        }

        // Adding the User ID and Name list
        List<User> listOfUsers = UserQuery.select();
        List<String> userList = new ArrayList<>();
        listOfUsers.forEach(user -> {
            userList.add(String.valueOf(user.getUserID()) + " : " + user.getUserName());
        });
        ObservableList<String> observableUserList = FXCollections.observableArrayList(userList);
        userIDField.setItems(observableUserList);

        int selectedUserId = selectedAppointment.getUser_id();
        int userIndexToSelect = -1;
        for (int i = 0; i < observableUserList.size(); i++) {
            String item = observableUserList.get(i);
            int userIdInItem = Integer.parseInt(item.substring(0, item.indexOf(" ")));
            if (userIdInItem == selectedUserId) {
                userIndexToSelect = i;
                break;
            }
        }
        if (userIndexToSelect != -1) {
            userIDField.getSelectionModel().select(userIndexToSelect);
        }
    }

    public static void getAppointment(Appointment app) {
        selectedAppointment = app;
    }

    @FXML
    public void onSaveClick(ActionEvent event) {
        try {
            int appointmentId = Integer.parseInt(IDField.getText());
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


            int updatedRows = AppointmentQuery.update(appointmentId, title, description, location, type, startInUtc.toLocalDateTime(), endInUtc.toLocalDateTime(), lastUpdatedBy, customerId, userId, contactID);

            if (updatedRows > 0) {
                // Show success message
                HelperFunctions.showAlert("information", "Updated", "Appointment successfully updated.");

                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Directory.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 1200, 900);
                String css = this.getClass().getResource("/view/styles.css").toExternalForm();
                scene.getStylesheets().add(css);
                stage.setTitle("Client/Appointment Scheduler");
                stage.setScene(scene);
                stage.show();
            } else {
                // Show failure message
                System.out.println("Failed to add appointment.");
            }
        } catch (Exception e) {
            // Log error and show error message
            e.printStackTrace();
            System.out.println("An error occurred while adding appointment.");
        }


    }

    @FXML
    public void onCancelClick(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Directory.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 900);
        String css = this.getClass().getResource("/view/styles.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("Client/Appointment Scheduler");
        stage.setScene(scene);
        stage.show();
    }
}
