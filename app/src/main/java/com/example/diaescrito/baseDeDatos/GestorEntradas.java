package com.example.diaescrito.baseDeDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diaescrito.entidades.Entrada;
import com.example.diaescrito.entidades.Usuario;

import java.util.ArrayList;
import java.util.List;

public class GestorEntradas extends AppCompatActivity {
    SQLiteDatabase db;
    private final Context context;

    private GestorUsuarios gestorUsuarios;

    public GestorEntradas(Context context) {
        this.context = context;
        initializeDatabase();
    }

    private void initializeDatabase() {
        db = context.openOrCreateDatabase("Entradas", Context.MODE_PRIVATE, null);
        String crearEntradas =
                "CREATE TABLE IF NOT EXISTS Entradas (" +
                        "IdEntrada INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "IdUsuario INTEGER, " +
                        "Titulo TEXT, " +
                        "Contenido TEXT," +
                        "Fecha TEXT, " +
                        "Imagen BLOB" +
                        ");";
        db.execSQL(crearEntradas);
    }

    public void insertarEntrada(Entrada entrada){
        initializeDatabase();
        if (db != null) {
            ContentValues values = new ContentValues();
            values.put("IdUsuario", entrada.getUsuario().getIdUsuario());
            values.put("Titulo", entrada.getTitulo());
            values.put("Contenido", entrada.getContenido());
            values.put("Fecha", entrada.getFecha());
            values.put("Imagen", entrada.getImagen());

            long result = db.insert("Entradas", null, values);
            if (result == -1) {
                Log.e("GestorEntradas", "Error al insertar la entrada en la base de datos");
            }
        } else {
            Log.e("GestorBaseDeDatos", "La base de datos es nula. Asegúrate de inicializarla correctamente.");
        }
    }

    public List<Entrada> obtenerEntradas(Usuario usuario){
        List<Entrada> listaEntradas = new ArrayList<>();
        String consulta = "SELECT * FROM Entradas WHERE IdUsuario = ?";
        try (Cursor cursor = db.rawQuery(consulta, new String[]{String.valueOf(usuario.getIdUsuario())})) {

            while (cursor.moveToNext()) {
                Integer id = cursor.getInt(cursor.getColumnIndexOrThrow("IdEntrada"));
                String titulo = cursor.getString(cursor.getColumnIndexOrThrow("Titulo"));
                String contenido = cursor.getString(cursor.getColumnIndexOrThrow("Contenido"));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow("Fecha"));
                byte[] imagen = cursor.getBlob(cursor.getColumnIndexOrThrow("Imagen"));

                Entrada entrada = new Entrada(titulo, contenido, fecha, usuario, imagen);
                listaEntradas.add(entrada);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaEntradas;
    }

}
