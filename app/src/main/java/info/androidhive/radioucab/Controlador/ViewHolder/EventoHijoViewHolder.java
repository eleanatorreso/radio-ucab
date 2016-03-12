package info.androidhive.radioucab.Controlador.ViewHolder;

public class EventoHijoViewHolder {

    private String direccionEvento;
    private String horarioEvento;
    private String descripcion;


    public EventoHijoViewHolder(String direccionEvento, String horarioEvento, String descripcion) {
        this.direccionEvento = direccionEvento;
        this.horarioEvento = horarioEvento;
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
