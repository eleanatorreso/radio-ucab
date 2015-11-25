package info.androidhive.radioucab.Controlador.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;

import info.androidhive.radioucab.Controlador.EventoHijoFragment;
import info.androidhive.radioucab.Controlador.EventoPadreFragment;
import info.androidhive.radioucab.Controlador.ViewHolder.EventoHijoViewHolder;
import info.androidhive.radioucab.Controlador.ViewHolder.EventoPadreViewHolder;
import info.androidhive.radioucab.R;

public class AdaptadorEvento extends ExpandableRecyclerAdapter<EventoPadreFragment, EventoHijoFragment> {

    LayoutInflater inflater;

    public AdaptadorEvento(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
        inflater = LayoutInflater.from(context);
    }

    public AdaptadorEvento(Context context, List<ParentObject> parentItemList, int customParentAnimationViewId) {
        super(context, parentItemList, customParentAnimationViewId);
        inflater = LayoutInflater.from(context);
    }

    public AdaptadorEvento(Context context, List<ParentObject> parentItemList, int customParentAnimationViewId, long animationDuration) {
        super(context, parentItemList, customParentAnimationViewId, animationDuration);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public EventoPadreFragment onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.fragment_eventos_parent, viewGroup, false);
        return new EventoPadreFragment(view);
    }

    @Override
    public EventoHijoFragment onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.fragment_eventos_child, viewGroup, false);
        return new EventoHijoFragment(view);
    }

    @Override
    public void onBindParentViewHolder(EventoPadreFragment eventoPadreFragment, int i, Object o) {
        EventoPadreViewHolder evento = (EventoPadreViewHolder)o;
        eventoPadreFragment.tituloEvento.setText(evento.getTituloEvento());
    }

    @Override
    public void onBindChildViewHolder(EventoHijoFragment eventoHijoFragment, int i, Object o) {
        EventoHijoViewHolder child = (EventoHijoViewHolder)o;
        eventoHijoFragment.direccionEvento.setText(child.getDireccionEvento());
        eventoHijoFragment.horarioEvento.setText(child.getHorarioEvento());
    }
}
