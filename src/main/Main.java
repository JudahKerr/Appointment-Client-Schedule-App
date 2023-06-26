package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.JDBC;

import java.sql.SQLException;

/**
 * The Main class opens the connection to the database through the JDBC class, it then launches the application with the Login page.
 */
public class Main extends Application {


    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        //  Starts MySQL Connection
        JDBC.openConnection();

        //  Starts the application, loads First Screen

        launch(args);

    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        stage.setTitle("Login");
        Scene scene = new Scene(root, 800, 600);
        String css = this.getClass().getResource("/view/styles.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }
}