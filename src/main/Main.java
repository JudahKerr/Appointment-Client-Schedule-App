package main;

import model.CustomerQuery;
import model.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Main extends Application {


    public static void main(String[] args) throws SQLException {
        //  Starts MySQL Connection
        JDBC.openConnection();

//     CustomerQuery.insert("Judah", "1234 Parkway", "80521", "9044687798", "Judah", "Judah", 42);
//
//     CustomerQuery.update("Peepee", "Peepee", "123 butt", "80521", "911", "Judah", 42, 2);
//
//        for(int i = 5; i <= 11; i++){
//            CustomerQuery.delete(i);
//        }


        CustomerQuery.select();


        //  Starts the application, loads First Screen
        System.setProperty("prism.lcdtext", "false");
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