package info.androidhive.radioucab.Model;

import com.orm.SugarRecord;

public class Premio extends SugarRecord {
    private int id;
    private String nombre;
    private String descripcion;
    private int posicion;
    private Concurso concurso;

    public Premio(){

    }

    public Premio(int id, String nombre, String descripcion, int posicion, Concurso concurso) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.posicion = posicion;
        this.concurso = concurso;
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

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public Concurso getConcurso() {
        return concurso;
    }

    public void setConcurso(Concurso concurso) {
        this.concurso = concurso;
    }
}
