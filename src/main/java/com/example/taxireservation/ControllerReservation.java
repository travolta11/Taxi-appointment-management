package com.example.taxireservation;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;

public class ControllerReservation implements Initializable {

    public static int id_resv;
    public static String clt;
    public static String mtr;
    public static int pr;
    public static  String DH;
    public static String st;
    @FXML
    private TableColumn<Reservation, Integer> idreservation;
    @FXML
    private TableColumn<Reservation, String> client;

    @FXML
    private TableColumn<Reservation, String> dateheure;

    @FXML
    private ComboBox<String> comboboxclient;

    @FXML
    private ComboBox<String> comboboxtaxi;

    @FXML
    private TextField prixfield;
    @FXML
    private TextField heurefield;
    @FXML
    private CheckBox checkboxcompl;
    @FXML
    private DatePicker datefield;

    @FXML
    private CheckBox checkboxincompl;
    @FXML
    private TableColumn<Reservation, String> matriculetaxi;

    @FXML
    private TableColumn<Reservation, Integer> prix;

    @FXML
    private TableColumn<Reservation, String> status;

    @FXML
    private TableView<Reservation> tablereservation;
    @FXML
    private Button Return_btn;

   static int index=-1;
    Connection conn=null;
    ResultSet res=null;
    PreparedStatement ps=null;




    @Override
    public  void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Reservation> listres = SqlConnection.getReservations();
        tablereservation.setItems(listres);
        idreservation.setCellValueFactory(new PropertyValueFactory<Reservation,Integer>("id_reservation"));
        client.setCellValueFactory(new PropertyValueFactory<Reservation,String>("client"));
        matriculetaxi.setCellValueFactory(new PropertyValueFactory<Reservation,String>("matriculetaxi"));
        prix.setCellValueFactory(new PropertyValueFactory<Reservation,Integer>("prix"));
        dateheure.setCellValueFactory(new PropertyValueFactory<Reservation,String>("dateHeure"));
        status.setCellValueFactory(new PropertyValueFactory<Reservation,String>("status"));
        ObservableList<String> clients = SqlConnection.getClients();
        comboboxclient.setItems(clients);
        ObservableList<String> taxis = SqlConnection.getTaxis();
        comboboxtaxi.setItems(taxis);

    }
    public void clientsBox(){
        ObservableList<String> clients = SqlConnection.getClients();
        comboboxclient.setItems(clients);
    }


    //regex functions
    public boolean prix_valide(String prix){
        return prix.matches("^[1-9][0-9]?$|100$");
    }
    public boolean heure_valide(String heure){
        return heure.matches("^([0-1][0-9]|2[0-3]):([0-5][0-9])$");
    }
    public void verify_checkbox(){
        checkboxincompl.setOnAction(event -> {
            if (checkboxincompl.isSelected()) {
                checkboxcompl.setSelected(false);
            }
        });

        checkboxcompl.setOnAction(event -> {
            if (checkboxcompl.isSelected()) {
                checkboxincompl.setSelected(false);
            }
        });
    }
    public void clickAjouter(){
        if(prix_valide(prixfield.getText()) && heure_valide(heurefield.getText()) && (checkboxcompl.isSelected() || checkboxincompl.isSelected()) && datefield.getValue() != null &&comboboxclient.getValue()!=null&&comboboxtaxi.getValue()!=null ){
                String client=comboboxclient.getValue();
                String taxi=comboboxtaxi.getValue();
                int prix=Integer.parseInt(prixfield.getText());
                String dateheure= datefield.getValue()+" "+heurefield.getText();
                String status="";

                if(checkboxincompl.isSelected()){
                     status="incomplete";
                } else if (checkboxcompl.isSelected()) {
                     status="complete";
                }
                //System.out.println(client+" "+taxi+" "+prix+" "+dateheure+" "+status);
            SqlConnection.addReservations(client,taxi,prix,dateheure,status);
            ObservableList<Reservation> listres = SqlConnection.getReservations();
            tablereservation.setItems(listres);
                //clearing Values
            comboboxclient.setValue(null);
            comboboxtaxi.setValue(null);
            prixfield.setText(null);
            heurefield.setText(null);
            datefield.setValue(null);
            checkboxincompl.setSelected(false);
            checkboxcompl.setSelected(false);

        }
        else {
            new Alert(Alert.AlertType.ERROR,"Vérifier les informations entré",ButtonType.OK).showAndWait();

        }


    }
    public  Reservation getInfoReservation(){
        index=tablereservation.getSelectionModel().getSelectedIndex();
        if(index <= -1){
            return null;
        }
        else {
             id_resv=idreservation.getCellData(index);
             clt=client.getCellData(index).toString();
             mtr=matriculetaxi.getCellData(index).toString();
             pr=prix.getCellData(index);
             DH=dateheure.getCellData(index).toString();
             st=status.getCellData(index).toString();
            System.out.println("-----------------------");
            System.out.println(id_resv+" "+clt+" "+mtr+" "+pr+" "+DH+" "+st);
            return new Reservation(id_resv,clt,mtr,pr,DH,st);
        }

    }
    public    void  clickModifier2() throws IOException{
        if(this.getInfoReservation()!=null){
            try {
                ControllerModifier.init(this.getInfoReservation());
                Parent root2 = FXMLLoader.load(getClass().getResource("Modifier.fxml"));
                Stage stage2 = new Stage();
                stage2.setTitle("Modifier");
                Scene scene2 = new Scene(root2);
                stage2.setScene(scene2);
                stage2.setResizable(false);
                //logo
                Image icon2 = new Image("file:///C:/Users/Lenovo/eclipse-workspace/taxi-reservation/src/main/java/com/example/taxireservation/findtaxi.png");
                stage2.getIcons().add(icon2);
                stage2.show();


            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else {
            new Alert(Alert.AlertType.ERROR,"Veillez séléctionner une réservation",ButtonType.OK).showAndWait();
        }

    }
    public void refresh(){
        ObservableList<Reservation> listres = SqlConnection.getReservations();
        tablereservation.setItems(listres);
    }
    public    void  clickSupprimer() throws IOException{
        if(this.getInfoReservation()!=null){
            try {
                Optional<ButtonType> a=new Alert(Alert.AlertType.WARNING,"Voulez vous le supprimer ?",ButtonType.OK,ButtonType.CANCEL).showAndWait();
                String test=a.get().getText();
                if(test.equals("OK")){
                    SqlConnection.DeleteReservations(getInfoReservation().getId_reservation());
                    refresh();
                }
                else if(test.equals("Cancel")){
                    refresh();

                }



            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else {
            new Alert(Alert.AlertType.ERROR,"Veillez séléctionner une réservation",ButtonType.OK).showAndWait();
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

}

