package controller;

import helper.HelperFunctions;
import helper.User;
import helper.UserQuery;
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
import java.util.ResourceBundle;

public class Login implements Initializable {

    @FXML
    TextField passwordField;
    @FXML
    TextField usernameField;

    @FXML
    protected void onLoginClick(ActionEvent event) throws IOException {

        boolean isAuthenticated = false;

        for (User user : UserQuery.select()) {
            if (user.getUserName().equals(usernameField.getText()) && user.getPassword().equals(passwordField.getText())) {
                isAuthenticated = true;
                break;
            }
        }

        if (isAuthenticated) {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Directory.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1200, 900);
            stage.setTitle("Client/Appointment Scheduler");
            stage.setScene(scene);
            stage.show();
        } else {
            HelperFunctions.showAlert("Error","Incorrect Username or Password");
        }
    }


    @FXML
    protected void onExitClick(ActionEvent event) throws IOException {
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
