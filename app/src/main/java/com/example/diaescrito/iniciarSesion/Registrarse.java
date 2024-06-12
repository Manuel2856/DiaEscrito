package com.example.diaescrito.iniciarSesion;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.diaescrito.EditarDia;
import com.example.diaescrito.MainActivity;
import com.example.diaescrito.baseDeDatos.GestorUsuarios;
import com.example.diaescrito.databinding.ActivityRegistrarseBinding;
import com.example.diaescrito.entidades.Usuario;

public class Registrarse extends AppCompatActivity {
    private ActivityRegistrarseBinding binding;
    private Button btnRegistrarse;
    private EditText etxtUsuario,etxtEmail,etxtContrasena;
    private GestorUsuarios gu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrarseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        btnRegistrarse = binding.btnRegistrarse;
        etxtUsuario = binding.etxtUsuario;
        etxtEmail = binding.etxtEmail;
        etxtContrasena = binding.etxtContrasena;
        gu = new GestorUsuarios(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        btnRegistrarse.setOnClickListener(e->{
            String usuario,email,contrasena;
            usuario = etxtUsuario.getText().toString();
            email = etxtEmail.getText().toString();
            contrasena = etxtContrasena.getText().toString();
            Usuario nuevoUsuario = new Usuario(usuario,email,contrasena);
            if(gu.insertarUsuario(nuevoUsuario)){
                MainActivity.setUsuarioApp(nuevoUsuario);
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("Email", email);
                startActivity(intent);
            }

        });
    }

}
