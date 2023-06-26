package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import model.CustomerQuery;
import model.HelperFunctions;

import java.io.IOException;
import java.sql.SQLException;


/**
 * This is the add customer class. It parses the text fields and combo boxes, does error checking, and then adds the customer to the database.
 */
public class AddCustomer {

    ObservableList<String> countryList = FXCollections.observableArrayList("U.S", "UK", "Canada");
    @FXML
    private TextField IDField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField phoneField;
    @FXML
    private ComboBox<String> countryField;
    @FXML
    private ComboBox<String> stateField;
    @FXML
    private TextField postalField;

    @FXML
    public void initialize() {

        // Auto-Hyphen  Phone number listener
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                return;
            }

            // Only allow digits and hyphens
            if (!newValue.matches("[\\d-]*")) {
                phoneField.setText(oldValue);
                return;
            }

            // Check if the user is backspacing and if so, do not format
            if (newValue.length() < oldValue.length()) {
                return;
            }

            String digits = newValue.replaceAll("[^\\d]", "");

            int length = digits.length();

            if (length == 0) {
                return;
            } else if (length < 4) {
                phoneField.setText(digits);
            } else if (length < 7) {
                String formatted = digits.replaceFirst("(\\d{3})(\\d+)", "$1-$2");
                phoneField.setText(formatted);
            } else {
                String formatted = digits.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "$1-$2-$3");
                phoneField.setText(formatted);
                if (phoneField.getText().length() > 12) {
                    phoneField.setText(oldValue);
                }
            }
        });


        countryField.setItems(countryList);

        countryField.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                if (newSelection.equals("U.S")) {
                    try {
                        stateField.setItems(HelperFunctions.getStateDivisions());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } else if (newSelection.equals("UK")) {
                    try {
                        stateField.setItems(HelperFunctions.getUKDivisions());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } else if (newSelection.equals("Canada")) {
                    try {
                        stateField.setItems(HelperFunctions.getCanadaDivisions());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }


    @FXML
    public void onSaveClick(ActionEvent event) throws IOException {
        try {
            // Validation
            String name = nameField.getText();
            if (name == null || name.trim().isEmpty()) {
                HelperFunctions.showAlert("Error", "Error", "Name field cannot be empty.");
                return;
            }

            if (name.matches(".*\\d.*")) {
                HelperFunctions.showAlert("Error", "Error", "Name should not contain numbers.");
                return;
            }

            String address = addressField.getText();
            if (address == null || address.trim().isEmpty()) {
                HelperFunctions.showAlert("Error", "Error", "Address field cannot be empty.");
                return;
            }

            String postal = postalField.getText();
            if (postal == null || postal.trim().isEmpty()) {
                HelperFunctions.showAlert("Error", "Error", "Postal Code field cannot be empty.");
                return;
            }

            if (!postal.matches("\\d+")) {
                HelperFunctions.showAlert("Error", "Error", "Postal code should contain only numbers.");
                return;
            }

            String phone = phoneField.getText();
            if (phone == null || phone.trim().isEmpty()) {
                HelperFunctions.showAlert("Error", "Error", "Phone field cannot be empty.");
                return;
            }

            if (!phoneField.getText().matches("\\d{3}-\\d{3}-\\d{4}")) {
                HelperFunctions.showAlert("Error", "Error", "Phone should be in the format xxx-xxx-xxxx.");
                return;
            }


            if (stateField.getSelectionModel().getSelectedItem() == null) {
                HelperFunctions.showAlert("Error", "Error", "State/Province field cannot be empty.");
                return;
            }

            if (countryField.getSelectionModel().getSelectedItem() == null) {
                HelperFunctions.showAlert("Error", "Error", "Country field cannot be empty.");
                return;
            }

            String createdBy = Directory.selectedUser.getUserName();
            String updatedBy = Directory.selectedUser.getUserName();
            int divID = 0;
            String state = stateField.getSelectionModel().getSelectedItem().toString();
            HelperFunctions.DivisionResult newDiv = HelperFunctions.divisionLookup(HelperFunctions.SearchType.BY_NAME, state);
            divID = Integer.parseInt(newDiv.getDivisionName());

            // Database update
            CustomerQuery.insert(name, address, postal, phone, createdBy, updatedBy, divID);

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Directory.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1200, 900);
            String css = this.getClass().getResource("/view/styles.css").toExternalForm();
            scene.getStylesheets().add(css);
            stage.setTitle("Client/Appointment Scheduler");
            stage.setScene(scene);
            stage.show();


        } catch (Exception ex) {

            HelperFunctions.showAlert("Error", "Error", "An error occurred while updating customer.");
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
