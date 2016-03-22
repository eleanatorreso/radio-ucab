package info.androidhive.radioucab.Model;

import com.orm.SugarRecord;

import java.util.List;

public class Usuario extends SugarRecord {
    private String nombre;
    private String apellido;
    private String correo;
    private String usuario_twitter;
    private String token_twitter;
    private String token_secret_twitter;
    private String guid;
    private String imagen_grande;
    private String imagen_normal;
    private String formato_imagen;
    private String id_movil;
    private int comentarios_inapropiados;
    private boolean sancionado;

    public Usuario() {

    }

    public Usuario(String nombre, String correo, String usuario_twitter, String token_twitter
            , String token_secret_twitter, String imagen_normal) {
        this.nombre = nombre;
        this.correo = correo;
        this.usuario_twitter = usuario_twitter;
        this.token_twitter = token_twitter;
        this.token_secret_twitter = token_secret_twitter;
        this.imagen_normal = imagen_normal;
    }

    public Usuario(String usuario_twitter, String token_twitter, String token_secret_twitter, String imagen_normal) {
        this.usuario_twitter = usuario_twitter;
        this.token_twitter = token_twitter;
        this.token_secret_twitter = token_secret_twitter;
        this.imagen_normal = imagen_normal;
    }

    public Usuario(String nombre, String apellido, String correo, String usuario_twitter, String token_twitter
            , String token_secret_twitter, String guid, String imagen_normal, String imagen_grande, int comentarios_inapropiados,
                   boolean sancionado) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.usuario_twitter = usuario_twitter;
        this.token_twitter = token_twitter;
        this.token_secret_twitter = token_secret_twitter;
        this.guid = guid;
        this.imagen_normal = imagen_normal;
        this.imagen_grande = imagen_grande;
        this.sancionado = sancionado;
        this.comentarios_inapropiados = comentarios_inapropiados;
    }

    public String getId_movil() {
        return id_movil;
    }

    public void setId_movil(String id_movil) {
        this.id_movil = id_movil;
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

    public String getImagen_grande() {
        return imagen_grande;
    }

    public void setImagen_grande(String imagen_grande) {
        this.imagen_grande = imagen_grande;
    }

    public String getImagen_normal() {
        return imagen_normal;
    }

    public void setImagen_normal(String imagen_normal) {
        this.imagen_normal = imagen_normal;
    }

    public String getFormato_imagen() {
        return formato_imagen;
    }

    public void setFormato_imagen(String formato_imagen) {
        this.formato_imagen = formato_imagen;
    }

    public int getComentarios_inapropiados() {
        return comentarios_inapropiados;
    }

    public void setComentarios_inapropiados(int comentarios_inapropiados) {
        this.comentarios_inapropiados = comentarios_inapropiados;
    }

    public boolean isSancionado() {
        return sancionado;
    }

    public void setSancionado(boolean sancionado) {
        this.sancionado = sancionado;
    }
}
