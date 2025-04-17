package com.example.gsb_visite;

import java.io.Serializable;
import java.util.Date;

public class Visite implements Serializable {
    private String date_visite;
    private String commentaire;
    private String visiteur;   // id du visiteur
    private String praticien;  // id du praticien
    private String motif;      // id du motif

    public String getDate_visite() {
        return date_visite;
    }

    public void setDate_visite(String date_visite) {
        this.date_visite = date_visite;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getVisiteur() {
        return visiteur;
    }

    public void setVisiteur(String visiteur) {
        this.visiteur = visiteur;
    }

    public String getPraticien() {
        return praticien;
    }

    public void setPraticien(String praticien) {
        this.praticien = praticien;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }
}
