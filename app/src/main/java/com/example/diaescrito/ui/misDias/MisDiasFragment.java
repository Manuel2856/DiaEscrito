package com.example.diaescrito.ui.misDias;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diaescrito.MainActivity;
import com.example.diaescrito.adaptadores.AdaptadorHistorias;
import com.example.diaescrito.baseDeDatos.GestorEntradas;
import com.example.diaescrito.databinding.MisDiasFragmentBinding;
import com.example.diaescrito.entidades.Entrada;
import com.example.diaescrito.entidades.Usuario;

import java.util.ArrayList;
import java.util.List;

public class MisDiasFragment extends Fragment implements AdaptadorHistorias.listener{

    private MisDiasFragmentBinding binding;
    private RecyclerView rvHistorias;
    private GestorEntradas ge;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = MisDiasFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        rvHistorias = binding.rvHistorias;
        rvHistorias.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvHistorias.setHasFixedSize(true);
        ge = new GestorEntradas(requireContext());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        List<Entrada>entradaList = ge.obtenerEntradas(MainActivity.getUsuarioApp());
        AdaptadorHistorias adaptador = new AdaptadorHistorias(entradaList, this);
        rvHistorias.setAdapter(adaptador);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClickCardView(int posicion) {
        Entrada entrada = ge.obtenerEntradas(MainActivity.getUsuarioApp()).get(posicion);

    }
}