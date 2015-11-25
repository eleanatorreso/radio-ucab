package info.androidhive.radioucab.Model;

import com.orm.SugarRecord;

public class Usuario extends SugarRecord<Programa> {
    private int myId;
    private String nombre;
    private String apellido;
    private String correo;
    private String usuarioTwitter;
    private String tokenAccess;
    private String token;
    private String secret;
    private String imagenPerfilURL;

    public Usuario() {

    }

    public Usuario(int myId, String nombre, String apellido, String correo, String usuarioTwitter, String tokenAccess, String token, String secret) {
        this.myId = myId;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.usuarioTwitter = usuarioTwitter;
        this.tokenAccess = tokenAccess;
        this.token = token;
        this.secret = secret;
    }

    public int getMyId() {
        return myId;
    }

    public void setMyId(int myId) {
        this.myId = myId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUsuarioTwitter() {
        return usuarioTwitter;
    }

    public void setUsuarioTwitter(String usuarioTwitter) {
        this.usuarioTwitter = usuarioTwitter;
    }

    public String getTokenAccess() {
        return tokenAccess;
    }

    public void setTokenAccess(String tokenAccess) {
        this.tokenAccess = tokenAccess;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getImagenPerfilURL() {
        return imagenPerfilURL;
    }

    public void setImagenPerfilURL(String imagenPerfilURL) {
        this.imagenPerfilURL = imagenPerfilURL;
    }
}
