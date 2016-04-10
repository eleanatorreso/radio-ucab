package info.androidhive.radioucab.Model;

import com.orm.SugarRecord;

public class LocutorParrilla extends SugarRecord {

    public Parrilla parrilla;
    public int MyId;
    public String nombreLocutor;
    public String usuarioTwitter;

    public LocutorParrilla() {

    }

    public int getMyId() {
        return MyId;
    }

    public void setMyId(int myId) {
        MyId = myId;
    }

    public LocutorParrilla(Parrilla parrilla, int myId, String nombreLocutor, String usuarioTwitter) {
        this.MyId = myId;
        this.parrilla = parrilla;
        this.nombreLocutor = nombreLocutor;
        this.usuarioTwitter = usuarioTwitter;
    }

    public Parrilla getParrilla() {
        return parrilla;
    }

    public void setParrilla(Parrilla parrilla) {
        this.parrilla = parrilla;
    }

    public String getNombreLocutor() {
        return nombreLocutor;
    }

    public void setNombreLocutor(String nombreLocutor) {
        this.nombreLocutor = nombreLocutor;
    }

    public String getUsuarioTwitter() {
        return usuarioTwitter;
    }

    public void setUsuarioTwitter(String usuarioTwitter) {
        this.usuarioTwitter = usuarioTwitter;
    }
}
