package com.example.gsb_visite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PraticienAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<Object> items = new ArrayList<>();
    private final OnPraticienClickListener listener;

    public interface OnPraticienClickListener {
        void onPraticienClick(Praticien praticien);
    }

    public PraticienAdapter(OnPraticienClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<Object> items) {
        this.items = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof Character ? TYPE_HEADER : TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.item_letter_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_praticien, parent, false);
            return new PraticienViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = items.get(position);

        if (holder instanceof HeaderViewHolder && item instanceof Character) {
            ((HeaderViewHolder) holder).letterTextView.setText(String.valueOf(item));
        } else if (holder instanceof PraticienViewHolder && item instanceof Praticien) {
            Praticien praticien = (Praticien) item;
            PraticienViewHolder praticienHolder = (PraticienViewHolder) holder;

            String title = "Dr ";
            if (praticien.getNom() != null && praticien.getNom().equalsIgnoreCase("Bereny")) {
                title = "Mme ";
            }

            String fullName = title + praticien.getPrenom() + " " + praticien.getNom();
            praticienHolder.nameTextView.setText(fullName);

            praticienHolder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPraticienClick(praticien);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView letterTextView;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            letterTextView = itemView.findViewById(R.id.letterTextView);
        }
    }

    static class PraticienViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;

        PraticienViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.praticienNameTextView);
        }
    }
}
