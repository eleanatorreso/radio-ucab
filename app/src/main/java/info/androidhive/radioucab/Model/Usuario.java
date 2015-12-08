package info.androidhive.radioucab.Model;

import com.orm.SugarRecord;

public class Usuario extends SugarRecord<Programa> {
    private String nombre;
    private String apellido;
    private String correo;
    private String usuario_twitter;
    private String token_twitter;
    private String token_secret_twitter;
    private String guid;
    private String imagenGrande;
    private String imagenNormal;
    private String formatoImagen;

    public Usuario() {

    }

    public Usuario(String usuario_twitter, String token_twitter, String token_secret_twitter, String imagenNormal) {
        this.usuario_twitter = usuario_twitter;
        this.token_twitter = token_twitter;
        this.token_secret_twitter = token_secret_twitter;
        this.imagenNormal = imagenNormal;
    }

    public Usuario(String nombre, String apellido, String correo, String usuario_twitter, String token_twitter
            , String token_secret_twitter, String guid, String imagenNormal, String imagenGrande) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.usuario_twitter = usuario_twitter;
        this.token_twitter = token_twitter;
        this.token_secret_twitter = token_secret_twitter;
        this.guid = guid;
        this.imagenNormal = imagenNormal;
        this.imagenGrande = imagenGrande;
    }

    public String getToken_twitter() {
        return token_twitter;
    }

    public void setToken_twitter(String token_twitter) {
        this.token_twitter = token_twitter;
    }

    public String getToken_secret_twitter() {
        return token_secret_twitter;
    }

    public void setToken_secret_twitter(String token_secret_twitter) {
        this.token_secret_twitter = token_secret_twitter;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
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

    public String getUsuario_twitter() {
        return usuario_twitter;
    }

    public void setUsuario_twitter(String usuario_twitter) {
        this.usuario_twitter = usuario_twitter;
    }

    public String getImagenGrande() {
        return imagenGrande;
    }

    public void setImagenGrande(String imagenGrande) {
        this.imagenGrande = imagenGrande;
    }

    public String getImagenNormal() {
        return imagenNormal;
    }

    public void setImagenNormal(String imagenNormal) {
        this.imagenNormal = imagenNormal;
    }

    public String getFormatoImagen() {
        return formatoImagen;
    }

    public void setFormatoImagen(String formatoImagen) {
        this.formatoImagen = formatoImagen;
    }
}
