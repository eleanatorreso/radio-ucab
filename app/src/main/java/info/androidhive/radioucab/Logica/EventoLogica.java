package info.androidhive.radioucab.Logica;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import info.androidhive.radioucab.Model.Evento;

/**
 * Created by Eleana Torres on 12/3/2016.
 */
public class EventoLogica {

    public List<Evento> procesarResultados(JSONArray resultadoConsulta) {
        List<Evento> eventos = new ArrayList<Evento>();
        Evento.deleteAll(Evento.class);
        Evento eventoNuevo = new Evento();
        try {
            for (int evento = 0; evento < resultadoConsulta.length(); evento++) {
                JSONObject objeto = resultadoConsulta.getJSONObject(evento);
                JSONArray direccion = objeto.getJSONArray("detalles");
                for (int detalles = 0; detalles < direccion.length(); detalles++) {
                    JSONObject objetoDetalle = direccion.getJSONObject(detalles);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                    Date fecha_inicio = new Date();
                    fecha_inicio = dateFormat.parse(objetoDetalle.getString("fecha_inicio"));
                    eventoNuevo = new Evento(objeto.getInt("id"), objeto.getString("nombre"), objetoDetalle.getString("horario")
                            , objetoDetalle.getString("direccion_completa"),fecha_inicio, objeto.getString("descripcion"));
                    eventoNuevo.save();
                    eventos.add(eventoNuevo);
                }
            }
        } catch (Exception excep) {
            Log.e("Eventos: procesar resul", excep.getMessage());
        }
        Collections.sort(eventos);
        return eventos;
    }

}
