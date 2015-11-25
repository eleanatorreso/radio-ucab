package info.androidhive.radioucab.Logica;

import org.json.JSONArray;
import org.json.JSONObject;

public interface RespuestaAsyncTask {
    public void procesoExitoso(JSONArray resultados);
    public void procesoExitoso(JSONObject resultado);
    public void procesoExitoso(String resultado);
    public void procesoNoExitoso();
}
