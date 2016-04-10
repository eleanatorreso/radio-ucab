package info.androidhive.radioucab.Model;

import com.orm.SugarRecord;

import java.util.List;

public class Parrilla extends SugarRecord {

    private String horario;
    private String nombrePrograma;
    private int idPrograma;
    private List<LocutorParrilla> locutores;

    public Parrilla() {

    }

    public Parrilla(String horario, String nombrePrograma, int idPrograma) {
        this.horario = horario;
        this.nombrePrograma = nombrePrograma;
        this.idPrograma = idPrograma;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getNombrePrograma() {
        return nombrePrograma;
    }

    public void setNombrePrograma(String nombrePrograma) {
        this.nombrePrograma = nombrePrograma;
    }

    public int getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(int idPrograma) {
        this.idPrograma = idPrograma;
    }

    private List<LocutorParrilla> busquedaLocutores () {
        locutores = LocutorParrilla.find(LocutorParrilla.class, "parrilla = ?", String.valueOf(this.getId()));
        return locutores;
    }

    public List<LocutorParrilla> getLocutores() {
        if (locutores != null)
            return  locutores;
        return busquedaLocutores();
    }
}
