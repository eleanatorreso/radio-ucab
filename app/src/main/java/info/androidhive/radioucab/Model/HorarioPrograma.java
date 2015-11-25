package info.androidhive.radioucab.Model;

import com.orm.SugarRecord;

public class HorarioPrograma extends SugarRecord<HorarioPrograma> {
    private int myId;
    private String horario;
    private Programa programa;
    //private Long idPrograma;

    public HorarioPrograma(int myId, String horario, Programa programa) {
        this.myId = myId;
        this.horario = horario;
        //this.idPrograma = idPrograma;
        this.programa = programa;
    }

    public HorarioPrograma () {}

    public int getMyId() {
        return myId;
    }

    public void setMyId(int myId) {
        this.myId = myId;
    }

    /*public Long getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(Long idPrograma) {
        this.idPrograma = idPrograma;
    }*/

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    public String getHorario() {

        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
