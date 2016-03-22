package info.androidhive.radioucab.Logica;

import com.orm.query.Condition;
import com.orm.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import info.androidhive.radioucab.Model.Tag;

public class TagLogica {

    public void actualizarTags(JSONArray tags, JSONArray tagsUsuario) {
        Tag.deleteAll(Tag.class);
        JSONObject objeto = null;
        for (int tag = 0; tag < tags.length(); tag++) {
            try {
                objeto = tags.getJSONObject(tag);
                Tag nuevo = new Tag(objeto.getInt("id"), objeto.getString("tag1"));
                nuevo.save();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        asociarTagUsuario(tagsUsuario);
    }

    public void asociarTagUsuario(JSONArray tagsUsuario) {
        JSONObject objeto = null;
        for (int tag = 0; tag < tagsUsuario.length(); tag++) {
            try {
                objeto = tagsUsuario.getJSONObject(tag);
                List<Tag> lista = Tag.findWithQuery(Tag.class,"Select * from Tag where my_id = ?",objeto.getString("id_tag"));
                Tag tagAModificar = lista.get(0);
                tagAModificar.setUsuario_tag(true);
                tagAModificar.save();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
