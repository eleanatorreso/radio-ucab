package info.androidhive.radioucab.Controlador;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

import info.androidhive.radioucab.R;

public class EventoHijoFragment extends ChildViewHolder {

    public TextView horarioEvento;
    public TextView direccionEvento;

    public EventoHijoFragment(View itemView) {
        super(itemView);
        horarioEvento = (TextView) itemView.findViewById(R.id.evento_horario);
        direccionEvento = (TextView) itemView.findViewById(R.id.evento_direccion);
    }
}
