package info.androidhive.radioucab.Controlador.ViewHolder;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;

public class EventoPadreViewHolder implements ParentObject {

    private List<Object> listaHijosEvento;
    private String tituloEvento;

    public EventoPadreViewHolder(List<Object> listaHijosEvento, String tituloEvento) {
        this.listaHijosEvento = listaHijosEvento;
        this.tituloEvento = tituloEvento;
    }

    public String getTituloEvento() {
        return tituloEvento;
    }

    public void setTituloEvento(String tituloEvento) {
        this.tituloEvento = tituloEvento;
    }

    @Override
    public List<Object> getChildObjectList() {
        return listaHijosEvento;
    }

    @Override
    public void setChildObjectList(List<Object> list) {
        listaHijosEvento = list;
    }


}
