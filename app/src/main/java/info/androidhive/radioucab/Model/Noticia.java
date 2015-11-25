package info.androidhive.radioucab.Model;

import com.orm.SugarRecord;

public class Noticia extends SugarRecord<Noticia> {

    private int myId;
    private String titular;
    private String texto_noticia;
    private String link;
    private String fuente;

    public Noticia () {
    }

    public Noticia(int myId, String titular, String texto_noticia, String link, String fuente) {
        this.myId = myId;
        this.titular = titular;
        this.texto_noticia = texto_noticia;
        this.link = link;
        this.fuente = fuente;
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
