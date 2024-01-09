package com.example.taxireservation;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ControllerModifier implements Initializable {


    @FXML
    private CheckBox checkboxcompl;

    @FXML
    private CheckBox checkboxincompl;

    @FXML
    private ComboBox<String> comboboxclient;

    @FXML
    private ComboBox<String> comboboxtaxi;

    @FXML
    private DatePicker datefield;

    @FXML
    private TextField heurefield;

    @FXML
    private TextField prixfield;
    static Reservation resv;
    public ControllerModifier() {
    }
    public static void init(Reservation resv1) {
        resv=resv1;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



            String heure=resv.getDateHeure().substring(11);
            LocalDateTime dateheure = LocalDateTime.parse(resv.getDateHeure(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            LocalDate date = dateheure.toLocalDate();


            comboboxclient.setValue(resv.getClient());
            comboboxtaxi.setValue(resv.getMatriculetaxi());
            prixfield.setText(String.valueOf(resv.getPrix()));
            datefield.setValue(date);
            heurefield.setText(heure);



        ObservableList<String> clients = SqlConnection.getClients();
        comboboxclient.setItems(clients);
        ObservableList<String> taxis = SqlConnection.getTaxis();
        comboboxtaxi.setItems(taxis);

    }
    public void clientsBox1(){
        ObservableList<String> clients = SqlConnection.getClients();
        comboboxclient.setItems(clients);
    }
    public boolean prix_valide(String prix){
        return prix.matches("^[1-9][0-9]?$|100$");
    }
    public boolean heure_valide(String heure){
        return heure.matches("^([0-1][0-9]|2[0-3]):([0-5][0-9])$");
    }
    public void verify_checkbox(){

        checkboxincompl.setOnAction(event -> {
            if (checkboxincompl.isSelected()) {
                checkboxincompl.setSelected(true);
                checkboxcompl.setSelected(false);
            }
        });

        checkboxcompl.setOnAction(event -> {
            if (checkboxcompl.isSelected()) {
                checkboxcompl.setSelected(true);
                checkboxincompl.setSelected(false);
            }
        });
    }
    public void clickModifier1(ActionEvent event) {

            if(prix_valide(prixfield.getText()) && heure_valide(heurefield.getText()) && (checkboxcompl.isSelected() || checkboxincompl.isSelected()) && datefield.getValue() != null &&comboboxclient.getValue()!=null&&comboboxtaxi.getValue()!=null ){
                String client=comboboxclient.getValue();
                String taxi=comboboxtaxi.getValue();
                int prix=Integer.parseInt(prixfield.getText());
                String dateheure= datefield.getValue()+" "+heurefield.getText();
                String status="";
                if(checkboxincompl.isSelected()){
                    checkboxcompl.setSelected(false);
                    status="incomplete";
                } else if (checkboxcompl.isSelected()) {
                    checkboxincompl.setSelected(false);
                    status="complete";
                }
                //System.out.println(client+" "+taxi+" "+prix+" "+dateheure+" "+status);
                SqlConnection.ModifyReservations(resv.id_reservation,client,taxi,prix,dateheure,status);



                Scene s=comboboxtaxi.getScene();
                Stage sg=(Stage)s.getWindow();
                sg.close();
                new Alert(Alert.AlertType.WARNING,"Veillez rafraîchir la table",ButtonType.OK).showAndWait();
            }
            else {
                new Alert(Alert.AlertType.ERROR,"Vérifier les informations entré",ButtonType.OK).showAndWait();

            }


        }



}
