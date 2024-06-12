package com.example.diaescrito.baseDeDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.diaescrito.entidades.Categoria;
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

    private GestorCategorias gc;
    SimpleDateFormat inputFormat = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
    SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    public GestorEntradas(Context context) {
        this.context = context;
        gc = new GestorCategorias(context);
        gc = new GestorCategorias(context);
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
                        "IdCategoria INTEGER, " +
                        "FOREIGN KEY(IdUsuario) REFERENCES Usuarios(IdUsuario), " +
                        "FOREIGN KEY(IdCategoria) REFERENCES Categorias(IdCategoria) " +
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
            values.put("IdCategoria", entrada.getCategoria().getIdCategoria());
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
            values.put("IdCategoria",entrada.getCategoria().getIdCategoria());
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

    public List<Entrada> obtenerEntradasOrdenadasPorFecha(Usuario usuario){
        initializeDatabase();
        List<Entrada> listaEntradas = new ArrayList<>();
        Categoria verano = gc.obtenerCategoriaPorNombre("Verano");
        String consulta = "SELECT * " +
                "FROM Entradas " +
                "WHERE IdUsuario = ? ORDER BY Fecha DESC";
        try (Cursor cursor = db.rawQuery(consulta, new String[]{String.valueOf(usuario.getIdUsuario())})) {
            while (cursor.moveToNext()) {
                Entrada entrada = new Entrada();
                String titulo = cursor.getString(cursor.getColumnIndexOrThrow("Titulo"));
                String contenido = cursor.getString(cursor.getColumnIndexOrThrow("Contenido"));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow("Fecha"));
                String idEntrada = cursor.getString(cursor.getColumnIndexOrThrow("IdEntrada"));
                String idCategoria = cursor.getString(cursor.getColumnIndexOrThrow("IdCategoria"));
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
                        entrada.setImagen(imagen);
                    }
                }
                entrada.setTitulo(titulo);
                entrada.setContenido(contenido);
                entrada.setFecha(fechaEntrada);
                entrada.setUsuario(usuario);
                entrada.setIdEntrada(Integer.parseInt(idEntrada));

                //Obtengo la categoria de la entrada
                Categoria categoria = gc.obtenerCategoriaPorId(Integer.parseInt(idCategoria));
                entrada.setCategoria(categoria);

                listaEntradas.add(entrada);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaEntradas;
    }
    public Entrada obtenerEntradaPorId(int idEntrada) {
        initializeDatabase();

        String consulta = "SELECT *" +
                "FROM Entradas " +
                "WHERE IdEntrada = ?";

        Entrada entrada = null;
        try (Cursor cursor = db.rawQuery(consulta, new String[]{String.valueOf(idEntrada)})) {
            entrada = new Entrada();
            if (cursor.moveToFirst()) {

                String titulo = cursor.getString(cursor.getColumnIndexOrThrow("Titulo"));
                String contenido = cursor.getString(cursor.getColumnIndexOrThrow("Contenido"));
                long fechaLong = cursor.getLong(cursor.getColumnIndexOrThrow("Fecha"));
                Date fecha = new Date(fechaLong);
                int idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow("IdUsuario"));
                Usuario usuario = obtenerUsuarioPorId(idUsuario);

                String idCategoria = cursor.getString(cursor.getColumnIndexOrThrow("IdCategoria"));
                Categoria categoria = gc.obtenerCategoriaPorId(Integer.parseInt(idCategoria));


                byte[] imagen = null;
                int imagenIndex = cursor.getColumnIndex("Imagen");
                if (imagenIndex != -1 && !cursor.isNull(imagenIndex)) {
                    imagen = cursor.getBlob(imagenIndex);
                    entrada.setImagen(imagen);
                }

                entrada.setTitulo(titulo);
                entrada.setContenido(contenido);
                entrada.setFecha(fecha);
                entrada.setUsuario(usuario);
                entrada.setCategoria(categoria);

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
