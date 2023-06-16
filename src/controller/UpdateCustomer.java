package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import model.Customer;
import model.CustomerQuery;
import model.HelperFunctions;

import java.io.IOException;
import java.sql.SQLException;

public class UpdateCustomer {

    @FXML private TextField IDField;
    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;
    @FXML private ComboBox countryField;
    @FXML private ComboBox stateField;
    @FXML private TextField postalField;


    private static Customer selectedCustomer = null;
    ObservableList<String> countryList = FXCollections.observableArrayList("U.S", "UK", "Canada");




    public static void getCustomer(Customer customer) {
        selectedCustomer = customer;
    }

    @FXML
    public void initialize() throws SQLException {

        //  Setting the text-fields
        IDField.setText(String.valueOf(selectedCustomer.getId()));
        nameField.setText(selectedCustomer.getName());
        addressField.setText(selectedCustomer.getAddress());
        phoneField.setText(selectedCustomer.getPhone());
        countryField.setItems(countryList);
        postalField.setText(selectedCustomer.getPostalCode());

        //  Setting what Country is focused;
        int divID = selectedCustomer.getDivisionId();
        HelperFunctions.DivisionResult newDiv = HelperFunctions.divisionLookup(HelperFunctions.SearchType.BY_ID, String.valueOf(divID));
        System.out.println(newDiv.getCountryCode());

        switch(newDiv.getCountryCode()) {
            case 1: {
                countryField.getSelectionModel().select(0); // US
                stateField.setItems(HelperFunctions.getStateDivisions());
                stateField.getSelectionModel().select(selectedCustomer.getStateName());
                break;
            }
            case 2: {
                countryField.getSelectionModel().select(1); // UK
                stateField.setItems(HelperFunctions.getUKDivisions());
                stateField.getSelectionModel().select(selectedCustomer.getStateName());
                break;
            }
            case 3: {
                countryField.getSelectionModel().select(2); // CANADA
                stateField.setItems(HelperFunctions.getCanadaDivisions());
                stateField.getSelectionModel().select(selectedCustomer.getStateName());
                break;
            }
        }

        //  Changing the State/Province choice-box depending on the Country
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
    public void onSaveClick(ActionEvent event) {
        try {
            // Validation
            String name = nameField.getText();
            if (name == null || name.trim().isEmpty()) {
                HelperFunctions.showAlert("Error", "Error","Name field cannot be empty.");
                return;
            }

            String address = addressField.getText();
            if (address == null || address.trim().isEmpty()) {
                HelperFunctions.showAlert("Error", "Error","Address field cannot be empty.");
                return;
            }

            String postal = postalField.getText();
            if (postal == null || postal.trim().isEmpty()) {
                HelperFunctions.showAlert("Error", "Error","Postal Code field cannot be empty.");
                return;
            }

            String phone = phoneField.getText();
            if (phone == null || phone.trim().isEmpty()) {
                HelperFunctions.showAlert("Error", "Error","Phone field cannot be empty.");
                return;
            }



            // Parsing
            int customerID;
            try {
                customerID = Integer.parseInt(IDField.getText());
            } catch (NumberFormatException e) {
                HelperFunctions.showAlert("Error", "Error","ID field cannot be empty.");
                return;
            }

            String createdBy = selectedCustomer.getCreatedBy();
            String updatedBy = selectedCustomer.getLastUpdatedBy();
            int divID = 0;
            String state = stateField.getSelectionModel().getSelectedItem().toString();
            HelperFunctions.DivisionResult newDiv = HelperFunctions.divisionLookup(HelperFunctions.SearchType.BY_NAME, state);
            divID = Integer.parseInt(newDiv.getDivisionName());

            // Database update
            CustomerQuery.update(name, address, postal, phone, createdBy, updatedBy, divID, customerID);

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Directory.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1200, 900);
            String css = this.getClass().getResource("/view/styles.css").toExternalForm();
            scene.getStylesheets().add(css);
            stage.setTitle("Client/Appointment Scheduler");
            stage.setScene(scene);
            stage.show();


        } catch (Exception ex) {
            // Handle other exceptions
            HelperFunctions.showAlert("Error", "Error","An error occurred while updating customer.");
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
