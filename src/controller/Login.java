package controller;

import javafx.scene.control.Label;
import model.HelperFunctions;
import model.User;
import model.UserQuery;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class Login implements Initializable {

    @FXML
    TextField passwordField;
    @FXML
    TextField usernameField;
    @FXML Label loginLabel;
    @FXML
    Label usernameLabel;
    @FXML
    Label passwordLabel;
    @FXML
    Label timezoneLabel;
    @FXML Button loginButton;
    @FXML Button exitButton;
    @FXML
    Label timezone;


    @FXML
    protected void onLoginClick(ActionEvent event) throws IOException {
        boolean isAuthenticated = false;

        for (User user : UserQuery.select()) {
            if (user.getUserName().equals(usernameField.getText()) && user.getPassword().equals(passwordField.getText())) {
                isAuthenticated = true;
                break;
            }
        }

        ResourceBundle resourceBundle = ResourceBundle.getBundle("Nat", Locale.getDefault());

        if (isAuthenticated) {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Directory.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1200, 900);
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
        Platform.exit();
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
