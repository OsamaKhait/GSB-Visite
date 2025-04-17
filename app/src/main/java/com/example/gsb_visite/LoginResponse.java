package com.example.gsb_visite;

import java.io.Serializable;

public class LoginResponse implements Serializable {
    private String message;
    private String token;
    private String visiteur_id;
    private String nom;
    private String prenom;
    private String email;

    public String getVisiteurId() {
        return visiteur_id;
    }

    public void setVisiteurId(String visiteurId) {
        this.visiteur_id = visiteurId;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}