package com.example.diaescrito.baseDeDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.diaescrito.entidades.Entrada;
import com.example.diaescrito.entidades.Usuario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GestorEntradas {
    SQLiteDatabase db;
    private final Context context;

    private GestorUsuarios gestorUsuarios;
    SimpleDateFormat inputFormat = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
    SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
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
                        "Fecha DATE, " +
                        "Imagen BLOB," +
                        "FOREIGN KEY(IdUsuario) REFERENCES Usuarios(IdUsuario) " +
                        ");";
        db.execSQL(crearEntradas);
    }


    public void insertarEntrada(Entrada entrada){
        initializeDatabase();
        Entrada entradaExistente = null;
        if (db != null) {
            ContentValues values = new ContentValues();
            values.put("IdUsuario", entrada.getUsuario().getIdUsuario());
            values.put("Titulo", entrada.getTitulo());
            values.put("Contenido", entrada.getContenido());
            values.put("Fecha", entrada.getFecha().toString());
            if(entrada.getIdEntrada()!=0){
                values.put("IdEntrada",entrada.getIdEntrada());
            }
            if (entrada.getImagen() != null) {
                values.put("Imagen", entrada.getImagen());
            }
            // Compruebo si la entrada ya existe
            if(entrada.getIdEntrada() !=0){
                 entradaExistente = obtenerEntradaPorId(entrada.getIdEntrada());
            }

            if (entradaExistente != null) {
                long result = db.insertWithOnConflict("Entradas", null, values,SQLiteDatabase.CONFLICT_REPLACE);
                if (result == -1) {
                    Log.e("GestorEntradas", "Error al insertar la entrada en la base de datos");
                }
            } else {
                long result = db.insert("Entradas", null, values);
                if (result == -1) {
                    Log.e("GestorEntradas", "Error al insertar la entrada en la base de datos");
                }
            }
        } else {
            Log.e("GestorBaseDeDatos", "La base de datos es nula. Asegúrate de inicializarla correctamente.");
        }
    }

    public List<Entrada> obtenerEntradas(Usuario usuario){
        initializeDatabase();
        List<Entrada> listaEntradas = new ArrayList<>();
        Entrada entrada;
        String consulta = "SELECT * FROM Entradas WHERE IdUsuario = ?";
        try (Cursor cursor = db.rawQuery(consulta, new String[]{String.valueOf(usuario.getIdUsuario())})) {

            while (cursor.moveToNext()) {
                String titulo = cursor.getString(cursor.getColumnIndexOrThrow("Titulo"));
                String contenido = cursor.getString(cursor.getColumnIndexOrThrow("Contenido"));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow("Fecha"));
                String idEntrada = cursor.getString(cursor.getColumnIndexOrThrow("IdEntrada"));
                Date fechaEntrada = null;
                try {
                    fechaEntrada = inputFormat.parse(fecha);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                byte[] imagen = null;
                int imagenIndex = cursor.getColumnIndex("Imagen");
                if (imagenIndex != -1) {
                    if (!cursor.isNull(imagenIndex)) {
                        imagen = cursor.getBlob(imagenIndex);
                    }
                }
                if(imagen == null){
                    entrada = new Entrada(titulo, contenido, fechaEntrada, usuario,Integer.parseInt(idEntrada));
                }else{
                    entrada = new Entrada(titulo, contenido, fechaEntrada, usuario, imagen,Integer.parseInt(idEntrada));
                }
                listaEntradas.add(entrada);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaEntradas;
    }
    public Entrada obtenerEntradaPorTituloYFecha(Usuario usuario, String titulo, Date fecha) {
        initializeDatabase();
        Entrada entrada = null;
        String consulta = "SELECT * FROM Entradas WHERE IdUsuario = ? AND Titulo = ? AND Fecha = ?";
        try (Cursor cursor = db.rawQuery(consulta, new String[]{String.valueOf(usuario.getIdUsuario()), titulo, String.valueOf(fecha)})) {
            if (cursor.moveToFirst()) {
                String contenido = cursor.getString(cursor.getColumnIndexOrThrow("Contenido"));
                byte[] imagen = null;
                int imagenIndex = cursor.getColumnIndex("Imagen");
                if (imagenIndex != -1) {
                    if (!cursor.isNull(imagenIndex)) {
                        imagen = cursor.getBlob(imagenIndex);
                    }
                }
                if (imagen == null) {
                    entrada = new Entrada(titulo, contenido, fecha, usuario);
                } else {
                    entrada = new Entrada(titulo, contenido, fecha, usuario, imagen);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return entrada;
    }
    public Entrada obtenerEntradaPorId(int idEntrada) {
        initializeDatabase();
        Entrada entrada = null;
        String consulta = "SELECT * FROM Entradas WHERE IdEntrada = ?";

        try (Cursor cursor = db.rawQuery(consulta, new String[]{String.valueOf(idEntrada)})) {
            if (cursor.moveToFirst()) {
                String titulo = cursor.getString(cursor.getColumnIndexOrThrow("Titulo"));
                String contenido = cursor.getString(cursor.getColumnIndexOrThrow("Contenido"));
                long fechaLong = cursor.getLong(cursor.getColumnIndexOrThrow("Fecha"));
                Date fecha = new Date(fechaLong);
                int idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow("IdUsuario"));
                Usuario usuario = obtenerUsuarioPorId(idUsuario);

                byte[] imagen = null;
                int imagenIndex = cursor.getColumnIndex("Imagen");
                if (imagenIndex != -1 && !cursor.isNull(imagenIndex)) {
                    imagen = cursor.getBlob(imagenIndex);
                }

                if (imagen == null) {
                    entrada = new Entrada(titulo, contenido, fecha, usuario);
                } else {
                    entrada = new Entrada(titulo, contenido, fecha, usuario, imagen);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return entrada;
    }

    private Usuario obtenerUsuarioPorId(int idUsuario) {
        Usuario usuario = null;
        String consulta = "SELECT * FROM Usuarios WHERE IdUsuario = ?";

        try (Cursor cursor = db.rawQuery(consulta, new String[]{String.valueOf(idUsuario)})) {
            if (cursor.moveToFirst()) {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nombre"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("Email"));
                String contrasena = cursor.getString(cursor.getColumnIndexOrThrow("Contrasena"));
                usuario = new Usuario(nombre, email, contrasena, idUsuario);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return usuario;
    }


    public void eliminarEntrada(Entrada entrada) {
        initializeDatabase();
        String consulta = "DELETE FROM Entradas WHERE IdUsuario = ? AND Titulo = ? AND Fecha = ?";
        try {
            db.execSQL(consulta, new String[]{String.valueOf(entrada.getUsuario().getIdUsuario()), entrada.getTitulo(), inputFormat.format(entrada.getFecha())});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void borrarBaseDeDatos(){
        context.deleteDatabase("Entradas");
    }


}
