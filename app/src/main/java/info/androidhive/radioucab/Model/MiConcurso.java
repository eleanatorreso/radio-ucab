package info.androidhive.radioucab.Model;

import com.orm.SugarRecord;

public class MiConcurso extends SugarRecord {
    private int myId;
    private String nombreConcurso;
    private String fechas;
    private String descripcion;
    private int gano;
    private String estado_concurso;
    private String resultado;

    public MiConcurso(){}

    public MiConcurso(int myId, String nombreConcurso, String fechas, String descripcion, int gano, String estado_concurso, String resultado) {
        this.myId = myId;
        this.nombreConcurso = nombreConcurso;
        this.fechas = fechas;
        this.descripcion = descripcion;
        this.gano = gano;
        this.estado_concurso = estado_concurso;
        this.resultado = resultado;
    }

    public int getMyId() {
        return myId;
    }

    public void setMyId(int myId) {
        this.myId = myId;
    }

    public String getNombreConcurso() {
        return nombreConcurso;
    }

    public void setNombreConcurso(String nombreConcurso) {
        this.nombreConcurso = nombreConcurso;
    }

    public String getFechas() {
        return fechas;
    }

    public void setFechas(String fechas) {
        this.fechas = fechas;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getGano() {
        return gano;
    }

    public void setGano(int gano) {
        this.gano = gano;
    }

    public String getEstado_concurso() {
        return estado_concurso;
    }

    public void setEstado_concurso(String estado_concurso) {
        this.estado_concurso = estado_concurso;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}


