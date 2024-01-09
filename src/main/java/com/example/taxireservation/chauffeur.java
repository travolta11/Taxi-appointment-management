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


public class chauffeur implements Initializable {

        private Connection connection;

        private ChauffeurData selectedChauffeur;

        @FXML
        private Button Return_btn;

        @FXML
        private Button ajouter_btn;

        @FXML
        private Button clear_btn;

        @FXML
        private TextField email;

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
        private TableView<ChauffeurData> tableView;

        @FXML
        private TableColumn<ChauffeurData, String> tab_nom;

        @FXML
        private TableColumn<ChauffeurData, String> tab_prenom;

        @FXML
        private TableColumn<ChauffeurData, String> tab_email;

        @FXML
        private TableColumn<ChauffeurData, String> tab_tel;

        @FXML
        private TableColumn<ChauffeurData, String> tab_id;

// load data from database and initialize in table
        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
                establishDatabaseConnection();

                tab_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
                tab_prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
                tab_email.setCellValueFactory(new PropertyValueFactory<>("email"));
                tab_tel.setCellValueFactory(new PropertyValueFactory<>("tel"));
                tab_id.setCellValueFactory(new PropertyValueFactory<>("id"));

                tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ChauffeurData>() {
                        @Override
                        public void changed(ObservableValue<? extends ChauffeurData> observableValue, ChauffeurData oldValue, ChauffeurData newValue) {
                                // Update the selectedChauffeur when a row is selected
                                selectedChauffeur = newValue;

                                // Populate the input fields with the selected data
                                if (selectedChauffeur != null) {
                                        nom.setText(selectedChauffeur.getNom());
                                        prenom.setText(selectedChauffeur.getPrenom());
                                        email.setText(selectedChauffeur.getEmail());
                                        tel.setText(selectedChauffeur.getTel());
                                }
                        }
                });

                loadDataFromDatabase();


        }

        //connection to database
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

//select logic
        private void loadDataFromDatabase() {
                if (connection != null) {
                        System.out.println("Connected to the database successfully!");
                } else {
                        System.err.println("Failed to connect to the database.");
                }
                String query = "SELECT id, nom, prenom, email, tel FROM chauffeur";

                try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                     ResultSet resultSet = preparedStatement.executeQuery()) {

                        while (resultSet.next()) {
                                String id = resultSet.getString("id");
                                String nom = resultSet.getString("nom");
                                String prenom = resultSet.getString("prenom");
                                String email = resultSet.getString("email");
                                String tel = resultSet.getString("tel");

                                ChauffeurData chauffeurData = new ChauffeurData(id, nom, prenom, email, tel);
                                tableView.getItems().add(chauffeurData);
                        }
                } catch (SQLException e) {
                        e.printStackTrace();
                }
        }
//add chauffeur  logic
        @FXML
        private void handleAjouterClick(ActionEvent event) {
                String nomValue = nom.getText();
                String prenomValue = prenom.getText();
                String emailValue = email.getText();
                String telValue = tel.getText();

                if (nomValue.isEmpty() || prenomValue.isEmpty() || emailValue.isEmpty() || telValue.isEmpty()) {
                        // Display an error message or handle the empty fields appropriately
                        return;
                }

                String insertQuery = "INSERT INTO chauffeur (nom, prenom, email, tel) VALUES (?, ?, ?, ?)";

                try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                        preparedStatement.setString(1, nomValue);
                        preparedStatement.setString(2, prenomValue);
                        preparedStatement.setString(3, emailValue);
                        preparedStatement.setString(4, telValue);

                        int affectedRows = preparedStatement.executeUpdate();

                        if (affectedRows > 0) {
                                // Insert successful
                                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                                        if (generatedKeys.next()) {
                                                // Retrieve the generated ID
                                                int generatedId = generatedKeys.getInt(1);

                                                // Update the ChauffeurData with the generated ID
                                                ChauffeurData chauffeurData = new ChauffeurData(String.valueOf(generatedId), nomValue, prenomValue, emailValue, telValue);
                                                tableView.getItems().add(chauffeurData);

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

                }
        }


//modify chauffeur method
        @FXML
        private void handleModifierClick(ActionEvent event) {
                // Check if a row is selected
                if (selectedChauffeur == null) {

                        return;
                }

                // Get updated values from the input fields
                String updatedNom = nom.getText();
                String updatedPrenom = prenom.getText();
                String updatedEmail = email.getText();
                String updatedTel = tel.getText();

                // Update the ChauffeurData in the TableView
                selectedChauffeur.setNom(updatedNom);
                selectedChauffeur.setPrenom(updatedPrenom);
                selectedChauffeur.setEmail(updatedEmail);
                selectedChauffeur.setTel(updatedTel);

                // Update the ChauffeurData in the database
                String updateQuery = "UPDATE chauffeur SET nom=?, prenom=?, email=?, tel=? WHERE id=?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                        preparedStatement.setString(1, updatedNom);
                        preparedStatement.setString(2, updatedPrenom);
                        preparedStatement.setString(3, updatedEmail);
                        preparedStatement.setString(4, updatedTel);
                        preparedStatement.setString(5, selectedChauffeur.getId());

                        int rowsUpdated = preparedStatement.executeUpdate();

                        if (rowsUpdated > 0) {
                                // Handle successful update
                                System.out.println("ChauffeurData updated successfully!");
                        } else {
                                // Handle update failure
                                System.err.println("Update failed");
                        }
                } catch (SQLException e) {
                        e.printStackTrace();

                }

                // Clear the input fields and reset selectedChauffeur
                refreshTableView();
                clearFields();

                selectedChauffeur = null;
        }

//Delete chauffeur method
        @FXML
        private void handleSupprimerClick(ActionEvent event) {
                // Get the selected ChauffeurData from the table
                ChauffeurData selectedChauffeur = tableView.getSelectionModel().getSelectedItem();

                if (selectedChauffeur == null) {

                        return;
                }

                // Delete the selected row from the database
                deleteRowFromDatabase(selectedChauffeur);

                // Remove the selectedChauffeur from the TableView
                tableView.getItems().remove(selectedChauffeur);

                // Clear input fields
                clearFields();
                refreshTableView();
        }
        private void deleteRowFromDatabase(ChauffeurData chauffeurData) {
                if (connection == null || chauffeurData == null || chauffeurData.getId() == null || chauffeurData.getId().isEmpty()) {

                        return;
                }

                String deleteQuery = "DELETE FROM chauffeur WHERE id = ?";

                try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                        preparedStatement.setString(1, chauffeurData.getId());

                        int affectedRows = preparedStatement.executeUpdate();

                        if (affectedRows > 0) {
                                System.out.println("Row deleted successfully from the database.");
                        } else {
                                System.err.println("Failed to delete row from the database.");
                        }
                } catch (SQLException e) {
                        e.printStackTrace();

                }
        }

//Return button handle (return to apptaxi.fxml)
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

                }
        }


        @FXML
        private void clear_Handle(ActionEvent event){
                clearFields();
        }

        private void clearFields() {
                nom.clear();
                prenom.clear();
                email.clear();
                tel.clear();
        }

        private void refreshTableView() {
                // Clear the items in the TableView
                tableView.getItems().clear();

                // Reload the data from the database
                loadDataFromDatabase();
        }

}
