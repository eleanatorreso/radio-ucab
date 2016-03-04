package info.androidhive.radioucab.Model;

import com.orm.SugarRecord;

public class Configuracion extends SugarRecord {
    public int usoHighStreaming;

    public int getUsoHighStreaming() {
        return usoHighStreaming;
    }

    public void setUsoHighStreaming(int usoHighStreaming) {
        this.usoHighStreaming = usoHighStreaming;
    }
}
