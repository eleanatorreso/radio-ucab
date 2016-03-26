package info.androidhive.radioucab.Logica;

import com.orm.query.Condition;
import com.orm.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.radioucab.Model.Tag;

public class TagLogica {
    private ManejoUsuarioActual manejoUsuarioActual = ManejoUsuarioActual.getInstancia();

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
                tagAModificar.setUsuario_tag(1);
                tagAModificar.save();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void modificarTagsAlmacenados(Long id, int valorSeleccionado){
        Tag tag = Tag.findById(Tag.class, id);
        if (tag != null) {
            tag.setUsuario_tag(valorSeleccionado);
            tag.save();
        }
    }

    public void actualizarTagModificados() {
        final List<Tag> tagsModificado = manejoUsuarioActual.getMisPreferenciasNoticias();
        for (Tag tag: tagsModificado) {
            modificarTagsAlmacenados(tag.getId(),tag.getUsuario_tag());
        }
        manejoUsuarioActual.setMisPreferenciasNoticias(new ArrayList<Tag>());
    }
}
