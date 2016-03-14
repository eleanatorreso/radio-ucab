package info.androidhive.radioucab.Controlador.Adaptor;

import android.app.Fragment;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import info.androidhive.radioucab.Model.Programa;
import info.androidhive.radioucab.R;
import info.androidhive.radioucab.Controlador.ViewHolder.ProgramaViewHolder;

public class AdaptadorPrograma extends RecyclerView.Adapter<ProgramaViewHolder>{

    private List<Programa> programas;
    private Fragment fragment;
    private int iconoPrograma = 1;

    public AdaptadorPrograma(List<Programa> programas, Fragment fragment) {
        this.programas = programas;
        this.fragment = fragment;
    }

    private int seleccionarIconoPrograma(){
        int resultado = 0;
        switch (iconoPrograma){
            case 1:
                resultado = R.drawable.ic_parrilla_grande;
                iconoPrograma++;
                break;
            case 2:
                resultado = R.drawable.ic_parrilla2;
                iconoPrograma++;
                break;
            case 3:
                resultado = R.drawable.ic_parrilla3;
                iconoPrograma++;
                break;
            case 4:
                resultado = R.drawable.ic_parrilla4;
                iconoPrograma++;
                break;
            case 5:
                resultado = R.drawable.ic_parrilla5;
                iconoPrograma++;
                break;
            case 6:
                resultado = R.drawable.ic_parrilla6;
                iconoPrograma = 1;
                break;
        }
        return resultado;
    }

    @Override
    public ProgramaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_programa, parent, false);
        ProgramaViewHolder programa = new ProgramaViewHolder(view);
        return programa;
    }

    @Override
    public void onBindViewHolder(ProgramaViewHolder holder, int position) {
        holder.tituloPrograma.setText(programas.get(position).getTitulo());
        holder.descripcionPrograma.setText(programas.get(position).getDescripcion());
        holder.tituloPrograma.setText(programas.get(position).getTitulo());
        holder.imagenPrograma.setImageResource(seleccionarIconoPrograma());
        holder.fragment = fragment;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return programas.size();
    }
}
