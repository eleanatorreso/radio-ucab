package info.androidhive.radioucab.Logica;

import org.json.JSONArray;
import org.json.JSONObject;

import info.androidhive.radioucab.Model.Programa;

public interface RespuestaProgramaAsyncTask {
    public void procesoExitoso(Programa programa);
    public void procesoNoExitoso();
}
