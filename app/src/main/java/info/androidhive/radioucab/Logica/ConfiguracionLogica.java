package info.androidhive.radioucab.Logica;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import info.androidhive.radioucab.Model.Configuracion;

public class ConfiguracionLogica {

    public void almacenarConfiguracion(int recibirNotificaciones, int usoStreaming) {
        final List<Configuracion> configuracionList = Configuracion.listAll(Configuracion.class);
        Configuracion configuracionActual = new Configuracion();
        if (configuracionList != null && configuracionList.size() > 0) {
            configuracionActual = configuracionList.get(0);
        }
        configuracionActual.setRecibirNotificaciones(recibirNotificaciones);
        configuracionActual.setUsoHighStreaming(usoStreaming);
        configuracionActual.save();
    }

    public void almacenarConfiguracionBD(int resultado) {
        Configuracion.deleteAll(Configuracion.class);
        Configuracion configuracionNueva = new Configuracion();
        configuracionNueva.setRecibirNotificaciones(resultado);
        configuracionNueva.save();
    }
}
