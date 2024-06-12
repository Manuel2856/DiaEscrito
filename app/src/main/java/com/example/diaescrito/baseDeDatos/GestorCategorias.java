package com.example.diaescrito.baseDeDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.diaescrito.entidades.Categoria;

import java.util.ArrayList;
import java.util.List;

public class GestorCategorias {
    SQLiteDatabase db;
    private final Context context;

    public GestorCategorias(Context context) {
        this.context = context;
        initializeDatabase();
    }
    private void initializeDatabase() {
        db = context.openOrCreateDatabase("Categorias", Context.MODE_PRIVATE, null);
        String crearCategorias =
                "CREATE TABLE IF NOT EXISTS Categorias (" +
                        "IdCategoria INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "Nombre TEXT NOT NULL" +
                        ");";
        db.execSQL(crearCategorias);
    }
    public List<Categoria> obtenerCategorias() {
        List<Categoria> categories = new ArrayList<>();
        initializeDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Categorias", null);
        if (cursor.moveToFirst()) {
            do {
                Categoria categoria = new Categoria();
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                categoria.setIdCategoria(id);
                categoria.setNombre(name);
                categories.add(categoria);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categories;
    }
    public void insertarCategoria(Categoria categoria) {
        if (!comprobarCategoria(categoria.getNombre())) {
            ContentValues values = new ContentValues();
            values.put("Nombre", categoria.getNombre());
            db.insert("Categorias", null, values);
        }
    }
    private boolean comprobarCategoria(String nombreCategoria) {
        Cursor cursor = db.rawQuery("SELECT 1 FROM Categorias WHERE Nombre = ?", new String[]{nombreCategoria});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }
    public Categoria obtenerCategoriaPorNombre(String nombreCategoria){
        initializeDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Categorias WHERE Nombre = ?", new String[]{nombreCategoria});
        Categoria categoria = new Categoria();
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("IdCategoria"));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nombre"));
            categoria.setIdCategoria(id);
            categoria.setNombre(nombre);
        }
        cursor.close();
        return categoria;
    }
    public Categoria obtenerCategoriaPorId(int categoryId) {
        initializeDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Categorias WHERE IdCategoria = ?", new String[]{String.valueOf(categoryId)});
        Categoria categoria = null;
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("IdCategoria"));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nombre"));
            categoria = new Categoria();
            categoria.setIdCategoria(id);
            categoria.setNombre(nombre);
        }
        cursor.close();
        return categoria;
    }

    public void borrarBaseDeDatos(){
        context.deleteDatabase("Categorias");
    }

}
