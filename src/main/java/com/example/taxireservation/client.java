package com.example.taxireservation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


public class client implements Initializable {

    private Connection connection;

    private ClientData selectedClient;

    @FXML
    private Button Return_btn;

    @FXML
    private Button ajouter_btn;

    @FXML
    private Button clear_btn;



    @FXML
    private Button modifier_btn;

    @FXML
    private TextField nom;

    @FXML
    private TextField prenom;

    @FXML
    private Button supprimer_btn;

    @FXML
    private TextField tel;

    @FXML
    private TableView<ClientData> tableView;

    @FXML
    private TableColumn<ClientData, String> tab_nom;

    @FXML
    private TableColumn<ClientData, String> tab_prenom;

    @FXML
    private TableColumn<ClientData, String> tab_email;

    @FXML
    private TableColumn<ClientData, String> tab_tel;

    @FXML
    private TableColumn<ClientData, String> tab_id;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        establishDatabaseConnection();

        tab_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        tab_prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        tab_tel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        tab_id.setCellValueFactory(new PropertyValueFactory<>("id"));

        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ClientData>() {
            @Override
            public void changed(ObservableValue<? extends ClientData> observableValue, ClientData oldValue, ClientData newValue) {
                // Update the selectedClient when a row is selected
                selectedClient = newValue;


                if (selectedClient != null) {
                    nom.setText(selectedClient.getNom());
                    prenom.setText(selectedClient.getPrenom());
                    tel.setText(selectedClient.getTel());
                }
            }
        });

        loadDataFromDatabase();


    }

    private void establishDatabaseConnection() {
        String url = "jdbc:mysql://localhost:3306/gestion-taxi";
        String username = "root";
        String password = "";

        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database successfully!");
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database.");
            e.printStackTrace();
        }
    }

    public void onClose() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void loadDataFromDatabase() {
        if (connection != null) {
            System.out.println("Connected to the database successfully!");
        } else {
            System.err.println("Failed to connect to the database.");
        }
        String query = "SELECT id_client, nom, prenom, num_tel FROM client";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String id = resultSet.getString("id_client");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String tel = resultSet.getString("num_tel");

                ClientData ClientData = new ClientData(id, nom, prenom,  tel);
                tableView.getItems().add(ClientData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//ajouter
    @FXML
    private void handleAjouterClick(ActionEvent event) {
        String nomValue = nom.getText();
        String prenomValue = prenom.getText();

        String telValue = tel.getText();

        if (nomValue.isEmpty() || prenomValue.isEmpty() ||  telValue.isEmpty()) {

            return;
        }

        String insertQuery = "INSERT INTO client (nom, prenom, num_tel) VALUES (?, ?,  ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, nomValue);
            preparedStatement.setString(2, prenomValue);

            preparedStatement.setString(3, telValue);

            int affectedRows = preparedStatement.executeUpdate();
//issam
            if (affectedRows > 0) {
                // Insert successful
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        // Retrieve the generated ID
                        int generatedId = generatedKeys.getInt(1);

                        // Update the ClientData with the generated ID
                        ClientData ClientData = new ClientData(String.valueOf(generatedId), nomValue, prenomValue, telValue);
                        tableView.getItems().add(ClientData);

                        // Clear input fields
                        clearFields();
                    } else {
                        // Handle the case where no ID was generated
                        System.err.println("No ID was generated for the new record.");
                    }
                }
            } else {
                // Handle insert failure
                System.err.println("insert failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }
    }


//modifier
    @FXML
    private void handleModifierClick(ActionEvent event) {
        // Check if a row is selected
        if (selectedClient == null) {
            // Display an error message or handle appropriately
            return;
        }

        // Get updated values from the input fields
        String updatedNom = nom.getText();
        String updatedPrenom = prenom.getText();

        String updatedTel = tel.getText();

        // Update the ClientData in the TableView
        selectedClient.setNom(updatedNom);
        selectedClient.setPrenom(updatedPrenom);

        selectedClient.setTel(updatedTel);

        // Update the ClientData in the database
        String updateQuery = "UPDATE client SET nom=?, prenom=?,  num_tel=? WHERE id_client=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, updatedNom);
            preparedStatement.setString(2, updatedPrenom);

            preparedStatement.setString(3, updatedTel);
            preparedStatement.setString(4, selectedClient.getId());

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                // Handle successful update
                System.out.println("ClientData updated successfully!");
            } else {
                // Handle update failure
                System.err.println("Update failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }

        // Clear the input fields and reset selectedClient
        refreshTableView();
        clearFields();

        selectedClient = null;
    }


    @FXML
    private void handleSupprimerClick(ActionEvent event) {
        // Get the selected ClientData from the table
        ClientData selectedClient = tableView.getSelectionModel().getSelectedItem();

        if (selectedClient == null) {

            return;
        }

        // Delete the selected row from the database
        deleteRowFromDatabase(selectedClient);

        // Remove the selectedClient from the TableView
        tableView.getItems().remove(selectedClient);

        // Clear input fields
        clearFields();
        refreshTableView();
    }
    private void deleteRowFromDatabase(ClientData ClientData) {
        if (connection == null || ClientData == null || ClientData.getId() == null || ClientData.getId().isEmpty()) {

            return;
        }

        String deleteQuery = "DELETE FROM client WHERE id_client = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setString(1, ClientData.getId());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Row deleted successfully from the database.");
            } else {
                System.err.println("Failed to delete row from the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }
    }


    @FXML
    private void handleReturnClick(ActionEvent event) {
        // Close the current window
        Stage stage = (Stage) Return_btn.getScene().getWindow();
        stage.close();

        // Open the apptaxi.fxml window
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("apptaxi.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }
    }


    @FXML
    private void clear_Handle(ActionEvent event){
        clearFields();
    }

    private void clearFields() {
        nom.clear();
        prenom.clear();

        tel.clear();
    }

    private void refreshTableView() {
        // Clear the items in the TableView
        tableView.getItems().clear();

        // Reload the data from the database
        loadDataFromDatabase();
    }

}
