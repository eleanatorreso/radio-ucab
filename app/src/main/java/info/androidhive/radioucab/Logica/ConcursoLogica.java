package info.androidhive.radioucab.Logica;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import info.androidhive.radioucab.Model.MiConcurso;

public class ConcursoLogica {

    public List<MiConcurso> almacenarMisConcursos(JSONArray misConcursos) {
        MiConcurso.deleteAll(MiConcurso.class);
        List<MiConcurso> concursos = new ArrayList<MiConcurso>();
        JSONObject objeto = null;
        for (int concurso = 0; concurso < misConcursos.length(); concurso++) {
            try {
                objeto = misConcursos.getJSONObject(concurso);
                int gano = 0;
                if (!objeto.getString("ganador").equals(""))
                    gano = 1;
                MiConcurso nuevo = new MiConcurso(objeto.getInt("id_concurso"), objeto.getString("nombre_concurso"), objeto.getString("fecha_concurso"),
                        objeto.getString("descripcion_concurso"), gano, objeto.getString("estado_concurso"),objeto.getString("ganador"));
                nuevo.save();
                concursos.add(nuevo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return concursos;
    }
}
