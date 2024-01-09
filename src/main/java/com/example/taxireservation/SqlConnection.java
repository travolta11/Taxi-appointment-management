package com.example.taxireservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

import static java.sql.DriverManager.getConnection;

public class SqlConnection {
    Connection conn=null;
    public static Connection conec(){
        String url="jdbc:mysql://localhost:3306/gestion-taxi";
        String username="root";
        String password="";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn= getConnection(url,username,password);
            return conn;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public  static ObservableList<Reservation> getReservations(){
        Connection conn=conec();
        ObservableList<Reservation> listres= FXCollections.observableArrayList();
        try {
            if (conn != null) {
                PreparedStatement ps = conn.prepareStatement("select * from reservation");
                ResultSet res = ps.executeQuery();

                while (res.next()) {
                    listres.add(new Reservation(

                            res.getInt("id_reservation"),
                            res.getString("client"),
                            res.getString("matriculetaxi"),
                            res.getInt("prix"),
                            res.getString("dateHeure"),
                            res.getString("status")
                    ));
                }

            } else {
                System.out.println("Error ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listres;


    }
    public  static void addReservations(String client,String matricule,int prix,String dateheure,String status){
        Connection conn=conec();
        try {
            if (conn != null) {
                PreparedStatement ps = conn.prepareStatement("insert into reservation (client,matriculetaxi,prix,dateHeure,status) VALUES ( '"+client+"','"+matricule+"',"+prix+",'"+dateheure+"','"+status+"'); ");
                ps.executeUpdate();

            } else {
                System.out.println("Error ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  static void ModifyReservations(int id,String client,String matricule,int prix,String dateheure,String status){
        Connection conn=conec();
        try {
            if (conn != null) {
                PreparedStatement ps = conn.prepareStatement("UPDATE reservation SET client ='"+client+"',matriculetaxi ='"+matricule+"',prix ="+prix+",dateHeure ='"+dateheure+"',status ='"+status+"' WHERE id_reservation="+id+";");
                ps.executeUpdate();

            } else {
                System.out.println("Error ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  static void DeleteReservations(int id){
        Connection conn=conec();
        try {
            if (conn != null) {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM reservation  WHERE id_reservation= "+id+" ;");
                ps.executeUpdate();

            } else {
                System.out.println("Error ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static ObservableList<String> getClients(){
        Connection conn=conec();
        ObservableList<String> clients= FXCollections.observableArrayList();
        try {
            if (conn != null) {
                PreparedStatement ps = conn.prepareStatement("select prenom,nom from client");
                ResultSet res = ps.executeQuery();

                while (res.next()) {
                    String nomprenom=res.getString("prenom")+" "+res.getString("nom");
                    clients.add(nomprenom);
                    //System.out.println(nomprenom);
                }
                return clients;

            } else {
                System.out.println("Error ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static ObservableList<String> getTaxis(){
        Connection conn=conec();
        ObservableList<String> Taxis= FXCollections.observableArrayList();
        try {
            if (conn != null) {
                PreparedStatement ps = conn.prepareStatement("select matricule from taxi");
                ResultSet res = ps.executeQuery();

                while (res.next()) {
                    String matricule=res.getString("matricule");
                    Taxis.add(matricule);
                    //System.out.println(nomprenom);
                }
                return Taxis;

            } else {
                System.out.println("Error ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
