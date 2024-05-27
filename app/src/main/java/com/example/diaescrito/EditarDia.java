package com.example.diaescrito;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.diaescrito.databinding.EditarDiaBinding;
import com.example.diaescrito.ui.home.InicioFragment;

public class EditarDia extends AppCompatActivity {
    private EditarDiaBinding binding;
    private ImageView btnVolverAtras;
    private Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = EditarDiaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        btnVolverAtras = binding.btnVolverAtras;
        btnGuardar = binding.btnGuardar;

        btnVolverAtras.setOnClickListener(e->{
            Intent intent = new Intent(this, InicioFragment.class);
            startActivity(intent);
        });
        btnGuardar.setOnClickListener(e->{

        });


    }
}
