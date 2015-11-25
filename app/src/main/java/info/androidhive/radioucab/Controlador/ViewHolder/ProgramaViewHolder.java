package info.androidhive.radioucab.Controlador.ViewHolder;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import info.androidhive.radioucab.Controlador.DetalleProgramaFragment;
import info.androidhive.radioucab.Model.HorarioPrograma;
import info.androidhive.radioucab.Model.Locutor;
import info.androidhive.radioucab.Model.Programa;
import info.androidhive.radioucab.R;

public class ProgramaViewHolder extends RecyclerView.ViewHolder{

    public CardView cardView;
    public TextView tituloPrograma;
    public TextView descripcionPrograma;
    public ImageView imagenPrograma;
    public Fragment fragment;

    public ProgramaViewHolder(View itemView) {
        super(itemView);
        cardView = (CardView)itemView.findViewById(R.id.cardViewPrograma);
        tituloPrograma = (TextView)itemView.findViewById(R.id.titulo_programa);
        descripcionPrograma = (TextView)itemView.findViewById(R.id.descripcion_programa);
        imagenPrograma = (ImageView)itemView.findViewById(R.id.imagen_programa);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                try {
                    DetalleProgramaFragment detalle = new DetalleProgramaFragment();
                    List<Programa> programaSeleccionado = Programa.find(Programa.class, "titulo =?", tituloPrograma.getText().toString());
                    Programa p = Programa.findById(Programa.class, Long.parseLong("1"));
                    List<Locutor> aaa = Locutor.listAll(Locutor.class);
                    HorarioPrograma ss = HorarioPrograma.findById(HorarioPrograma.class,Long.parseLong("1"));
                    if (programaSeleccionado != null && programaSeleccionado.size() > 0) {
                        detalle.programa = programaSeleccionado.get(0);
                        FragmentManager fm = fragment.getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.addToBackStack("atras");
                        ft.replace(((ViewGroup)fragment.getView().getParent()).getId(), detalle);
                        ft.commit();
                    }
                }
                catch (Exception e) {
                    int x = 2;
                }
            }

        });
    }
}
