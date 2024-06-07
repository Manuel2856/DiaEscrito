package com.example.diaescrito;

import static android.app.PendingIntent.getActivity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.diaescrito.baseDeDatos.GestorEntradas;
import com.example.diaescrito.databinding.EditarDiaBinding;
import com.example.diaescrito.entidades.Entrada;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class EditarDia extends AppCompatActivity {
    private EditarDiaBinding binding;
    private ImageView btnVolverAtras;
    private Button btnGuardar;
    private int contadorTituloEntrada=0, contadorContenidoEntrada=0;
    private GestorEntradas ge;
    private static final int PICK_IMAGE = 1;
    private ImageView imagenUsuario;
    private Uri photoUri;
    private static final int PERMISSIONS_REQUEST_CAMERA = 100;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Uri> cameraLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = EditarDiaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        btnVolverAtras = binding.btnVolverAtras;
        btnGuardar = binding.btnGuardar;
        Intent intentEditarDia = getIntent();
        ge = new GestorEntradas(this);
        imagenUsuario = binding.imgAddImage;
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Entrada entradaEditar = MainActivity.getEntradaEditar();
        if(entradaEditar!=null){
            binding.etxtTituloEntrada.setText(entradaEditar.getTitulo());
            binding.etxtContenidoEntrada.setText(entradaEditar.getContenido());
            byte[] imagenBytes = entradaEditar.getImagen();

            if (imagenBytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length);
                binding.imgAddImage.setImageBitmap(bitmap);
            }
        }
        btnVolverAtras.setOnClickListener(e->{
            volverAInicio();
        });
        btnGuardar.setOnClickListener(e->{
            String fechaString = intentEditarDia.getStringExtra("date");
            if(fechaString==null){
                fechaString = entradaEditar.getFechaFormateada();
            }
            Date fechaDate = null;
            try {
                fechaDate = dateFormat.parse(fechaString);
            } catch (ParseException a) {
                a.printStackTrace();
            }
            byte[] imagenByteArray = null;
            if (photoUri != null) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(photoUri);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);

                    imagenByteArray = byteArrayOutputStream.toByteArray();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            Entrada entradaAGuardar = new Entrada(binding.etxtTituloEntrada.getText().toString(),
                    binding.etxtContenidoEntrada.getText().toString(),fechaDate,MainActivity.getUsuarioApp(),imagenByteArray,
                    MainActivity.getEntradaEditar()!=null? MainActivity.getEntradaEditar().getIdEntrada() : 0);
            ge.insertarEntrada(entradaAGuardar);
            MainActivity.setEntradaEditar(null);
            volverAInicio();
        });
        binding.etxtTituloEntrada.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && binding.etxtTituloEntrada.getText().toString().equalsIgnoreCase("Título de la entrada")) {
                binding.etxtTituloEntrada.setText("");
            }
        });
        binding.etxtContenidoEntrada.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && binding.etxtContenidoEntrada.getText().toString().equalsIgnoreCase("Contenido")) {
                binding.etxtContenidoEntrada.setText("");
            }
        });
        binding.imgAddImage.setOnClickListener(e->{
            mostrarMenuFoto();
        });

        //Registro las actividades de la camara y la galeria
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                photoUri = result.getData().getData();
                imagenUsuario.setImageURI(photoUri);
            }
        });
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
            if (success) {
                imagenUsuario.setImageURI(photoUri);
            }
        });
    }


    private void mostrarMenuFoto() {
        PopupMenu popupMenu = new PopupMenu(this, imagenUsuario);
        popupMenu.getMenuInflater().inflate(R.menu.menu_image_source, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_gallery) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryLauncher.launch(galleryIntent);
                return true;
            } else if (item.getItemId() == R.id.menu_camera) {

                photoUri = createImageUri();
                if (photoUri != null) {
                    cameraLauncher.launch(photoUri);
                }
                return true;
            }
            return false;
        });

        popupMenu.show();
    }
    private Uri createImageUri() {
        ContentResolver contentResolver = getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "IMG_" + System.currentTimeMillis());
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");

        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
    }
    private void takePhotoWithPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_CAMERA);
        } else {
            takePhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
            }
        }
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                File photoFile = createImageFile();
                photoUri = FileProvider.getUriForFile(this, "com.example.diaescrito.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                cameraLauncher.launch(photoUri);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imagenUsuario.setImageURI(selectedImage);
        }
    }
    public void volverAInicio(){
        finish();
    }
}
