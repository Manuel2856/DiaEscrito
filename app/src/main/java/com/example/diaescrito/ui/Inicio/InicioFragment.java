package com.example.diaescrito.ui.Inicio;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.diaescrito.EditarDia;
import com.example.diaescrito.databinding.InicioFragmentBinding;
import java.util.Calendar;

public class InicioFragment extends Fragment {

    private InicioFragmentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = InicioFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        CalendarView calendario = binding.calendarView;
        Calendar calendar = Calendar.getInstance();
        //Obtengo los datos
        int añoActual = calendar.get(Calendar.YEAR);
        int mesActual = calendar.get(Calendar.MONTH);
        int diaActual = calendar.get(Calendar.DAY_OF_MONTH);
        calendario.setDate(calendar.getTimeInMillis());
        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String fechaSeleccionada = dayOfMonth + "/" + (month + 1) + "/" + year;
                Intent intent = new Intent(getActivity(), EditarDia.class);
                intent.putExtra("date", fechaSeleccionada);
                startActivity(intent);
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