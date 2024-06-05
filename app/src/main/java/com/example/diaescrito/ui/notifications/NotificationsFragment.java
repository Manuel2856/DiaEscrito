package com.example.diaescrito.ui.notifications;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.diaescrito.NotificacionDiaria;
import com.example.diaescrito.databinding.FragmentNotificationsBinding;

import org.w3c.dom.ls.LSOutput;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private TextView txtNotificaciones;
    private Switch swtNotificaciones;
    private static final int PERMISSION_REQUEST_CODE = 1001;
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            NotificacionDiaria.showNotification(requireContext());
        } else {
        }
    });

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        txtNotificaciones = binding.txtNotificaciones;
        swtNotificaciones = binding.swtNotificaciones;
        swtNotificaciones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    swtNotificaciones.setText("Sí");
                    if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECEIVE_BOOT_COMPLETED) == PackageManager.PERMISSION_GRANTED){
                        NotificacionDiaria.showNotification(requireContext());
                    }else {
                        requestPermissionLauncher.launch(Manifest.permission.RECEIVE_BOOT_COMPLETED);
                        //NotificacionDiaria.scheduleNotification(this);
                        //NotificacionDiaria.showNotification(this);
                    }
                } else {
                    swtNotificaciones.setText("No");
                }
            }
        });

        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}