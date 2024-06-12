package com.example.diaescrito;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.Manifest;


import com.example.diaescrito.baseDeDatos.GestorCategorias;
import com.example.diaescrito.entidades.Categoria;
import com.example.diaescrito.entidades.Entrada;
import com.example.diaescrito.entidades.Usuario;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.diaescrito.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static Usuario usuarioApp;
    private static Entrada entradaEditar;
    private static final int PERMISSION_REQUEST_CODE = 1001;
    private GestorCategorias gc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gc = new GestorCategorias(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.calendario, R.id.mis_dias, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        crearCategorias();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                NotificacionDiaria.showNotification(this);
            }
        }
    }


    public static Usuario getUsuarioApp() {
        return usuarioApp;
    }

    public static void setUsuarioApp(Usuario usuarioApp) {
        MainActivity.usuarioApp = usuarioApp;
    }

    public static Entrada getEntradaEditar() {
        return entradaEditar;
    }

    public static void setEntradaEditar(Entrada entradaEditar) {
        MainActivity.entradaEditar = entradaEditar;
    }
    private boolean comprobarPermisoNotificaciones() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_BOOT_COMPLETED) == PackageManager.PERMISSION_GRANTED;
    }

    private void pedirPermisoNotificaciones() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED}, PERMISSION_REQUEST_CODE);
    }
    private void crearCategorias(){
        Categoria verano = new Categoria();
        Categoria invierno = new Categoria();
        Categoria otono = new Categoria();
        Categoria primavera = new Categoria();
        verano.setNombre("Verano");
        invierno.setNombre("Invierno");
        otono.setNombre("Otoño");
        primavera.setNombre("Primavera");
        gc.insertarCategoria(verano);
        gc.insertarCategoria(invierno);
        gc.insertarCategoria(otono);
        gc.insertarCategoria(primavera);
    }

}