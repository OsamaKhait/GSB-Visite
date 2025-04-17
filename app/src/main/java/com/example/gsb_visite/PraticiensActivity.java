package com.example.gsb_visite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gsb_visite.databinding.ActivityPraticiensBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PraticiensActivity extends AppCompatActivity implements PraticienAdapter.OnPraticienClickListener {

    private ActivityPraticiensBinding binding;
    private Visiteur visiteur;
    private ArrayList<Praticien> allPraticiens;
    private TreeMap<Character, ArrayList<Praticien>> praticiensByLetter;
    private PraticienAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPraticiensBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Get visiteur from intent
        visiteur = (Visiteur) getIntent().getSerializableExtra("visiteur");

        // Set user name in the header
        binding.userNameTextView.setText(visiteur != null ? visiteur.getNom() + " " + visiteur.getPrenom() : "Utilisateur");

        // Setup ProgressBar
        progressBar = binding.progressBar;
        progressBar.setVisibility(View.VISIBLE);

        // Setup RecyclerView
        setupRecyclerView();

        // Initialize data from API
        initPraticiensList();

        // Setup search functionality
        setupSearch();
    }

    private void setupRecyclerView() {
        adapter = new PraticienAdapter(this);
        binding.praticiensRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.praticiensRecyclerView.setAdapter(adapter);
    }

    private void initPraticiensList() {
        // Check if visiteur and token are available
        if (visiteur == null || visiteur.getToken() == null) {
            Toast.makeText(this, "Erreur : Utilisateur non authentifié", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        // Create Retrofit service
        gsbServices service = RetrofitClientInstance.getRetrofitInstance().create(gsbServices.class);

        // Make API call to fetch praticiens
        Call<List<Praticien>> call = service.getPraticiens("Bearer " + visiteur.getToken());

        call.enqueue(new Callback<List<Praticien>>() {
            @Override
            public void onResponse(Call<List<Praticien>> call, Response<List<Praticien>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    allPraticiens = new ArrayList<>(response.body());

                    if (allPraticiens.isEmpty()) {
                        Toast.makeText(PraticiensActivity.this, "Aucun praticien trouvé", Toast.LENGTH_SHORT).show();
                    } else {
                        organizePraticiensByLetter();
                    }
                } else {
                    String errorMessage = response.code() == 401 ?
                            "Non autorisé. Veuillez vous reconnecter." :
                            "Erreur de chargement des praticiens : " + response.code();

                    Toast.makeText(PraticiensActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Praticien>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(PraticiensActivity.this,
                        "Erreur réseau : " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void organizePraticiensByLetter() {
        praticiensByLetter = new TreeMap<>();

        // Group praticiens by first letter of last name
        for (Praticien praticien : allPraticiens) {
            char firstLetter = praticien.getNom().toUpperCase().charAt(0);
            if (!praticiensByLetter.containsKey(firstLetter)) {
                praticiensByLetter.put(firstLetter, new ArrayList<>());
            }
            praticiensByLetter.get(firstLetter).add(praticien);
        }

        // Sort each group alphabetically
        for (ArrayList<Praticien> group : praticiensByLetter.values()) {
            Collections.sort(group, new Comparator<Praticien>() {
                @Override
                public int compare(Praticien p1, Praticien p2) {
                    return p1.getNom().compareToIgnoreCase(p2.getNom());
                }
            });
        }

        // Display all praticiens
        updatePraticiensList(praticiensByLetter);
    }

    private void updatePraticiensList(TreeMap<Character, ArrayList<Praticien>> praticienMap) {
        List<Object> items = new ArrayList<>();

        for (Character letter : praticienMap.keySet()) {
            items.add(letter); // Add letter header
            items.addAll(praticienMap.get(letter)); // Add all praticiens for this letter
        }

        adapter.setItems(items);
    }

    @Override
    public void onPraticienClick(Praticien praticien) {
        // On lance l'activité de détail en passant le praticien sélectionné
        Intent intent = new Intent(PraticiensActivity.this, DetailPraticienActivity.class);
        intent.putExtra("praticien", praticien); // envoie l'objet praticien
        startActivity(intent);
    }


    private void setupSearch() {
        EditText searchEditText = binding.searchEditText;
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPraticiens(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });
    }

    private void filterPraticiens(String query) {
        if (query.isEmpty()) {
            // Show all praticiens
            updatePraticiensList(praticiensByLetter);
        } else {
            // Filter praticiens by name
            TreeMap<Character, ArrayList<Praticien>> filteredMap = new TreeMap<>();

            for (Praticien praticien : allPraticiens) {
                String fullName = praticien.getNom() + " " + praticien.getPrenom();
                if (fullName.toLowerCase().contains(query.toLowerCase())) {
                    char firstLetter = praticien.getNom().toUpperCase().charAt(0);
                    if (!filteredMap.containsKey(firstLetter)) {
                        filteredMap.put(firstLetter, new ArrayList<>());
                    }
                    filteredMap.get(firstLetter).add(praticien);
                }
            }

            // Display filtered praticiens
            updatePraticiensList(filteredMap);
        }
    }
}