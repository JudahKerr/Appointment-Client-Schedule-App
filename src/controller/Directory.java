package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.Main;
import model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Directory {

    public static User selectedUser = null;


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
    private TableColumn<Appointment, LocalDateTime> appStartColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> appEndColumn;
    @FXML
    private TableColumn<Appointment, String> appContactColumn;
    @FXML
    private TableColumn<Appointment, String> appCustomerColumn;
    @FXML
    private TableColumn<Appointment, String> appUserColumn;

    // Radio Buttons
    @FXML private RadioButton weekRadio;
    @FXML private RadioButton monthRadio;
    @FXML private RadioButton allRadio;


    public void initialize() throws SQLException {
        // Sets the Appointment Table
        // Formatter for the date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Sets the Appointment Table
        List<Appointment> appointments = AppointmentQuery.select();
        convertAppointmentTimesToLocal(appointments);
        appTable.setItems(FXCollections.observableArrayList(appointments));

        // Configure custom formatting for start and end columns
        appStartColumn.setCellFactory(column -> new TableCell<Appointment, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        appEndColumn.setCellFactory(column -> new TableCell<Appointment, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });




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

        // Adding the radio buttons to a toggle group
        ToggleGroup group = new ToggleGroup();
        weekRadio.setToggleGroup(group);
        monthRadio.setToggleGroup(group);
        allRadio.setToggleGroup(group);
        allRadio.setSelected(true);
    }

    public static void getUser(User user){
        selectedUser = user;
    }

    private void convertAppointmentTimesToLocal(List<Appointment> appointments) {
        for (Appointment appointment : appointments) {
            LocalDateTime localStart = HelperFunctions.convertUtcToLocal(appointment.getStart());
            LocalDateTime localEnd = HelperFunctions.convertUtcToLocal(appointment.getEnd());


            appointment.setStart(localStart);
            appointment.setEnd(localEnd);
        }
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

    @FXML
    public void onAllRadio(ActionEvent event) throws IOException {

        List<Appointment> appointments = AppointmentQuery.select();
        convertAppointmentTimesToLocal(appointments);
        appTable.setItems(FXCollections.observableArrayList(appointments));

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
    }

    @FXML
    public void onWeekRadio(ActionEvent event) throws IOException {

        List<Appointment> appointments = AppointmentQuery.selectAppointmentsForCurrentWeek();
        convertAppointmentTimesToLocal(appointments);
        appTable.setItems(FXCollections.observableArrayList(appointments));

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
    }

    @FXML
    public void onMonthRadio(ActionEvent event) throws IOException {
        List<Appointment> appointments = AppointmentQuery.selectAppointmentsForCurrentMonth();
        convertAppointmentTimesToLocal(appointments);
        appTable.setItems(FXCollections.observableArrayList(appointments));

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
    }

    public void onUpdateCustomerClick(javafx.event.ActionEvent event) throws IOException {

        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {
            HelperFunctions.showAlert("error", "Error", "No Customer Selected");
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

    @FXML public void onAppointmentDeleteClick(ActionEvent event) throws IOException {

        Appointment selectedAppointment = appTable.getSelectionModel().getSelectedItem();

        if(selectedAppointment == null) {
            HelperFunctions.showAlert("error", "Error", "No appointment selected.");
        } else {
            ButtonType result = HelperFunctions.showAlert("Confirmation", "Confirmation", "Are you sure you want to delete this appointment?");

            if (result == ButtonType.OK) {

                AppointmentQuery.delete(selectedAppointment.getId());


                HelperFunctions.showAlert("information", "Deleted", "The appointment " + selectedAppointment.getTitle() + " was deleted.");
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

    @FXML
    public void onCustomerDeleteClick(ActionEvent event) throws IOException {
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {
            HelperFunctions.showAlert("error", "Error", "No Customer Selected");
        } else {
            ButtonType result = HelperFunctions.showAlert("Confirmation", "Confirmation", "Are you sure you want to delete this customer? This will delete all appointments associated with this customer as well.");

            if (result == ButtonType.OK) {
                // Deletes all the appointments associated with the customer
                AppointmentQuery.deleteAppointmentsByCustomerID(selectedCustomer.getId());

                // Deletes the customer
                CustomerQuery.delete(selectedCustomer.getId());

                HelperFunctions.showAlert("information", "Deleted", "The customer " + selectedCustomer.getName() + " was deleted.");
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
        }
    }

    public void onAddAppointmentClick(javafx.event.ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/AddAppointment.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        String css = this.getClass().getResource("/view/styles.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("Add Appointment");
        stage.setScene(scene);
        stage.show();
    }

    public void onUpdateAppointmentClick(javafx.event.ActionEvent event) throws IOException {

        Appointment selectedAppointment = appTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            HelperFunctions.showAlert("error", "Error", "No Appointment Selected");
        } else {
            try {
                UpdateAppointment.getAppointment(selectedAppointment);

                Node sourceNode = (Node) event.getSource();
                System.out.println("Source Node: " + sourceNode);
                System.out.println("Source Scene: " + sourceNode.getScene());

                Stage stage = (Stage) sourceNode.getScene().getWindow();
                System.out.println("Stage: " + stage);

                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/UpdateAppointment.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 800, 500);
                String css = this.getClass().getResource("/view/styles.css").toExternalForm();
                scene.getStylesheets().add(css);
                stage.setTitle("Update Appointment");
                stage.setScene(scene);
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
                HelperFunctions.showAlert("error", "Error", "There was an error loading the scene.");
            }
        }
    }


    public void onReportsClick(javafx.event.ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Reports.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 900);
        String css = this.getClass().getResource("/view/styles.css").toExternalForm();
        scene.getStylesheets().add(css);
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
