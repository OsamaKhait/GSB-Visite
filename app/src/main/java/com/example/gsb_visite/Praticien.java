package com.example.gsb_visite;

import java.io.Serializable;
import java.util.List;

public class Praticien implements Serializable {
    private String nom;
    private String prenom;
    private String tel;
    private String email;
    private String rue;
    private String code_postal;
    private String ville;
    private String _id;
    private String specialite; // ✅ nouveau champ
    private List<Visite> visites;

    public Praticien() {}

    public Praticien(String nom, String prenom, String tel, String email, String rue, String code_postal, String ville, String _id) {
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.email = email;
        this.rue = rue;
        this.code_postal = code_postal;
        this.ville = ville;
        this._id = _id;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getCode_postal() {
        return code_postal;
    }

    public void setCode_postal(String code_postal) {
        this.code_postal = code_postal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getSpecialite() {  // ✅ getter spécialité
        return specialite;
    }

    public void setSpecialite(String specialite) {  // ✅ setter spécialité
        this.specialite = specialite;
    }

    public List<Visite> getVisites() {
        return visites;
    }

    public void setVisites(List<Visite> visites) {
        this.visites = visites;
    }

    public String getAdresse() {
        return rue + ", " + code_postal + " " + ville;
    }

    @Override
    public String toString() {
        return "Praticien{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                '}';
    }
}
