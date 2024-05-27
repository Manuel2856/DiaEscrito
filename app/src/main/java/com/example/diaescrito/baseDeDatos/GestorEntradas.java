package com.example.diaescrito.baseDeDatos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diaescrito.entidades.Entrada;
import com.example.diaescrito.entidades.Usuario;

import java.util.ArrayList;
import java.util.List;

public class GestorEntradas extends AppCompatActivity {
    SQLiteDatabase db;
    private Context context;

    private GestorUsuarios gestorUsuarios;

    public GestorEntradas(Context context) {
        this.context = context;
        initializeDatabase();
    }

    private void initializeDatabase() {
        db = context.openOrCreateDatabase("Entradas", Context.MODE_PRIVATE, null);
        String crearEntradas =
                "CREATE TABLE IF NOT EXISTS Entradas (" +
                        "IdUsuario INTEGER PRIMARY KEY, " +
                        "Titulo TEXT, " +
                        "Contenido TEXT," +
                        "Fecha TEXT " +");";
        db.execSQL(crearEntradas);
    }
    public void insertarEntrada(Entrada entrada){
        initializeDatabase();
        SQLiteStatement statement = null;
        if (db != null) {
            statement = db.compileStatement("INSERT INTO Entradas (IdEntrada,Titulo, Contenido,Fecha) VALUES (?, ?, ?,?);");

            // Resto del código...
        } else {
            Log.e("GestorBaseDeDatos", "La base de datos es nula. Asegúrate de inicializarla correctamente.");
        }
        String idUsuario = String.valueOf(entrada.getUsuario().getIdUsuario());
        String titulo = entrada.getTitulo();
        String contenido = entrada.getContenido();
        String fecha = entrada.getFecha();

        statement.bindString(1,idUsuario);
        statement.bindString(2, titulo);
        statement.bindString(3, contenido);
        statement.bindString(4, fecha);

        statement.executeInsert();
    }
    public List<Entrada> obtenerEntradas(Usuario usuario){
        List<Entrada> listaEntradas = new ArrayList<>();
        String consulta = "SELECT * FROM Entradas where Entradas.IdUsuario = ?";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(consulta, new String[]{String.valueOf(usuario.getIdUsuario())});

            while (cursor.moveToNext()) {
                Integer id = cursor.getInt(cursor.getColumnIndexOrThrow("IdEntrada"));
                String titulo = cursor.getString(cursor.getColumnIndexOrThrow("Titulo"));
                String contenido = cursor.getString(cursor.getColumnIndexOrThrow("Contenido"));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow("Fecha"));
                Entrada entrada = new Entrada(titulo,contenido,fecha,usuario);
                listaEntradas.add(entrada);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return listaEntradas;
    }

}
