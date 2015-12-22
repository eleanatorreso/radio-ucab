package info.androidhive.radioucab.Model;

import com.orm.SugarRecord;

public class Hashtag extends SugarRecord {

    public String hashtag;
    public int tipo;
    public int idPrograma;

    public Hashtag() {

    }

    public Hashtag(String hashtag, int tipo, int idPrograma) {
        this.hashtag = hashtag;
        this.tipo = tipo;
        this.idPrograma = idPrograma;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(int idPrograma) {
        this.idPrograma = idPrograma;
    }
}
