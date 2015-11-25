package info.androidhive.radioucab.Controlador.ViewHolder;

public class EventoHijoViewHolder {

    private String direccionEvento;
    private String horarioEvento;

    public EventoHijoViewHolder(String direccionEvento, String horarioEvento) {
        this.direccionEvento = direccionEvento;
        this.horarioEvento = horarioEvento;
    }

    public String getDireccionEvento() {
        return direccionEvento;
    }

    public void setDireccionEvento(String direccionEvento) {
        this.direccionEvento = direccionEvento;
    }

    public String getHorarioEvento() {
        return horarioEvento;
    }

    public void setHorarioEvento(String horarioEvento) {
        this.horarioEvento = horarioEvento;
    }
}
