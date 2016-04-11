package info.androidhive.radioucab.Model;

import com.orm.SugarRecord;

import java.util.Date;
import java.util.List;

public class Concurso extends SugarRecord {
    private int myId;
    private String nombre;
    private String descripcion;
    private int comentarios_dia;
    private Date fecha_inicio;
    private Date fecha_fin;
    private String hashtag;
    private String terminos_condiciones;
    private List<Premio> premios;

    public Concurso(){

    }

    public Concurso(int myId, String nombre, String descripcion, int comentarios_dia, Date fecha_inicio, Date fecha_fin, String hashtag, String terminos_condiciones) {
        this.myId = myId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.comentarios_dia = comentarios_dia;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.hashtag = hashtag;
        this.terminos_condiciones = terminos_condiciones;
    }

    public int getMyId() {
        return myId;
    }

    public void setMyId(int myId) {
        this.myId = myId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getComentarios_dia() {
        return comentarios_dia;
    }

    public void setComentarios_dia(int comentarios_dia) {
        this.comentarios_dia = comentarios_dia;
    }

    public Date getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public Date getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(Date fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public String getTerminos_condiciones() {
        return terminos_condiciones;
    }

    public void setTerminos_condiciones(String terminos_condiciones) {
        this.terminos_condiciones = terminos_condiciones;
    }

    private List<Premio> busquedaPremios () {
        premios = Premio.find(Premio.class, "concurso = ?", String.valueOf(this.getId()));
        return premios;
    }

    public List<Premio> getPremios() {
        if (premios != null)
            return  premios;
        return busquedaPremios();
    }

    public void setPremios(List<Premio> premios) {
        this.premios = premios;
    }
}
