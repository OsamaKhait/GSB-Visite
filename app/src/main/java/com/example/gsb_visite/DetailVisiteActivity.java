package com.example.gsb_visite;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailVisiteActivity extends AppCompatActivity {

    private TextView userNameTextView, textDate, textCommentaire, textPraticien, textMotif;
    private Visite visite;
    private Visiteur visiteur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_visite);

        // Liaison des vues
        userNameTextView = findViewById(R.id.userNameTextView);
        textDate = findViewById(R.id.textDate);
        textCommentaire = findViewById(R.id.textCommentaire);
        textPraticien = findViewById(R.id.textPraticien);
        textMotif = findViewById(R.id.textMotif);

        // Récupération des données depuis l'intent
        visiteur = (Visiteur) getIntent().getSerializableExtra("visiteur");
        visite = (Visite) getIntent().getSerializableExtra("visite");

        if (visiteur != null) {
            String fullName = visiteur.getPrenom() + " " + visiteur.getNom();
            userNameTextView.setText(fullName);
        }

        if (visite != null) {
            textDate.setText(visite.getDate_visite());
            textCommentaire.setText(visite.getCommentaire());

            chargerNomPraticien(visite.getPraticien());
            chargerMotif(visite.getMotif());
        } else {
            Toast.makeText(this, "Erreur : aucune visite reçue", Toast.LENGTH_SHORT).show();
        }
    }

    private void chargerNomPraticien(String praticienId) {
        gsbServices service = RetrofitClientInstance.getRetrofitInstance().create(gsbServices.class);
        Call<Praticien> call = service.getPraticienDetails("Bearer " + visiteur.getToken(), praticienId);

        call.enqueue(new Callback<Praticien>() {
            @Override
            public void onResponse(Call<Praticien> call, Response<Praticien> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Praticien praticien = response.body();
                    textPraticien.setText(praticien.getPrenom() + " " + praticien.getNom());
                } else {
                    textPraticien.setText("Praticien inconnu");
                }
            }

            @Override
            public void onFailure(Call<Praticien> call, Throwable t) {
                textPraticien.setText("Erreur de chargement praticien");
            }
        });
    }

    private void chargerMotif(String motifId) {
        gsbServices service = RetrofitClientInstance.getRetrofitInstance().create(gsbServices.class);
        Call<Motif> call = service.getMotifDetails("Bearer " + visiteur.getToken(), motifId);

        call.enqueue(new Callback<Motif>() {
            @Override
            public void onResponse(Call<Motif> call, Response<Motif> response) {
                if (response.isSuccessful() && response.body() != null) {
                    textMotif.setText(response.body().getLibelle());
                } else {
                    textMotif.setText("Motif inconnu");
                }
            }

            @Override
            public void onFailure(Call<Motif> call, Throwable t) {
                textMotif.setText("Erreur de chargement motif");
            }
        });
    }
}
