package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.Main;
import model.Appointment;
import model.AppointmentQuery;
import model.HelperFunctions;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Reports {

    @FXML private ComboBox<String> contactCombo;
    @FXML private TableView<Appointment> contactTable;
    @FXML private TableColumn<Appointment, Integer> contactID;
    @FXML private TableColumn<Appointment, String> contactTitle;
    @FXML private TableColumn<Appointment, String> contactType;
    @FXML private TableColumn<Appointment, String> contactStart;
    @FXML private TableColumn<Appointment, String> contactEnd;
    @FXML private TableColumn<Appointment, Integer> contactCustomerID;
    @FXML private TableColumn<Appointment, String> contactDescription;

    @FXML public void initialize() throws SQLException {
        contactCombo.setItems(HelperFunctions.getContacts());

        // Setup TableColumn cellValueFactories
        contactID.setCellValueFactory(new PropertyValueFactory<>("Id"));
        contactTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        contactType.setCellValueFactory(new PropertyValueFactory<>("type"));
        contactStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        contactEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        contactCustomerID.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        contactDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    @FXML public void onContactChange(ActionEvent event) throws IOException, SQLException {
        String selectedContactID = HelperFunctions.contactLookup(HelperFunctions.SearchType.BY_NAME, contactCombo.getSelectionModel().getSelectedItem());


        List<Appointment> appointments = AppointmentQuery.appointmentsByContactID(selectedContactID);
        ObservableList<Appointment> observableAppointments = FXCollections.observableArrayList(appointments);
        contactTable.setItems(observableAppointments);
    }

    public void onBackClick(javafx.event.ActionEvent event) throws IOException {
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
