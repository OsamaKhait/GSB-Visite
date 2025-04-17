package com.example.gsb_visite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.gsb_visite.databinding.ActivityAcceuilBinding;

public class Acceuil extends AppCompatActivity {

    private ActivityAcceuilBinding binding;
    private Visiteur visiteur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAcceuilBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Get visiteur from intent
        visiteur = (Visiteur) getIntent().getSerializableExtra("visiteur");

        // Set user name in the header
        if (visiteur != null) {
            binding.textViewName.setText(visiteur.getNom() + " " + visiteur.getPrenom());
        }

        // Set up button click listener to navigate to Praticiens screen
        binding.btnMesPraticiens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Acceuil.this, PraticiensActivity.class);
                intent.putExtra("visiteur", visiteur);
                startActivity(intent);
            }
        });

        binding.btnDernieresVisites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Acceuil.this, VisitesActivity.class);
                intent.putExtra("visiteur", visiteur);
                startActivity(intent);
            }
        });

    }
}