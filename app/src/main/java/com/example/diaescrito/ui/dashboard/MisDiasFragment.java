package com.example.diaescrito.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diaescrito.databinding.MisDiasFragmentBinding;
import com.example.diaescrito.entidades.Entrada;

import java.util.ArrayList;
import java.util.List;

public class MisDiasFragment extends Fragment {

    private MisDiasFragmentBinding binding;
    private RecyclerView rvHistorias;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = MisDiasFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        rvHistorias = binding.rvHistorias;
        rvHistorias.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvHistorias.setHasFixedSize(true);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void rellenarHistorias(){
        List<Entrada>entradaList = new ArrayList<>();

    }
}