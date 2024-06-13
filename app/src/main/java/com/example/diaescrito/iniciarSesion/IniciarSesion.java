package com.example.diaescrito.iniciarSesion;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.diaescrito.MainActivity;
import com.example.diaescrito.R;
import com.example.diaescrito.baseDeDatos.GestorUsuarios;
import com.example.diaescrito.databinding.ActivityIniciarSesionBinding;
import com.example.diaescrito.entidades.Usuario;

import java.util.List;
import java.util.concurrent.Executor;

public class IniciarSesion extends AppCompatActivity {
    private ActivityIniciarSesionBinding binding;
    private EditText emailUsuario,contrasena;
    private String emailUsuarioS,contrasenaS;
    private Button btnAcceder;
    private Button btnHuella;
    private GestorUsuarios gu;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        binding = ActivityIniciarSesionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        btnAcceder = binding.btnAcceder;
        btnHuella = binding.btnHuella;
        emailUsuario = binding.txtUsuario;
        contrasena = binding.txtContrasena;

        gu = new GestorUsuarios(this);
        initializeBiometricPrompt();

        btnAcceder.setOnClickListener(e->{
            emailUsuarioS = emailUsuario.getText().toString();
            contrasenaS = contrasena.getText().toString();
            if(gu.comprobarUsuario(emailUsuarioS,contrasenaS)){
                Usuario usuarioDB = gu.obtenerUsuarioPorEmail(emailUsuarioS);
                MainActivity.setUsuarioApp(usuarioDB);
                Intent intent = new Intent(IniciarSesion.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btnHuella.setOnClickListener(e->{
            List<Usuario>listaUsuario = gu.obtenerUsuarios();
            if(listaUsuario.size()==1){
                Usuario usuarioApp = listaUsuario.get(0);
                MainActivity.setUsuarioApp(usuarioApp);
                biometricPrompt.authenticate(promptInfo);
            }else{
                showEmailDialog();
            }

        });
        binding.registrarse.setOnClickListener(e->{
            Intent intent = new Intent(IniciarSesion.this, Registrarse.class);
            startActivity(intent);
        });
    }
    private void showEmailDialog() {
        EditText emailInput = new EditText(this);
        emailInput.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Email")
                .setMessage("Ingresa un email para continuar")
                .setView(emailInput)
                .setPositiveButton("OK", (dialog, which) -> {
                    emailUsuarioS = emailInput.getText().toString();
                    biometricPrompt.authenticate(promptInfo);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
    private void initializeBiometricPrompt() {
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "Error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Intent intent = new Intent(IniciarSesion.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "No se puede acceder a la huella", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Inicio de sesión biométrico")
                .setSubtitle("Mantén pulsado sobre la huella de su móvil para acceder")
                .setNegativeButtonText("Cancelar")
                .build();
    }
}
