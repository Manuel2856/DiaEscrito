package com.example.diaescrito.ui.notifications;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import java.util.Calendar;


import com.example.diaescrito.NotificacionDiaria;
import com.example.diaescrito.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private TextView txtNotificaciones;
    private Switch swtNotificaciones;
    private TextClock txtclckHoraNotificacion;
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
        txtclckHoraNotificacion = binding.txtclckHoraNotificacion;
        txtclckHoraNotificacion.setVisibility(View.INVISIBLE);
        swtNotificaciones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    swtNotificaciones.setText("Sí");
                    requestPermissions();
                    txtclckHoraNotificacion.setVisibility(View.VISIBLE);
                    NotificacionDiaria.showNotification(requireContext());
                } else {
                    swtNotificaciones.setText("No");
                }
            }
        });
        txtclckHoraNotificacion.setOnClickListener(e->{
            showTimePickerDialog();
        });
        return root;
    }
    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                txtclckHoraNotificacion.setText(String.format("%02d:%02d", hourOfDay, minute));
                NotificacionDiaria.cancelScheduledNotifications(requireContext());
                NotificacionDiaria.scheduleNotification(requireContext(),hourOfDay,minute);
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECEIVE_BOOT_COMPLETED) == PackageManager.PERMISSION_GRANTED) {
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                requestPermissionLauncher.launch(Manifest.permission.RECEIVE_BOOT_COMPLETED);
            }
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECEIVE_BOOT_COMPLETED) == PackageManager.PERMISSION_GRANTED) {
                NotificacionDiaria.showNotification(requireContext());
            } else {
                requestPermissionLauncher.launch(Manifest.permission.RECEIVE_BOOT_COMPLETED);
            }
        }
    }

}