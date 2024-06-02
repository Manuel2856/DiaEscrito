package com.example.diaescrito.iniciarSesion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.diaescrito.MainActivity;
import com.example.diaescrito.baseDeDatos.GestorUsuarios;
import com.example.diaescrito.databinding.ActivityIniciarSesionBinding;
import com.example.diaescrito.entidades.Usuario;

public class IniciarSesion extends AppCompatActivity {
    private ActivityIniciarSesionBinding binding;
    private EditText emailUsuario,contrasena;
    private String emailUsuarioS,contrasenaS;
    private Button btnAcceder;
    private GestorUsuarios gu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIniciarSesionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        btnAcceder = binding.btnAcceder;
        emailUsuario = binding.txtUsuario;
        contrasena = binding.txtContrasena;


        Usuario u = new Usuario("Usuario","a","a");
        gu = new GestorUsuarios(this);
        gu.borrarBaseDeDatos();
        gu.insertarUsuario(u);

        btnAcceder.setOnClickListener(e->{
            emailUsuarioS = emailUsuario.getText().toString();
            contrasenaS = contrasena.getText().toString();
            if(gu.comprobarUsuario(u.getEmail(),u.getContrasena())){
                Usuario usuarioDB = gu.obtenerUsuarioPorEmail(emailUsuarioS);
                MainActivity.setUsuarioApp(usuarioDB);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        });
        binding.registrarse.setOnClickListener(e->{
            Intent intent = new Intent(this, Registrarse.class);
            startActivity(intent);
        });
    }
}
