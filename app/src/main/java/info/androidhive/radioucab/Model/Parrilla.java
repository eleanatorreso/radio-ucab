package info.androidhive.radioucab.Model;

import com.orm.SugarRecord;

public class Parrilla extends SugarRecord {

    public String horario;
    public String nombrePrograma;
    public int idPrograma;

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
}
