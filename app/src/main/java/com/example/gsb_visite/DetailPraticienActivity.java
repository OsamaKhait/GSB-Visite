package com.example.gsb_visite;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailPraticienActivity extends AppCompatActivity {

    private TextView nomTextView, prenomTextView, adresseTextView, telTextView, specialiteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_praticien);

        nomTextView = findViewById(R.id.nomTextView);
        prenomTextView = findViewById(R.id.prenomTextView);
        adresseTextView = findViewById(R.id.adresseTextView);
        telTextView = findViewById(R.id.telTextView);
        specialiteTextView = findViewById(R.id.specialiteTextView);

        Praticien praticien = (Praticien) getIntent().getSerializableExtra("praticien");

        if (praticien != null) {
            nomTextView.setText(praticien.getNom());
            prenomTextView.setText(praticien.getPrenom());
            adresseTextView.setText(praticien.getAdresse());
            telTextView.setText(praticien.getTel());
            specialiteTextView.setText(praticien.getSpecialite());
        }
    }
}
