package info.androidhive.radioucab.Model;

import com.orm.SugarRecord;

public class Tag extends SugarRecord {
    private int myId;
    private String nombre_tag;
    private int usuario_tag;

    public Tag(){

    }

    public Tag(int myId, String nombre_tag) {
        this.myId = myId;
        this.nombre_tag = nombre_tag;
    }

    public int getMyId() {
        return myId;
    }

    public void setMyId(int myId) {
        this.myId = myId;
    }

    public String getNombre_tag() {
        return nombre_tag;
    }

    public void setNombre_tag(String nombre_tag) {
        this.nombre_tag = nombre_tag;
    }

    public int getUsuario_tag() {
        return usuario_tag;
    }

    public void setUsuario_tag(int usuario_tag) {
        this.usuario_tag = usuario_tag;
    }
}
