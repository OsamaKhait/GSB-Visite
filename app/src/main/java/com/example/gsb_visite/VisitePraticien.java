package com.example.gsb_visite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.gsb_visite.databinding.ActivityVisitePraticienBinding;

public class VisitePraticien extends AppCompatActivity {
    private ActivityVisitePraticienBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVisitePraticienBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

    }
}