package com.example.gsb_visite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Call;
import android.util.Log;

import com.example.gsb_visite.databinding.ActivityLoginBinding;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private Visiteur visiteur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visiteur = new Visiteur();
                visiteur.setEmail(binding.emailEditText.getText().toString());
                visiteur.setPassword(binding.passwordEditText.getText().toString());

                gsbServices service = RetrofitClientInstance.getRetrofitInstance().create(gsbServices.class);
                Call<LoginResponse> call = service.login(visiteur);

                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Erreur d'authentification", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        LoginResponse loginResponse = response.body();
                        if (loginResponse == null) {
                            Toast.makeText(MainActivity.this, "Erreur d'authentification", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Stockez le token dans l'objet visiteur
                        visiteur.setToken(loginResponse.getToken());

                        // Si vous avez modifié l'API pour renvoyer les infos du visiteur
                        if (loginResponse.getVisiteurId() != null) {
                            visiteur.setToken(loginResponse.getToken());
                            visiteur.setVisiteurId(loginResponse.getVisiteurId());
                            visiteur.setNom(loginResponse.getNom());
                            visiteur.setPrenom(loginResponse.getPrenom());
                            visiteur.setEmail(loginResponse.getEmail());

                            Toast.makeText(MainActivity.this, "Bienvenue " + loginResponse.getPrenom(), Toast.LENGTH_SHORT).show();

// Passer à l'écran d'accueil
                            Intent intent = new Intent(MainActivity.this, Acceuil.class);
                            intent.putExtra("visiteur", visiteur);
                            startActivity(intent);
                        } else {
                            // Sinon, faire la deuxième requête pour récupérer les infos du visiteur
                            getVisiteur(loginResponse.getVisiteurId());
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Une erreur est survenue : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    public void getVisiteur(String visiteurId) {
        gsbServices service = RetrofitClientInstance.getRetrofitInstance().create(gsbServices.class);
        Call<Visiteur> call = service.getVisiteur("Bearer " + visiteur.getToken(), visiteurId);

        // Ajouter des logs pour déboguer
        Log.d("API_DEBUG", "Appel getVisiteur avec id: " + visiteurId);
        Log.d("API_DEBUG", "Token: " + visiteur.getToken());

        call.enqueue(new Callback<Visiteur>() {
            @Override
            public void onResponse(Call<Visiteur> call, Response<Visiteur> response) {
                if (!response.isSuccessful()) {
                    Log.e("API_ERROR", "Code erreur: " + response.code());
                    Log.e("API_ERROR", "Message erreur: " + response.message());

                    try {
                        Log.e("API_ERROR", "Erreur corps: " + response.errorBody().string());
                    } catch (Exception e) {
                        Log.e("API_ERROR", "Impossible de lire errorBody");
                    }

                    Toast.makeText(MainActivity.this, "Erreur de récupération des informations: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Visiteur visiteurInfo = response.body();
                if (visiteurInfo != null) {
                    Log.d("API_SUCCESS", "Visiteur info reçu: " + visiteurInfo.getNom() + " " + visiteurInfo.getPrenom());
                    // Transférer les infos à l'objet visiteur existant
                    visiteur.setNom(visiteurInfo.getNom());
                    visiteur.setPrenom(visiteurInfo.getPrenom());
                    // etc.

                    Intent intent = new Intent(MainActivity.this, Acceuil.class);
                    intent.putExtra("visiteur", visiteur);

                    startActivity(intent);
                } else {
                    Log.e("API_ERROR", "Visiteur info est null");
                    Toast.makeText(MainActivity.this, "Informations vides reçues", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Visiteur> call, Throwable t) {
                Log.e("API_FAILURE", "Erreur: " + t.getMessage(), t);
                Toast.makeText(MainActivity.this, "Une erreur est survenue lors de la récupération des informations du visiteur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}