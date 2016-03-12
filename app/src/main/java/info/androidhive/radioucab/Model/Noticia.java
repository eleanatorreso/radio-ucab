package info.androidhive.radioucab.Model;

import com.orm.SugarRecord;

public class Noticia extends SugarRecord {

    private int myId;
    private String titular;
    private String texto_noticia;
    private String link;
    private String fuente;
    private int tipo;
    private String uploader;
    private String fecha_creacion;
    private String etiquetas;

    public Noticia () {
    }

    public Noticia(int myId, String titular, String texto_noticia, String link, String fuente, int tipo, String uploader, String fecha_creacion, String etiquetas) {
        this.myId = myId;
        this.titular = titular;
        this.texto_noticia = texto_noticia;
        this.link = link;
        this.fuente = fuente;
        this.tipo = tipo;
        this.uploader = uploader;
        this.fecha_creacion = fecha_creacion;
        this.etiquetas = etiquetas;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public String getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(String etiquetas) {
        this.etiquetas = etiquetas;
    }

    public int getMyId() {
        return myId;
    }

    public void setMyId(int myId) {
        this.myId = myId;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getTexto_noticia() {
        return texto_noticia;
    }

    public void setTexto_noticia(String texto_noticia) {
        this.texto_noticia = texto_noticia;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }
}
