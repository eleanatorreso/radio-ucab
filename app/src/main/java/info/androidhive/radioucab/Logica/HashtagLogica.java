package info.androidhive.radioucab.Logica;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import info.androidhive.radioucab.Conexiones.conexionGETAPIJSONArray;
import info.androidhive.radioucab.Model.Hashtag;

public class HashtagLogica implements RespuestaAsyncTask {
    private conexionGETAPIJSONArray conexion = new conexionGETAPIJSONArray();
    private Context contexto;

    public HashtagLogica(Context contexto) {
        this.contexto = contexto;
    }

    public void actualizarListaHashtag() {
        conexion = new conexionGETAPIJSONArray();
        conexion.contexto = contexto;
        conexion.delegate = this;
        conexion.execute("Api/Comentario/GetHashtags");
    }

    private void almacenarHashtag(JSONArray resultados){
        Hashtag hashtag;
        for (int i = 0; i < resultados.length(); i++) {
            try {
                JSONObject objeto = resultados.getJSONObject(i);
                hashtag = new Hashtag(objeto.getString("hashtag"), objeto.getInt("tipo"),objeto.getInt("idPrograma"));
                hashtag.save();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void procesoExitoso(JSONArray resultados) {
        if (resultados != null && resultados.length() > 0) {
            almacenarHashtag(resultados);
        }
    }

    @Override
    public void procesoExitoso(JSONObject resultado) {

    }

    @Override
    public void procesoExitoso(int codigo, int tipo) {

    }

    @Override
    public void procesoNoExitoso() {

    }
}
