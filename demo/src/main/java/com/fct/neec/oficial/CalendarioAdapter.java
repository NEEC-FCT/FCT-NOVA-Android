package com.fct.neec.oficial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CalendarioAdapter extends RecyclerView.Adapter<CalendarioAdapter.ViewHolder> {

    private ArrayList<Event> dataset;
    private eventoListener evento_listener;

    CalendarioAdapter(ArrayList<Event> dataset, eventoListener evento_listener){
        this.dataset = dataset;
        this.evento_listener = evento_listener;
    }

    @NonNull
    @Override
    public CalendarioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_evento, parent, false);
        return new ViewHolder(v, evento_listener);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos){
        Event evt = dataset.get(pos);
        if (evt != null){
            holder.titulo.setText((String)evt.getData());
            long time = evt.getTimeInMillis();
            if (time > 0) {
                holder.data.setText(new SimpleDateFormat("dd MMMM").format(time));
                holder.cor.setBackgroundColor(evt.getColor());
            }
        }

    }

    @Override
    public int getItemCount(){
        return dataset.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titulo;
        TextView data;
        TextView cor;

        eventoListener evento_listener;
        ViewHolder(View v, eventoListener evento_listener){
            super(v);
            titulo = v.findViewById(R.id.titulo);
            data = v.findViewById(R.id.data);
            cor = v.findViewById(R.id.cor);
            this.evento_listener = evento_listener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            evento_listener.onEventoClick(getAdapterPosition());
        }
    }
    public interface eventoListener{
        void onEventoClick(int pos);
    }
}
