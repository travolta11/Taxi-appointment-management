package com.example.taxireservation;

public class TaxiData {
    private String id;
    private String marque;
    private String matricule;
    private String nbrplace;
    private String etat;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TaxiData(String id, String marque, String matricule, String nbrplace, String etat) {
        this.id=id;
        this.marque = marque;
        this.matricule = matricule;
        this.nbrplace = nbrplace;
        this.etat = etat;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNbrplace() {
        return nbrplace;
    }

    public void setNbrplace(String nbrplace) {
        this.nbrplace = nbrplace;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }
}
