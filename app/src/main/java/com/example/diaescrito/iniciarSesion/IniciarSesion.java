package com.example.diaescrito.iniciarSesion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diaescrito.MainActivity;
import com.example.diaescrito.baseDeDatos.GestorUsuarios;
import com.example.diaescrito.databinding.ActivityIniciarSesionBinding;
import com.example.diaescrito.entidades.Usuario;
import com.example.diaescrito.ui.home.InicioFragment;

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
            if(gu.comprobarUsuario(emailUsuarioS,contrasenaS)){
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
