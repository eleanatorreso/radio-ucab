package info.androidhive.radioucab.Controlador.ViewHolder;

import android.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import info.androidhive.radioucab.Controlador.ProgramaDetalleFragment;
import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.Model.HorarioPrograma;
import info.androidhive.radioucab.Model.Locutor;
import info.androidhive.radioucab.Model.MiConcurso;
import info.androidhive.radioucab.Model.Programa;
import info.androidhive.radioucab.R;

public class MisConcursosViewHolder extends RecyclerView.ViewHolder {

    public CardView cardView;
    public TextView tituloConcurso;
    public TextView fechasConcurso;
    public TextView descripcionConcurso;
    public TextView estadoConcurso;
    public TextView resultadoConcurso;
    public int ganoConcurso;
    public ImageView imagen_trofeo;
    public int myId;
    public Fragment fragment;

    private ManejoActivity manejoActivity = ManejoActivity.getInstancia();

    public MisConcursosViewHolder(View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.cardViewConcurso);
        tituloConcurso = (TextView) itemView.findViewById(R.id.texto_nombre_concurso);
        descripcionConcurso = (TextView) itemView.findViewById(R.id.texto_descripcion_concurso);
        fechasConcurso = (TextView) itemView.findViewById(R.id.texto_fecha_concurso);
        estadoConcurso = (TextView) itemView.findViewById(R.id.texto_estado_concurso);
        resultadoConcurso = (TextView) itemView.findViewById(R.id.texto_resultado_concurso);
        imagen_trofeo = (ImageView) itemView.findViewById(R.id.imagen_concurso_trofeo);
    }
}
