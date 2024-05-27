package com.example.diaescrito.baseDeDatos;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diaescrito.entidades.Usuario;

import java.util.ArrayList;
import java.util.List;

public class GestorUsuarios extends AppCompatActivity {
    SQLiteDatabase db;
    private Context context;

    public GestorUsuarios(Context context) {
        this.context = context;
        initializeDatabase();
    }
    private void initializeDatabase() {
        db = context.openOrCreateDatabase("Usuarios", Context.MODE_PRIVATE, null);
        String crearEntradas =
                "CREATE TABLE IF NOT EXISTS Usuarios (" +
                        "IdUsuario INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT, " +
                        "Nombre TEXT, " +
                        "Email TEXT," +
                        "Contrasena TEXT" +");";
        db.execSQL(crearEntradas);
    }
    public boolean insertarUsuario(Usuario usuario){
        initializeDatabase();
        SQLiteStatement statement = null;
        if (db != null) {
            if(!comprobarUsuario(usuario.getEmail(),usuario.getContrasena())){
                statement = db.compileStatement("INSERT INTO Usuarios (Nombre,Email, Contrasena) VALUES (?, ?, ?);");
            }
        } else {
            Log.e("GestorUsuarios", "La base de datos es nula. Asegúrate de inicializarla correctamente.");
        }
        String nombre = usuario.getNombre();
        String contrasenia = usuario.getContrasena();
        String email = usuario.getEmail();

        statement.bindString(1, nombre);
        statement.bindString(2, email);
        statement.bindString(3, contrasenia);

        statement.executeInsert();
        return true;
    }
    public Usuario obtenerUsuarioPorEmail(String email) {
        initializeDatabase();
        Usuario usuario = null;
        Cursor cursor = null;
        try {
            if (db != null) {
                cursor = db.rawQuery("SELECT * FROM Usuarios WHERE email = ?", new String[]{email});
                if (cursor.moveToFirst()) {
                    String nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nombre"));
                    String contrasenia = cursor.getString(cursor.getColumnIndexOrThrow("Contrasena"));
                    int idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow("IdUsuario"));
                    usuario = new Usuario(nombre, email, contrasenia, idUsuario);
                }
            } else {
                Log.e("GestorBaseDeDatos", "La base de datos es nula. Asegúrate de inicializarla correctamente.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return usuario;
    }

    public Usuario obtenerUsuarioPorId(int idUsuario){
        initializeDatabase();
        Usuario usuario = null;
        SQLiteStatement statement = null;
        Cursor cursor = null;
        try {
            if (db != null) {
                cursor = db.rawQuery("SELECT * FROM Usuarios WHERE IdUsuario = ?",new String[]{String.valueOf(usuario.getIdUsuario())});
                if (cursor.moveToFirst()) {
                    String nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nombre"));
                    String contrasenia = cursor.getString(cursor.getColumnIndexOrThrow("Contrasenia"));
                    String email = cursor.getString(cursor.getColumnIndexOrThrow("Email"));
                    // Aquí obtén el resto de los atributos del usuario según tu esquema de base de datos
                    usuario = new Usuario(nombre, email, contrasenia, idUsuario);
                }
            } else {
                Log.e("GestorBaseDeDatos", "La base de datos es nula. Asegúrate de inicializarla correctamente.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
        return usuario;
    }

    public List<Usuario> obtenerUsuarios() {
        List<Usuario> listaPerfiles = new ArrayList<>();
        String consulta = "SELECT * FROM Usuarios";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(consulta, null);
        } catch (Exception e) {
        }

        if (cursor.getCount() == 0){
            return listaPerfiles;
        }
        while (cursor.moveToNext()) {
            Integer id = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("IdPerfil")));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nombre"));
            String contrasenia = cursor.getString(cursor.getColumnIndexOrThrow("Contrasenia"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("Email"));
            Usuario perfil = new Usuario(nombre,email, contrasenia, id);
            listaPerfiles.add(perfil);
        }
        return listaPerfiles;
    }
    public void borrarUsuario(Usuario usuario){
        int id = usuario.getIdUsuario();
        SQLiteStatement statement = null;
        statement = db.compileStatement("Delete FROM Usuarios where IdUsuario = ?");
        statement.bindString(1, getString(id));
        statement.executeUpdateDelete();
    }
    public boolean comprobarUsuario(String email, String contrasena){
        SQLiteStatement statement = db.compileStatement("SELECT COUNT(*) FROM Usuarios WHERE Email = ? AND Contrasena = ?");
        statement.bindString(1, email);
        statement.bindString(2, contrasena);
        try {
            return statement.simpleQueryForLong() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
    public void borrarBaseDeDatos() {
        context.deleteDatabase("Usuarios");
    }
}
