package info.androidhive.radioucab.Model;

import com.orm.SugarRecord;

public class Locutor extends SugarRecord {

    private int myId;
    private String nombreCompleto;
    private String usuarioTwitter;
    private String usuarioFacebook;
    private String usuarioNombreFacebook;
    private String usuarioInstagram;

    public Locutor () {}

    public Locutor(int myId, String nombreCompleto, String usuarioTwitter, String usuarioFacebook, String usuarioInstagram, String usuarioNombreFacebook) {
        this.myId = myId;
        this.nombreCompleto = nombreCompleto;
        this.usuarioTwitter = usuarioTwitter;
        this.usuarioFacebook = usuarioFacebook;
        this.usuarioInstagram = usuarioInstagram;
        this.usuarioNombreFacebook = usuarioNombreFacebook;
    }

    public String getUsuarioNombreFacebook() {
        return usuarioNombreFacebook;
    }

    public void setUsuarioNombreFacebook(String usuarioNombreFacebook) {
        this.usuarioNombreFacebook = usuarioNombreFacebook;
    }

    public int getMyId() {
        return myId;
    }

    public void setMyId(int myId) {
        this.myId = myId;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getUsuarioTwitter() {
        return usuarioTwitter;
    }

    public void setUsuarioTwitter(String usuarioTwitter) {
        this.usuarioTwitter = usuarioTwitter;
    }

    public String getUsuarioFacebook() {
        return usuarioFacebook;
    }

    public void setUsuarioFacebook(String usuarioFacebook) {
        this.usuarioFacebook = usuarioFacebook;
    }

    public String getUsuarioInstagram() {
        return usuarioInstagram;
    }

    public void setUsuarioInstagram(String usuarioInstagram) {
        this.usuarioInstagram = usuarioInstagram;
    }
}
