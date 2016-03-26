package info.androidhive.radioucab.Controlador.ViewHolder;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import info.androidhive.radioucab.Controlador.ProgramaDetalleFragment;
import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.Logica.ProgramaLogica;
import info.androidhive.radioucab.Model.HorarioPrograma;
import info.androidhive.radioucab.Model.Locutor;
import info.androidhive.radioucab.Model.Programa;
import info.androidhive.radioucab.R;

public class ProgramaViewHolder extends RecyclerView.ViewHolder {

    public CardView cardView;
    public TextView tituloPrograma;
    public TextView descripcionPrograma;
    public ImageView imagenPrograma;
    public Fragment fragment;
    private final ProgramaLogica programaLogica = new ProgramaLogica();

    public ProgramaViewHolder(View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.cardViewPrograma);
        tituloPrograma = (TextView) itemView.findViewById(R.id.titulo_programa);
        descripcionPrograma = (TextView) itemView.findViewById(R.id.descripcion_programa);
        imagenPrograma = (ImageView) itemView.findViewById(R.id.imagen_programa);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                programaLogica.ingresarDetallePrograma(tituloPrograma.getText().toString());
            }

        });
    }
}
