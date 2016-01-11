package info.androidhive.radioucab.Logica;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import info.androidhive.radioucab.Conexiones.conexionGETAPIJSONArray;
import info.androidhive.radioucab.Model.Concurso;
import info.androidhive.radioucab.Model.Premio;

public class ManejoConcurso implements RespuestaAsyncTask {

    private Context contexto;
    private ManejoProgressDialog manejoProgressDialog = ManejoProgressDialog.getInstancia();
    private RespuestaLogica delegate = null;

    public ManejoConcurso(Context contexto, RespuestaLogica delegate){
        this.contexto = contexto;
        this.delegate = delegate;
    }

    public void comprobarConcursosActuales() {
        Concurso.deleteAll(Concurso.class);
        conexionGETAPIJSONArray conexion = new conexionGETAPIJSONArray();
        conexion.contexto = contexto;
        conexion.mensaje = "";
        conexion.delegate = this;
        conexion.execute("Api/Concurso/getConcursoActuales");
    }

    public void cargarPremiacionesActuales(JSONArray resultados){
        List<Concurso> concursos = new ArrayList<Concurso>();
        Concurso.deleteAll(Concurso.class);
        Premio.deleteAll(Premio.class);
        Concurso concursoNuevo = new Concurso();
        Premio premioNuevo = new Premio();
        Date fecha_inicio = new Date();
        Date fecha_fin = new Date();
        try {
            for (int concurso = 0; concurso < resultados.length(); concurso++) {
                JSONObject objeto = resultados.getJSONObject(concurso);
                JSONArray premios = objeto.getJSONArray("premios");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                fecha_inicio = dateFormat.parse(objeto.getString("fecha_inicio"));
                fecha_fin = dateFormat.parse(objeto.getString("fecha_fin"));
                concursoNuevo = new Concurso(objeto.getInt("id"), objeto.getString("nombre"), objeto.getString("descripcion"),
                        objeto.getInt("comentarios_dia"), fecha_inicio, fecha_fin, objeto.getString("hashtag"), objeto.getString("terminos_condiciones"));
                concursoNuevo.save();
                for (int premio = 0; premio < premios.length(); premio++) {
                    JSONObject objetoPremio = premios.getJSONObject(premio);
                    premioNuevo = new Premio(objetoPremio.getInt("id"), objetoPremio.getString("nombre_premio"), objetoPremio.getString("descripcion_premio"),
                            objetoPremio.getInt("posicion"), concursoNuevo);
                    premioNuevo.save();
                }
            }
        }
        catch (Exception excep) {
            Log.e("Concurso: cargar", excep.getMessage());
        }
    }

    @Override
    public void procesoExitoso(JSONArray resultados) {
        if (resultados != null && resultados.length() > 0){
            cargarPremiacionesActuales(resultados);
        }
        delegate.procesoExitoso();
    }

    @Override
    public void procesoExitoso(JSONObject resultado) {
        delegate.procesoNoExitoso();
    }

    @Override
    public void procesoExitoso(int codigo, int tipo) {

    }

    @Override
    public void procesoNoExitoso() {

    }
}
