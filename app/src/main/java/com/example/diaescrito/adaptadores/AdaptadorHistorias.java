package com.example.diaescrito.adaptadores;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diaescrito.R;
import com.example.diaescrito.entidades.Entrada;

import java.util.List;

public class AdaptadorHistorias extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_CON_IMAGEN = 1;
    private static final int TYPE_SIN_IMAGEN = 2;
    private List<Entrada> listaEntradas;
    private Context context;
    private AdaptadorHistorias.listener listener;
    public interface listener{
        void onClickCardView(int posicion);
    }
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == TYPE_CON_IMAGEN) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_historia_con_imagen, parent, false);
            return new HistoriaConImagenViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_historia, parent, false);
            return new HistoriaSinImagenViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Entrada entrada = listaEntradas.get(position);
        if (holder.getItemViewType() == TYPE_CON_IMAGEN) {
            HistoriaConImagenViewHolder conImagenHolder = (HistoriaConImagenViewHolder) holder;
            conImagenHolder.tituloTextView.setText(entrada.getTitulo());
            conImagenHolder.descripcionTextView.setText(entrada.getContenido());
            conImagenHolder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(entrada.getImagen(), 0, entrada.getImagen().length));
        } else {
            HistoriaSinImagenViewHolder sinImagenHolder = (HistoriaSinImagenViewHolder) holder;
            sinImagenHolder.tituloTextView.setText(entrada.getTitulo());
            sinImagenHolder.descripcionTextView.setText(entrada.getContenido());
        }

        holder.itemView.setOnClickListener(v -> listener.onClickCardView(position));
    }

    @Override
    public int getItemCount() {
        return listaEntradas.size();
    }


    public AdaptadorHistorias(List<Entrada> listaEntradas, AdaptadorHistorias.listener listener) {
        this.listaEntradas = listaEntradas;
        this.listener = listener;
    }

    public static class HistoriaConImagenViewHolder extends RecyclerView.ViewHolder {
        private TextView tituloTextView;
        private TextView descripcionTextView;
        private ImageView imageView;

        public HistoriaConImagenViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTextView = itemView.findViewById(R.id.txtTituloHistoriaConImagen);
            descripcionTextView = itemView.findViewById(R.id.txtDescripcionConImagen);
            imageView = itemView.findViewById(R.id.imgAddImage);
        }
    }

    public static class HistoriaSinImagenViewHolder extends RecyclerView.ViewHolder {
        private TextView tituloTextView;
        private TextView descripcionTextView;

        public HistoriaSinImagenViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTextView = itemView.findViewById(R.id.txtTituloHistoriaSinImagen);
            descripcionTextView = itemView.findViewById(R.id.txtDescripcionSinImagen);
        }
    }

}
