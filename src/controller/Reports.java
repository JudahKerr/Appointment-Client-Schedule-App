package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Reports class has 3 different tables showing useful data. Select a contact, and it will show all of their appointments. The 2nd table shows all appointments by month and type. The 3rd table shows all divisions with customers and their total amounts. I have my 2nd Lambda expression here, a foreach loop on all my appointments. I chose to use a Lambda here because it is more concise and easier to read than the old for loop I had.
 */
public class Reports {

    @FXML
    private ComboBox<String> contactCombo;
    @FXML
    private TableView<Appointment> contactTable;
    @FXML
    private TableColumn<Appointment, Integer> contactID;
    @FXML
    private TableColumn<Appointment, String> contactTitle;
    @FXML
    private TableColumn<Appointment, String> contactType;
    @FXML
    private TableColumn<Appointment, LocalDateTime> contactStart;
    @FXML
    private TableColumn<Appointment, LocalDateTime> contactEnd;
    @FXML
    private TableColumn<Appointment, Integer> contactCustomerID;
    @FXML
    private TableColumn<Appointment, String> contactDescription;

    @FXML
    private TableView<MonthlyReport> appTable;
    @FXML
    private TableColumn<MonthlyReport, String> appMonth;
    @FXML
    private TableColumn<MonthlyReport, String> appType;
    @FXML
    private TableColumn<MonthlyReport, Integer> appTotal;

    @FXML
    private TableView<SpecialReport> specialTable;
    @FXML
    private TableColumn<SpecialReport, String> specialName;
    @FXML
    private TableColumn<SpecialReport, String> specialTotal;


    @FXML
    public void initialize() throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        contactCombo.setItems(HelperFunctions.getContacts());
        contactCombo.getSelectionModel().select(0);
        try {
            onContactChange(null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Configure custom formatting for start and end columns
        contactStart.setCellFactory(column -> new TableCell<Appointment, LocalDateTime>() {
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

        contactEnd.setCellFactory(column -> new TableCell<Appointment, LocalDateTime>() {
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

        // Setup TableColumn cellValueFactories
        contactID.setCellValueFactory(new PropertyValueFactory<>("Id"));
        contactTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        contactType.setCellValueFactory(new PropertyValueFactory<>("type"));
        contactStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        contactEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        contactCustomerID.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        contactDescription.setCellValueFactory(new PropertyValueFactory<>("description"));


        // Fetch appointments data
        List<Appointment> appointments = AppointmentQuery.select();

        // Process the data
        Map<String, Map<String, Integer>> monthlyReportData = new HashMap<>();

        //  Lambda Expression #2
        appointments.forEach(appointment -> {
            String month = appointment.getStart().getMonth().toString();
            String type = appointment.getType();
            monthlyReportData.putIfAbsent(month, new HashMap<>());
            monthlyReportData.get(month).put(type, monthlyReportData.get(month).getOrDefault(type, 0) + 1);
        });

        // Convert processed data into list
        List<MonthlyReport> reports = new ArrayList<>();
        for (String month : monthlyReportData.keySet()) {
            for (String type : monthlyReportData.get(month).keySet()) {
                reports.add(new MonthlyReport(month, type, monthlyReportData.get(month).get(type)));
            }
        }


        appMonth.setCellValueFactory(new PropertyValueFactory<>("month"));
        appType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appTotal.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));


        appTable.setItems(FXCollections.observableArrayList(reports));


        List<SpecialReport> specialReports = FXCollections.observableArrayList();
        Map<String, Integer> stateCounts = new HashMap<>();

        // Count the number of customers per state
        for (Customer customer : CustomerQuery.select()) {
            String state = customer.getStateName();
            stateCounts.put(state, stateCounts.getOrDefault(state, 0) + 1);
        }

        // Convert the stateCounts map to a list of SpecialReport objects
        for (Map.Entry<String, Integer> entry : stateCounts.entrySet()) {
            specialReports.add(new SpecialReport(entry.getKey(), entry.getValue()));
        }

        specialTable.setItems((ObservableList<SpecialReport>) specialReports);
        specialName.setCellValueFactory(new PropertyValueFactory<>("name"));
        specialTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    private void convertAppointmentTimesToLocal(List<Appointment> appointments) {
        for (Appointment appointment : appointments) {
            LocalDateTime localStart = HelperFunctions.convertUtcToLocal(appointment.getStart());
            LocalDateTime localEnd = HelperFunctions.convertUtcToLocal(appointment.getEnd());


            appointment.setStart(localStart);
            appointment.setEnd(localEnd);
        }
    }


    @FXML
    public void onContactChange(ActionEvent event) throws IOException, SQLException {
        String selectedContactID = HelperFunctions.contactLookup(HelperFunctions.SearchType.BY_NAME, contactCombo.getSelectionModel().getSelectedItem());


        List<Appointment> appointments = AppointmentQuery.appointmentsByContactID(selectedContactID);
        ObservableList<Appointment> observableAppointments = FXCollections.observableArrayList(appointments);
        convertAppointmentTimesToLocal(appointments);
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
