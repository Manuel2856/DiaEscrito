package com.example.diaescrito.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diaescrito.R;
import com.example.diaescrito.entidades.Entrada;

import java.util.List;

public class AdaptadorHistorias extends RecyclerView.Adapter<AdaptadorHistorias.HistoriaViewHolder> {

    private static final int TYPE_CON_IMAGEN = 1;
    private static final int TYPE_SIN_IMAGEN = 2;
    private List<Entrada> listaEntradas;
    private Context context;
    private AdaptadorHistorias.listener listener;

    @Override
    public int getItemViewType(int position) {
        Entrada entrada = listaEntradas.get(position);
        if (entrada.hasImage()) {
            return TYPE_CON_IMAGEN;
        } else {
            return TYPE_SIN_IMAGEN;
        }
    }

    @NonNull
    @Override
    public HistoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoriaViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listaEntradas.size();
    }

    public interface listener{
        void onClickCardView(int posicion);
    }
    public AdaptadorHistorias(List<Entrada> listaEntradas, AdaptadorHistorias.listener listener) {
        this.listaEntradas = listaEntradas;
        this.listener = listener;
    }

    public static class HistoriaViewHolder extends RecyclerView.ViewHolder {

        private TextView tituloTextView;
        private TextView descripcionTextView;
        private CardView cardView;
        private ConstraintLayout constraintLayout;


        public HistoriaViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTextView = itemView.findViewById(R.id.txtTituloHistoriaSinImagen);
            descripcionTextView = itemView.findViewById(R.id.txtDescripcionSinImagen);
        }
    }

}
