package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.ButtonType;
import model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;
import java.sql.SQLException;

public class Directory {


    // Customer Table FXML
    @FXML
    private TableView<Customer> customersTable;
    @FXML
    private TableColumn<Customer, Integer> customersID;
    @FXML
    private TableColumn<Customer, String> customersName;
    @FXML
    private TableColumn<Customer, String> customersAddress;
    @FXML
    private TableColumn<Customer, String> customersPhone;
    @FXML
    private TableColumn<Customer, String> customersState;
    @FXML
    private TableColumn<Customer, String> customersPostal;


    // Appointment Table FXML
    @FXML
    private TableView<Appointment> appTable;
    @FXML
    private TableColumn<Appointment, Integer> appIDColumn;
    @FXML
    private TableColumn<Appointment, String> appTitleColumn;
    @FXML
    private TableColumn<Appointment, String> appTypeColumn;
    @FXML
    private TableColumn<Appointment, String> appDescriptionColumn;
    @FXML
    private TableColumn<Appointment, String> appLocationColumn;
    @FXML
    private TableColumn<Appointment, String> appStartColumn;
    @FXML
    private TableColumn<Appointment, String> appEndColumn;
    @FXML
    private TableColumn<Appointment, String> appContactColumn;
    @FXML
    private TableColumn<Appointment, String> appCustomerColumn;
    @FXML
    private TableColumn<Appointment, String> appUserColumn;


    public void initialize() throws SQLException {
        // Sets the Appointment Table
        appTable.setItems(FXCollections.observableArrayList(AppointmentQuery.select()));
        appIDColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        appTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appStartColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        appEndColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        appContactColumn.setCellValueFactory(new PropertyValueFactory<>("contact_id"));
        appCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        appUserColumn.setCellValueFactory(new PropertyValueFactory<>("user_id"));

        //  Sets the Customer Table
        customersTable.setItems(FXCollections.observableArrayList(CustomerQuery.select()));
        customersID.setCellValueFactory(new PropertyValueFactory<>("Id"));
        customersName.setCellValueFactory(new PropertyValueFactory<>("name"));
        customersAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        customersPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customersState.setCellValueFactory(new PropertyValueFactory<>("stateName"));
        customersPostal.setCellValueFactory(new PropertyValueFactory<>("postalCode"));

        customersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Deselect the item in the second table view
                appTable.getSelectionModel().clearSelection();
            }
        });

        appTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Deselect the item in the first table view
                customersTable.getSelectionModel().clearSelection();
            }
        });
    }


    public void onAddCustomerClick(javafx.event.ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/AddCustomer.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 600);
        String css = this.getClass().getResource("/view/styles.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("Add Customer");
        stage.setScene(scene);
        stage.show();
    }

    public void onUpdateCustomerClick(javafx.event.ActionEvent event) throws IOException {

        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();

        if(selectedCustomer == null) {
            HelperFunctions.showAlert("error","Error","No Customer Selected");
        } else {
            UpdateCustomer.getCustomer(selectedCustomer);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/UpdateCustomer.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 600);
            String css = this.getClass().getResource("/view/styles.css").toExternalForm();
            scene.getStylesheets().add(css);
            stage.setTitle("Update Customer");
            stage.setScene(scene);
            stage.show();
        }

    }

    @FXML
    public void onCustomerDeleteClick(ActionEvent event) throws  IOException {
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();

        if(selectedCustomer == null) {
            HelperFunctions.showAlert("error","Error","No Customer Selected");
        } else {
            ButtonType result = HelperFunctions.showAlert("Confirmation","Confirmation","Are you sure you want to delete this customer?");

            if (result == ButtonType.OK) {
                // Deletes the customer
                CustomerQuery.delete(selectedCustomer.getId());

                //  Refresh the Stage
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Directory.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 1200, 900);
                String css = this.getClass().getResource("/view/styles.css").toExternalForm();
                scene.getStylesheets().add(css);
                stage.setTitle("Client/Appointment Scheduler");
                stage.setScene(scene);
                stage.show();
            } else {

            }
    }}

    public void onAddAppointmentClick(javafx.event.ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/AddAppointment.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        stage.setTitle("Add Appointment");
        stage.setScene(scene);
        stage.show();
    }

    public void onUpdateAppointmentClick(javafx.event.ActionEvent event) throws IOException {

        Appointment selectedAppointment = (Appointment) appTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            HelperFunctions.showAlert("error", "Error", "No Appointment Selected");
        } else {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/UpdateAppointment.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 500);
            stage.setTitle("Update Appointment");
            stage.setScene(scene);
            stage.show();
        }
    }

    public void onReportsClick(javafx.event.ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Reports.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 900);
        stage.setTitle("Reports");
        stage.setScene(scene);
        stage.show();
    }

    public void onLogoutClick(javafx.event.ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        stage.setTitle("Login");
        Scene scene = new Scene(root, 800, 600);
        String css = this.getClass().getResource("/view/styles.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }


}
