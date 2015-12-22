package info.androidhive.radioucab.Model;

import com.orm.SugarRecord;

import java.util.Date;

public class Evento extends SugarRecord implements Comparable<Evento> {
    private int myId;
    private String titulo;
    private String horario;
    private String direccion;
    private Date fecha_inicio;

    public Evento() {

    }

    public Evento(int myId, String titulo, String horario, String direccion, Date fecha_inicio) {
        this.myId = myId;
        this.titulo = titulo;
        this.horario = horario;
        this.direccion = direccion;
        this.fecha_inicio = fecha_inicio;
    }

    public int getMyId() {
        return myId;
    }

    public void setMyId(int myId) {
        this.myId = myId;
    }

    public Date getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public int compareTo(Evento otroEvento) {
        if (fecha_inicio.compareTo(otroEvento.fecha_inicio) < 0) {
            return -1;
        }
        else if (fecha_inicio.compareTo(otroEvento.fecha_inicio) > 0) {
            return 1;
        }
        return 0;
    }
}
