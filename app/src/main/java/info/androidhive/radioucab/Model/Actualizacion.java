package info.androidhive.radioucab.Model;

import android.support.annotation.Nullable;

import com.orm.SugarRecord;
import java.util.Date;

public class Actualizacion extends SugarRecord {
    private Date actEvento;
    private Date actNoticia;
    private Date actPrograma;

    public Actualizacion () {

    }

    public Actualizacion(Date actEvento, Date actNoticia, Date actPrograma) {
        this.actEvento = actEvento;
        this.actNoticia = actNoticia;
        this.actPrograma = actPrograma;
    }

    public Date getActPrograma() {
        return actPrograma;
    }

    public void setActPrograma(Date actPrograma) {
        this.actPrograma = actPrograma;
    }

    public Date getActEvento() {
        return actEvento;
    }

    public void setActEvento(Date actEvento) {
        this.actEvento = actEvento;
    }

    public Date getActNoticia() {
        return actNoticia;
    }

    public void setActNoticia(Date actNoticia) {
        this.actNoticia = actNoticia;
    }

}
