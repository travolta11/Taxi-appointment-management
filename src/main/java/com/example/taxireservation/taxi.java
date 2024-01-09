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


public class taxi implements Initializable {

    private Connection connection;

    private TaxiData selectedTaxi;

    @FXML
    private Button Return_btn;

    @FXML
    private Button ajouter_btn;

    @FXML
    private Button clear_btn;

    @FXML
    private TextField nbrplace;

    @FXML
    private Button modifier_btn;

    @FXML
    private TextField marque;

    @FXML
    private TextField matricule;

    @FXML
    private Button supprimer_btn;

    @FXML
    private TextField etat;

    @FXML
    private TableView<TaxiData> tableView;

    @FXML
    private TableColumn<TaxiData, String> tab_marque;

    @FXML
    private TableColumn<TaxiData, String> tab_matricule;

    @FXML
    private TableColumn<TaxiData, String> tab_nbrplace;

    @FXML
    private TableColumn<TaxiData, String> tab_etat;

    @FXML
    private TableColumn<TaxiData, String> tab_id;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        establishDatabaseConnection();

        tab_marque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        tab_matricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        tab_nbrplace.setCellValueFactory(new PropertyValueFactory<>("nbrplace"));
        tab_etat.setCellValueFactory(new PropertyValueFactory<>("etat"));
        tab_id.setCellValueFactory(new PropertyValueFactory<>("id"));

        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TaxiData>() {
            @Override
            public void changed(ObservableValue<? extends TaxiData> observableValue, TaxiData oldValue, TaxiData newValue) {

                selectedTaxi = newValue;

                // Populate the input fields with the selected data
                if (selectedTaxi != null) {
                    marque.setText(selectedTaxi.getMarque());
                    matricule.setText(selectedTaxi.getMatricule());
                    nbrplace.setText(selectedTaxi.getNbrplace());
                    etat.setText(selectedTaxi.getEtat());
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
        String query = "SELECT id, marque, matricule, nbrplace, etat FROM taxi";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String marque = resultSet.getString("marque");
                String matricule = resultSet.getString("matricule");
                String nbrplace = resultSet.getString("nbrplace");
                String etat = resultSet.getString("etat");

                TaxiData TaxiData = new TaxiData(id, marque, matricule, nbrplace, etat);
                tableView.getItems().add(TaxiData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAjouterClick(ActionEvent event) {
        String marqueValue = marque.getText();
        String matriculeValue = matricule.getText();
        String nbrplaceValue = nbrplace.getText();
        String etatValue = etat.getText();

        if (marqueValue.isEmpty() || matriculeValue.isEmpty() || nbrplaceValue.isEmpty() || etatValue.isEmpty()) {
            // Display an error message or handle the empty fields appropriately
            return;
        }

        String insertQuery = "INSERT INTO taxi (marque, matricule, nbrplace, etat) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, marqueValue);
            preparedStatement.setString(2, matriculeValue);
            preparedStatement.setString(3, nbrplaceValue);
            preparedStatement.setString(4, etatValue);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                // Insert successful
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        // Retrieve the generated ID
                        int generatedId = generatedKeys.getInt(1);

                        // Update the TaxiData with the generated ID
                        TaxiData TaxiData = new TaxiData(String.valueOf(generatedId), marqueValue, matriculeValue, nbrplaceValue, etatValue);
                        tableView.getItems().add(TaxiData);

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



    @FXML
    private void handleModifierClick(ActionEvent event) {
        // Check if a row is selected
        if (selectedTaxi == null) {
            // Display an error message or handle appropriately
            return;
        }

        // Get updated values from the input fields
        String updatedMarque = marque.getText();
        String updatedMatricule = matricule.getText();
        String updatedNbrplace = nbrplace.getText();
        String updatedEtat = etat.getText();

        // Update the TaxiData in the TableView
        selectedTaxi.setMarque(updatedMarque);
        selectedTaxi.setMatricule(updatedMatricule);
        selectedTaxi.setNbrplace(updatedNbrplace);
        selectedTaxi.setEtat(updatedEtat);

        // Update the TaxiData in the database
        String updateQuery = "UPDATE taxi SET marque=?, matricule=?, nbrplace=?, etat=? WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, updatedMarque);
            preparedStatement.setString(2, updatedMatricule);
            preparedStatement.setString(3, updatedNbrplace);
            preparedStatement.setString(4, updatedEtat);
            preparedStatement.setString(5, selectedTaxi.getId());

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                // Handle successful update
                System.out.println("TaxiData updated successfully!");
            } else {
                // Handle update failure
                System.err.println("Update failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }

        // Clear the input fields and reset selectedTaxi
        refreshTableView();
        clearFields();

        selectedTaxi = null;
    }


    @FXML
    private void handleSupprimerClick(ActionEvent event) {
        // Get the selected TaxiData from the table
        TaxiData selectedTaxi = tableView.getSelectionModel().getSelectedItem();

        if (selectedTaxi == null) {

            return;
        }

        // Delete the selected row from the database
        deleteRowFromDatabase(selectedTaxi);

        // Remove the selectedTaxi from the TableView
        tableView.getItems().remove(selectedTaxi);

        // Clear input fields
        clearFields();
        refreshTableView();
    }
    private void deleteRowFromDatabase(TaxiData TaxiData) {
        if (connection == null || TaxiData == null || TaxiData.getId() == null || TaxiData.getId().isEmpty()) {

            return;
        }

        String deleteQuery = "DELETE FROM taxi WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setString(1, TaxiData.getId());

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
        marque.clear();
        matricule.clear();
        nbrplace.clear();
        etat.clear();
    }

    private void refreshTableView() {
        // Clear the items in the TableView
        tableView.getItems().clear();

        // Reload the data from the database
        loadDataFromDatabase();
    }

}
