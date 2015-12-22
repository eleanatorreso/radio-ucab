package info.androidhive.radioucab.Model;

import com.orm.SugarRecord;

public class LocutorPrograma extends SugarRecord {

    private Programa programa;
    private Locutor locutor;

    public LocutorPrograma() {
    }

    public LocutorPrograma(Programa programa, Locutor locutor) {
        this.programa = programa;
        this.locutor = locutor;
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    public Locutor getLocutor() {
        return locutor;
    }

    public void setLocutor(Locutor locutor) {
        this.locutor = locutor;
    }
}
