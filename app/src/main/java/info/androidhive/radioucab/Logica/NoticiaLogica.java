package info.androidhive.radioucab.Logica;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import info.androidhive.radioucab.Model.Noticia;

public class NoticiaLogica {

    public List<Noticia> procesarResultados(JSONArray resultado) {
        Noticia noticiaNueva = new Noticia();
        List<Noticia> noticias = new ArrayList<Noticia>();
        try {
            Noticia.deleteAll(Noticia.class);
            for (int i = 0; i < resultado.length(); i++) {
                JSONObject objeto = resultado.getJSONObject(i);
                noticiaNueva = new Noticia(objeto.getInt("id"), objeto.getString("titular"), objeto.getString("texto_noticia")
                        , objeto.getString("link"), objeto.getString("fuente"), objeto.getInt("tipo"), objeto.getString("uploader"),
                        objeto.getString("fecha_creacion"), objeto.getString("noticia_tag"));
                noticiaNueva.save();
                noticias.add(noticiaNueva);
            }
            return noticias;
        } catch (Exception e) {
            Log.e("Noticias:procesando", e.getMessage());
        }
        return null;
    }
}
