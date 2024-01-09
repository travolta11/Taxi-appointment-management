package com.example.taxireservation;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HelloApplication extends Application {
    private static Stage primaryStage;

    // database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gestion-taxi";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    //buttons
    @FXML
    private  Button g_rv;
    @FXML
    private Button g_chauffeur;
    @FXML
    private Button g_client;
    @FXML
    private Button  g_taxi;
    @FXML
    private Button dec_btn;

    //connection
    public static Connection getDatabaseConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    //login page
    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginForm.fxml"));
        try {
            Parent root = fxmlLoader.load();

            // Pass the database connection to the controller
            LoginForm loginFormController = fxmlLoader.getController();
            Connection connection = getDatabaseConnection();
            loginFormController.setDatabaseConnection(connection);

            // Check if the database connection is successful
            if (connection != null) {
                System.out.println("Connected to the database successfully!");
            } else {
                System.err.println("Failed to connect to the database.");
            }

            Scene scene = new Scene(root, 750, 400);
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();
        } catch (IOException | SQLException e) {
            e.printStackTrace();

        }
    }
// page apptaxi handle
    public static void openAppTaxiWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("apptaxi.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 800, 400);
            primaryStage.setScene(scene);
            primaryStage.setTitle("App Taxi");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading apptaxi.fxml: " + e.getMessage());
        }
    }

//gestion chauffeur handle
    @FXML
    private void handleGestionChauffeurClick(ActionEvent event) {
        // Load chauffeur.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("chauffeur.fxml"));
        Parent root;
        try {

            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return; // Handle the exception appropriately
        }

        // Create a new stage for the chauffeur.fxml window
        Stage chauffeurStage = new Stage();
        chauffeurStage.setTitle("Gestion Chauffeur");
        chauffeurStage.setScene(new Scene(root));

        // Close the current (apptaxi.fxml) window
        Stage currentStage = (Stage) g_chauffeur.getScene().getWindow(); // Assuming g_chauffeur is the button ID
        currentStage.close();

        // Show the new chauffeur.fxml window
        chauffeurStage.show();
    }

//gestion rendez-vous handle
    @FXML
    private void rv_click(ActionEvent event) {
        // Load chauffeur.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Reservation.fxml"));
        Parent root;
        try {

            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }


        Stage reservationStage = new Stage();
        reservationStage.setTitle("Gestion Reservation");
        reservationStage.setScene(new Scene(root));

        Stage currentStage = (Stage) g_rv.getScene().getWindow();
        currentStage.close();


        reservationStage.show();
    }

//gestion client handle
    @FXML
    private void client_click(ActionEvent event) {
        // Load chauffeur.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("client.fxml"));
        Parent root;
        try {

            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }


        Stage clientStage = new Stage();
        clientStage.setTitle("Gestion Client");
        clientStage.setScene(new Scene(root));

        Stage currentStage = (Stage) g_client.getScene().getWindow();
        currentStage.close();


        clientStage.show();
    }

//gestion taxi handle
    @FXML
    private void taxi_click(ActionEvent event) {
        // Load chauffeur.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("taxi.fxml"));
        Parent root;
        try {

            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }


        Stage taxiStage = new Stage();
        taxiStage.setTitle("Gestion Taxi");
        taxiStage.setScene(new Scene(root));

        Stage currentStage = (Stage) g_taxi.getScene().getWindow();
        currentStage.close();


        taxiStage.show();
    }

//logout handle
    @FXML
    private void dec_click(ActionEvent event) {
        // Load chauffeur.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginForm.fxml"));
        Parent root;
        try {

            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }


        Stage loginStage = new Stage();
        loginStage.setTitle("Connexion");
        loginStage.setScene(new Scene(root));

        Stage currentStage = (Stage) dec_btn.getScene().getWindow();
        currentStage.close();

        System.out.println("vous etes deconecté!!!!");
        loginStage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}
