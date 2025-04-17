package com.example.gsb_visite;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisiteAdapter extends RecyclerView.Adapter<VisiteAdapter.VisiteViewHolder> {

    private List<Visite> visites;
    private String token;
    private Visiteur visiteur;

    public VisiteAdapter(List<Visite> visites, String token, Visiteur visiteur) {
        this.visites = visites;
        this.token = token;
        this.visiteur = visiteur;
    }


    @NonNull
    @Override
    public VisiteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_visite, parent, false);
        return new VisiteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisiteViewHolder holder, int position) {
        Visite visite = visites.get(position);

        String dateStr = visite.getDate_visite(); // présumé au format "yyyy-MM-dd'T'HH:mm:ss"
        try {
            SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date date = apiFormat.parse(dateStr);

            SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault());
            holder.dateText.setText(displayFormat.format(date));
        } catch (ParseException | NullPointerException e) {
            holder.dateText.setText(dateStr != null ? dateStr : "Date inconnue");
        }

        // Appel API pour récupérer le praticien
        String praticienId = visite.getPraticien();
        gsbServices service = RetrofitClientInstance.getRetrofitInstance().create(gsbServices.class);
        Call<Praticien> call = service.getPraticienDetails("Bearer " + token, praticienId);

        call.enqueue(new Callback<Praticien>() {
            @Override
            public void onResponse(Call<Praticien> call, Response<Praticien> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Praticien praticien = response.body();
                    String title = praticien.getNom().equalsIgnoreCase("Bereny") ? "Mme " : "Dr ";
                    holder.nomPraticien.setText(title + praticien.getPrenom() + " " + praticien.getNom());
                    // Glide.with(holder.praticienImage.getContext()).load(praticien.getPhotoUrl()).into(holder.praticienImage); // si dispo
                } else {
                    holder.nomPraticien.setText("Praticien inconnu");
                }
            }

            @Override
            public void onFailure(Call<Praticien> call, Throwable t) {
                holder.nomPraticien.setText("Erreur de chargement");
            }
        });
        holder.cardView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, DetailVisiteActivity.class);
            intent.putExtra("visite", visite);        // visite sélectionnée
            intent.putExtra("visiteur", visiteur);    // visiteur connecté
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return visites.size();
    }

    public static class VisiteViewHolder extends RecyclerView.ViewHolder {
        TextView dateText, nomPraticien;
        ImageView praticienImage;
        CardView cardView;

        public VisiteViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.dateText);
            nomPraticien = itemView.findViewById(R.id.nomPraticienText);
            praticienImage = itemView.findViewById(R.id.praticienImage);
            cardView = itemView.findViewById(R.id.cardVisite);
        }
    }
}
