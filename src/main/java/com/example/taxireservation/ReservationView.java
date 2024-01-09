package com.example.taxireservation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class ReservationView extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/reservationtaxi2/Reservation.fxml"));


        Scene scene = new Scene(root);
        stage.setTitle("Réservation d'un Taxi");
        stage.setScene(scene);
        stage.setResizable(false);
        //logo
        Image icon=new Image("file:///C:/Users/Lenovo/eclipse-workspace/taxi-reservation/src/main/java/com/example/taxireservation/findtaxi.png");
        stage.getIcons().add(icon);
        stage.show();


    }


    public static void main(String[] args) {
        launch();


    }
}