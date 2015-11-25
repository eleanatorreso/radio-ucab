package info.androidhive.radioucab.Model;

import com.orm.SugarRecord;
import java.util.List;

public class Programa extends SugarRecord<Programa> {
    private int myId;
    private String titulo;
    private String descripcion;
    private List<HorarioPrograma> horarios;
    private List<Locutor> locutores;
    private String image;

    public Programa (){

    }

    public Programa(int myId, String titulo, String descripcion, String image) {
        this.myId = myId;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.image = image;
    }

    public Programa(int myId, String titulo, String descripcion) {
        this.myId = myId;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public int getMyId() {
        return myId;
    }

    public void setMyId(int myId) {
        this.myId = myId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    private List<HorarioPrograma> busquedaHorarios () {
        horarios = HorarioPrograma.find(HorarioPrograma.class, "programa = ?", String.valueOf(this.getId()));
        return horarios;
    }

    public List<HorarioPrograma> getHorarios() {
        if (horarios != null)
            return  horarios;
        return busquedaHorarios();
    }

    public void setHorarios(List<HorarioPrograma> horarios) {
        this.horarios = horarios;
    }

    private List<Locutor> busquedaLocutores () {
        locutores = Locutor.find(Locutor.class, "programa = ?", String.valueOf(this.getId()));
        return locutores;
    }

    public List<Locutor> getLocutores() {
        if (horarios != null)
            return  locutores;
        return busquedaLocutores();
    }

}
