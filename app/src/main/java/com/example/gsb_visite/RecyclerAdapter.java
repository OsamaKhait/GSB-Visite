package com.example.gsb_visite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    private List<Praticien> dataModelList;

    public RecyclerAdapter(List<Praticien> dataModelList) {
        this.dataModelList = dataModelList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerViewHolder viewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_planets, parent, false);
        viewHolder = new RecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.textViewName.setText(dataModelList.get(position).getNom() + "    " + dataModelList.get(position).getPrenom() );
    }

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
        }
    }
}
