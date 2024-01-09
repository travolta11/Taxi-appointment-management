package com.example.taxireservation;

public class Reservation {
 public int id_reservation;
 public String client;
 public String matriculetaxi;
 public int prix;
 public String dateheure;
 public String status;

    @Override
    public String toString() {
        return "Reservation{" +
                "id_reservation=" + id_reservation +
                ", client='" + client + '\'' +
                ", matriculetaxi='" + matriculetaxi + '\'' +
                ", prix=" + prix +
                ", dateHeure='" + dateheure + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public int getId_reservation() {
        return id_reservation;
    }

    public String getClient() {
        return client;
    }

    public String getMatriculetaxi() {
        return matriculetaxi;
    }

    public int getPrix() {
        return prix;
    }

    public String getDateHeure() {
        return dateheure;
    }

    public String getStatus() {
        return status;
    }

    public Reservation(int id_reservation, String client, String matriculetaxi, int prix, String dateHeure, String status) {
        this.id_reservation = id_reservation;
        this.client = client;
        this.matriculetaxi = matriculetaxi;
        this.prix = prix;
        this.dateheure = dateHeure;
        this.status = status;
    }
}
