package com.example.taxireservation;

import javafx.scene.control.Alert;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginForm {

    @FXML
    private TextField txtUserName;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnok;

    // Database connection
    private Connection connection;

    // Setter method to set the database connection
    public void setDatabaseConnection(Connection connection) {
        this.connection = connection;
    }

    // Event handler for the login button
    @FXML
    void login() {
        String username = txtUserName.getText();
        String password = txtPassword.getText();

        // Validate login
        boolean isValidLogin = isValidLogin(username, password);


        if (isValidLogin) {
            showInfoAlert("Connexion réussie", "Bienvenue sur l'application !");
            openAppTaxiWindow();
        } else {
            showErrorAlert("Identifiant invalide", "Le pseudo ou mot de passe est incorect. Veuillez réessayer.");
        }
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfoAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Method to check if the login is valid
    private boolean isValidLogin(String username, String password) {
        try {

            String sql = "SELECT * FROM login WHERE username = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, password);

                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next(); // Returns true if there is a match
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to open the apptaxi.fxml window
    private void openAppTaxiWindow() {
        // Placeholder: Implement the opening of apptaxi.fxml window according to your application structure
        // Example:
        btnok.getScene().getWindow().hide();

        // Open the apptaxi.fxml window
        HelloApplication.openAppTaxiWindow();

    }
}
