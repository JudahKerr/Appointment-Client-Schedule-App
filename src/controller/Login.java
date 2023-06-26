package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import main.Main;
import model.*;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;


/**
 * The login class makes sure the user is authenticated through the database, providing the right username and password. Also, where the application writes to "login_activity.txt" to document all successful and otherwise login attempts.
 */
public class Login implements Initializable {

    @FXML
    TextField passwordField;
    @FXML
    TextField usernameField;
    @FXML
    Label loginLabel;
    @FXML
    Label usernameLabel;
    @FXML
    Label passwordLabel;
    @FXML
    Label timezoneLabel;
    @FXML
    Button loginButton;
    @FXML
    Button exitButton;
    @FXML
    Label timezone;


    @FXML
    protected void onLoginClick(ActionEvent event) throws IOException {
        authenticate();
    }

    @FXML
    public void onKeyPress(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            authenticate();
        }
    }

    private void authenticate() throws IOException {
        boolean isAuthenticated = false;

        for (User user : UserQuery.select()) {
            if (user.getUserName().equals(usernameField.getText()) && user.getPassword().equals(passwordField.getText())) {
                isAuthenticated = true;
                Directory.getUser(user);
                List<Appointment> userAppointments = AppointmentQuery.appointmentsByUserID(String.valueOf(user.getUserID()));

                LocalDateTime currentTime = LocalDateTime.now();

                // For demonstration purposes: If it's currently a weekend, simulate the time as being a Monday at 12pm.
                int dayOfWeek = currentTime.getDayOfWeek().getValue();
                if (dayOfWeek == 6 || dayOfWeek == 7) {
                    currentTime = currentTime.plusDays(8 - dayOfWeek).withHour(12).withMinute(0).withSecond(0).withNano(0);
                }
                boolean hasAppointmentWithin15Mins = false;

                for (Appointment appointment : userAppointments) {

                    LocalDateTime appointmentStartTime = appointment.getStart();
                    ZoneId utcZone = ZoneId.of("UTC");
                    ZonedDateTime appointmentStartTimeZoned = appointmentStartTime.atZone(utcZone).withZoneSameInstant(ZoneId.systemDefault());


                    ZonedDateTime currentTimeZoned = currentTime.atZone(ZoneId.systemDefault());

                    // Calculate minutes between current time and appointment time
                    long minutesUntilAppointment = ChronoUnit.MINUTES.between(currentTimeZoned, appointmentStartTimeZoned);


                    if (minutesUntilAppointment >= 0 && minutesUntilAppointment <= 15) {
                        hasAppointmentWithin15Mins = true;
                        HelperFunctions.showAlert("information", "Appointment Upcoming",
                                String.format("Appointment ID: %d\nDate: %s\nTime: %s",
                                        appointment.getId(),
                                        appointmentStartTimeZoned.toLocalDate(),
                                        appointmentStartTimeZoned.toLocalTime()));
                        break;
                    }
                }

                if (!hasAppointmentWithin15Mins) {
                    HelperFunctions.showAlert("information", "No Appointments", "You have no appointments within the next 15 minutes.");
                }

                break;
            }
        }

        // Record login attempt
        recordLoginAttempt(usernameField.getText(), isAuthenticated);

        ResourceBundle resourceBundle = ResourceBundle.getBundle("Nat", Locale.getDefault());

        if (isAuthenticated) {
            Stage stage = (Stage) passwordField.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Directory.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1200, 900);
            String css = this.getClass().getResource("/view/styles.css").toExternalForm();
            scene.getStylesheets().add(css);
            stage.setTitle("Client/Appointment Scheduler");
            stage.setScene(scene);
            stage.show();
        } else {
            String errorTitle = resourceBundle.getString("errorTitle");
            String errorMessage = resourceBundle.getString("errorMessage");
            HelperFunctions.showAlert("error", errorTitle, errorMessage);
        }
    }


    @FXML
    protected void onExitClick(ActionEvent event) throws IOException {
        JDBC.closeConnection();
        Platform.exit();
    }

    private void recordLoginAttempt(String username, boolean isSuccess) throws IOException {
        String timestamp = LocalDateTime.now().toString();
        String status = isSuccess ? "SUCCESS" : "FAILURE";

        String logEntry = String.format("%s - Login attempt by user: %s, Status: %s\n", timestamp, username, status);

        try (FileWriter fileWriter = new FileWriter("login_activity.txt", true)) {
            fileWriter.write(logEntry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle("Nat", Locale.getDefault());

            loginLabel.setText(resourceBundle.getString("Login"));
            usernameLabel.setText(resourceBundle.getString("Username"));
            passwordLabel.setText(resourceBundle.getString("Password"));
            timezoneLabel.setText(resourceBundle.getString("Timezone"));
            exitButton.setText(resourceBundle.getString("Exit"));
            loginButton.setText(resourceBundle.getString("Login"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String timeZoneId = TimeZone.getDefault().getID();
        timezone.setText(timeZoneId);
    }
}
