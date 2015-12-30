package info.androidhive.radioucab.Model;

import com.orm.SugarRecord;

import java.sql.Time;

public class HorarioPrograma extends SugarRecord {
    private int myId;
    private String horario_inicio;
    private String horario_fin;
    private int dia_semana;
    private Programa programa;

    public HorarioPrograma(int myId, int dia_semana, String horario_inicio, String horario_fin, Programa programa) {
        this.myId = myId;
        this.horario_fin = horario_fin;
        this.horario_inicio = horario_inicio;
        this.programa = programa;
        this.dia_semana = dia_semana;
    }

    public HorarioPrograma () {}

    public int getMyId() {
        return myId;
    }

    public void setMyId(int myId) {
        this.myId = myId;
    }


    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    public String getHorario_inicio() {
        return horario_inicio;
    }

    public void setHorario_inicio(String horario_inicio) {
        this.horario_inicio = horario_inicio;
    }

    public String getHorario_fin() {
        return horario_fin;
    }

    public void setHorario_fin(String horario_fin) {
        this.horario_fin = horario_fin;
    }

    public int getDia_semana() {
        return dia_semana;
    }

    public void setDia_semana(int dia_semana) {
        this.dia_semana = dia_semana;
    }
}
