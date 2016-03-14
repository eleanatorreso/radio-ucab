package info.androidhive.radioucab.Controlador.Adaptor;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import info.androidhive.radioucab.Controlador.ViewHolder.MisConcursosViewHolder;
import info.androidhive.radioucab.Model.MisConcurso;
import info.androidhive.radioucab.R;

public class AdaptadorMisConcursos extends RecyclerView.Adapter<MisConcursosViewHolder>{

    private List<MisConcurso> concursos;
    private Fragment fragment;

    public AdaptadorMisConcursos(List<MisConcurso> concursos, Fragment fragment) {
        this.concursos = concursos;
        this.fragment = fragment;
    }


    @Override
    public MisConcursosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_mis_concursos, parent, false);
        MisConcursosViewHolder concurso = new MisConcursosViewHolder(view);
        return concurso;
    }

    @Override
    public void onBindViewHolder(MisConcursosViewHolder holder, int position) {
        MisConcurso miConcurso = concursos.get(position);
        holder.tituloConcurso.setText(miConcurso.getNombreConcurso());
        holder.descripcionConcurso.setText(miConcurso.getDescripcion());
        holder.ganoConcurso = miConcurso.getGano();
        holder.fechasConcurso.setText(miConcurso.getFechas());
        holder.resultadoConcurso.setText(miConcurso.getResultado());
        holder.estadoConcurso.setText(miConcurso.getEstado_concurso());
        holder.fragment = fragment;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return concursos.size();
    }
}
