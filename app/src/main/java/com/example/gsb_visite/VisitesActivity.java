package com.example.gsb_visite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gsb_visite.databinding.ActivityVisitesBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitesActivity extends AppCompatActivity {

    private ActivityVisitesBinding binding;
    private Visiteur visiteur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVisitesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Récupérer l'objet visiteur depuis l'intent
        visiteur = (Visiteur) getIntent().getSerializableExtra("visiteur");

        if (visiteur != null) {
            binding.userNameTextView.setText(visiteur.getNom() + " " + visiteur.getPrenom());
            fetchVisites();
        }

        // Gestion du bouton "Créer un rapport de visite"
        binding.createRapportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisitesActivity.this, CreerRapportActivity.class);
                intent.putExtra("visiteur", visiteur);
                startActivity(intent);
            }
        });
    }

    // Appel à l'API pour récupérer toutes les visites
    private void fetchVisites() {
        gsbServices service = RetrofitClientInstance.getRetrofitInstance().create(gsbServices.class);
        Call<List<Visite>> call = service.getVisites("Bearer " + visiteur.getToken());

        call.enqueue(new Callback<List<Visite>>() {
            @Override
            public void onResponse(Call<List<Visite>> call, Response<List<Visite>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Visite> allVisites = response.body();
                    List<Visite> visites = new ArrayList<>();

                    for (Visite v : allVisites) {
                        if (v.getVisiteur() != null &&
                                v.getVisiteur().equals(visiteur.getVisiteurId())) {
                            visites.add(v);
                        }
                    }

                    setupRecycler(visites);
                } else {
                    Toast.makeText(VisitesActivity.this, "Erreur lors du chargement des visites", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Visite>> call, Throwable t) {
                Toast.makeText(VisitesActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Initialisation du RecyclerView avec les visites filtrées
    private void setupRecycler(List<Visite> visites) {
        VisiteAdapter adapter = new VisiteAdapter(visites, visiteur.getToken(), visiteur);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchVisites(); // Recharge les visites à chaque fois qu'on revient sur l'écran
    }

}
