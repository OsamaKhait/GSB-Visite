package com.example.gsb_visite;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Motif implements Serializable {

    @SerializedName("_id")
    private String id;

    private String libelle;

    public String getId() {
        return id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }
}
