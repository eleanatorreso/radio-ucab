package info.androidhive.radioucab.Model;

import com.orm.SugarRecord;

public class Configuracion extends SugarRecord {
    private int usoHighStreaming;
    private int recibirNotificaciones;

    public Configuracion(){

    }

    public int getUsoHighStreaming() {
        return usoHighStreaming;
    }

    public void setUsoHighStreaming(int usoHighStreaming) {
        this.usoHighStreaming = usoHighStreaming;
    }

    public int getRecibirNotificaciones() {
        return recibirNotificaciones;
    }

    public void setRecibirNotificaciones(int recibirNotificaciones) {
        this.recibirNotificaciones = recibirNotificaciones;
    }
}
