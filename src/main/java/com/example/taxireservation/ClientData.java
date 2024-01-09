package com.example.taxireservation;

public class ClientData {

    private String id;
    private String nom;
    private String prenom;
    private String tel;

    public void setId(String id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getTel() {
        return tel;
    }

    public ClientData(String id, String nom, String prenom, String tel) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
    }
}
