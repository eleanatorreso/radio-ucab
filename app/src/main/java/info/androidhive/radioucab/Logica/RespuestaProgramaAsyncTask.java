package info.androidhive.radioucab.Logica;

import org.json.JSONArray;
import org.json.JSONObject;

import info.androidhive.radioucab.Model.Parrilla;
import info.androidhive.radioucab.Model.Programa;

public interface RespuestaProgramaAsyncTask {
    public void procesoExitoso(Parrilla programa);
    public void procesoNoExitoso();
}
