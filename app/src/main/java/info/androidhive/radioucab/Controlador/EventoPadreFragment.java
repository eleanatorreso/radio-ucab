package info.androidhive.radioucab.Controlador;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import info.androidhive.radioucab.R;

public class EventoPadreFragment extends ParentViewHolder {
    public TextView tituloEvento;
    public ImageButton parentDropDownArrow;

    public EventoPadreFragment(View itemView) {
        super(itemView);

        tituloEvento = (TextView) itemView.findViewById(R.id.titulo_evento);
        parentDropDownArrow = (ImageButton) itemView.findViewById(R.id.list_item_expand_arrow);
    }
}
