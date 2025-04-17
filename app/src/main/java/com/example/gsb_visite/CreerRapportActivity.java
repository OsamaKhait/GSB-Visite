package com.example.gsb_visite;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gsb_visite.databinding.ActivityCreerRapportBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreerRapportActivity extends AppCompatActivity {

    private ActivityCreerRapportBinding binding;
    private Visiteur visiteur;
    private List<Praticien> praticienList = new ArrayList<>();
    private List<Motif> motifList = new ArrayList<>();
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreerRapportBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        visiteur = (Visiteur) getIntent().getSerializableExtra("visiteur");

        if (visiteur != null) {
            binding.textViewName.setText(visiteur.getNom() + " " + visiteur.getPrenom());
        } else {
            Toast.makeText(this, "Erreur : visiteur null", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        binding.dateEditText.setOnClickListener(v -> showDatePicker());

        loadPraticiens();
        loadMotifs();

        binding.btnSubmitRapport.setOnClickListener(v -> envoyerRapport());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    // Format ISO complet pour correspondre à l'attente probable de l'API
                    selectedDate = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth);
                    binding.dateEditText.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void loadPraticiens() {
        gsbServices service = RetrofitClientInstance.getRetrofitInstance().create(gsbServices.class);
        Call<List<Praticien>> call = service.getPraticiens("Bearer " + visiteur.getToken());

        call.enqueue(new Callback<List<Praticien>>() {
            @Override
            public void onResponse(Call<List<Praticien>> call, Response<List<Praticien>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    praticienList = response.body();
                    List<String> noms = praticienList.stream()
                            .map(p -> p.getNom() + " " + p.getPrenom())
                            .collect(Collectors.toList());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(CreerRapportActivity.this, android.R.layout.simple_spinner_item, noms);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.praticienSpinner.setAdapter(adapter);
                } else {
                    Toast.makeText(CreerRapportActivity.this, "Erreur chargement praticiens", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Praticien>> call, Throwable t) {
                Toast.makeText(CreerRapportActivity.this, "Erreur réseau praticiens : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMotifs() {
        gsbServices service = RetrofitClientInstance.getRetrofitInstance().create(gsbServices.class);
        Call<List<Motif>> call = service.getMotifs("Bearer " + visiteur.getToken());

        call.enqueue(new Callback<List<Motif>>() {
            @Override
            public void onResponse(Call<List<Motif>> call, Response<List<Motif>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    motifList = response.body();
                    List<String> libelles = motifList.stream()
                            .map(Motif::getLibelle)
                            .collect(Collectors.toList());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(CreerRapportActivity.this, android.R.layout.simple_spinner_item, libelles);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.motifSpinner.setAdapter(adapter);
                } else {
                    Toast.makeText(CreerRapportActivity.this, "Erreur chargement motifs", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Motif>> call, Throwable t) {
                Toast.makeText(CreerRapportActivity.this, "Erreur réseau motifs : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void envoyerRapport() {
        String commentaire = binding.commentaireEditText.getText().toString();
        int praticienIndex = binding.praticienSpinner.getSelectedItemPosition();
        int motifIndex = binding.motifSpinner.getSelectedItemPosition();

        if (selectedDate == null || praticienIndex == -1 || motifIndex == -1 || commentaire.isEmpty()) {
            Toast.makeText(this, "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show();
            return;
        }

        Visite visite = new Visite();
        visite.setDate_visite(selectedDate);
        visite.setCommentaire(commentaire);
        visite.setVisiteur(visiteur.getVisiteurId());
        visite.setPraticien(praticienList.get(praticienIndex).getId());
        visite.setMotif(motifList.get(motifIndex).getId());

        Log.d("JSON_DEBUG", new Gson().toJson(visite));
        Log.d("DEBUG_MOTIF_ID", "motif=" + motifList.get(motifIndex).getId());


        gsbServices service = RetrofitClientInstance.getRetrofitInstance().create(gsbServices.class);
        Call<Void> call = service.creerRapport("Bearer " + visiteur.getToken(), visite);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CreerRapportActivity.this, "Rapport envoyé avec succès", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Toast.makeText(CreerRapportActivity.this, "Erreur " + response.code() + " : " + errorBody, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(CreerRapportActivity.this, "Erreur inconnue : " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CreerRapportActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
