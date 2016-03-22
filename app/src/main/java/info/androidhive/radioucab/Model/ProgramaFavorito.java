package info.androidhive.radioucab.Model;

import com.orm.SugarRecord;

public class ProgramaFavorito extends SugarRecord {

    private int idPrograma;
    private String nombrePrograma;

    public ProgramaFavorito() {
    }

    public ProgramaFavorito(int idPrograma, String nombrePrograma) {
        this.idPrograma = idPrograma;
        this.nombrePrograma = nombrePrograma;
    }

    public int getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(int idPrograma) {
        this.idPrograma = idPrograma;
    }

    public String getNombrePrograma() {
        return nombrePrograma;
    }

    public void setNombrePrograma(String nombrePrograma) {
        this.nombrePrograma = nombrePrograma;
    }
}
