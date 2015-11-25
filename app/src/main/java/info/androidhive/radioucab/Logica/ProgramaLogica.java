package info.androidhive.radioucab.Logica;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.radioucab.Model.HorarioPrograma;
import info.androidhive.radioucab.Model.Locutor;
import info.androidhive.radioucab.Model.LocutorPrograma;
import info.androidhive.radioucab.Model.Programa;

public class ProgramaLogica {

    public List<HorarioPrograma> procesarHorarios (JSONArray resultadoConsulta, Programa programa) {
        List<HorarioPrograma> horarios = new ArrayList<HorarioPrograma>();
        JSONObject objeto;
        HorarioPrograma horarioNuevo = new HorarioPrograma();
        for (int horario = 0; horario < resultadoConsulta.length(); horario ++) {
            try {
                objeto = resultadoConsulta.getJSONObject(horario);
                horarioNuevo = new HorarioPrograma(objeto.getInt("id"), objeto.getString("horario"), programa);
                horarioNuevo.save();
                horarios.add(horarioNuevo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return horarios;
    }

    public List<Locutor> procesarLocutores (JSONArray resultadoConsulta) {
        List<Locutor> locutores = new ArrayList<Locutor>();
        JSONObject objeto;
        Locutor locutorNuevo = new Locutor();
        for (int horario = 0; horario < resultadoConsulta.length(); horario ++) {
            try {
                objeto = resultadoConsulta.getJSONObject(horario);
                locutorNuevo = new Locutor(objeto.getInt("id"), objeto.getString("nombreCompleto"),
                        objeto.getString("usuarioTwitter"), objeto.getString("usuarioFacebook"), objeto.getString("usuarioInstagram"));
                locutorNuevo.save();
                locutores.add(locutorNuevo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return locutores;
    }

    public List<Programa> procesarResultados (JSONArray resultadoConsulta) {
        Programa programaNuevo = new Programa();
        LocutorProgramaLogica listaProgramaLocutor = new LocutorProgramaLogica();
        List<Programa> programas = new ArrayList<Programa>();
        for (int evento = 0; evento < resultadoConsulta.length(); evento++) {
            JSONObject objeto = null;
            try {
                objeto = resultadoConsulta.getJSONObject(evento);
                JSONArray locutores = objeto.getJSONArray("locutores");
                JSONArray horarios = objeto.getJSONArray("horarios");
                //Long id, String titulo, String descripcion, List<HorarioPrograma> horarios, List<Locutor> locutores, String image
                programaNuevo = new Programa(objeto.getInt("id"), objeto.getString("nombre"), objeto.getString("descripcion"));
                programaNuevo.save();
                procesarHorarios(horarios, programaNuevo);
                List<Locutor> locutoresPrograma = procesarLocutores(locutores);
                listaProgramaLocutor.procesarLocutoresPrograma(locutoresPrograma, programaNuevo);
                programas.add(programaNuevo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            int a = 2;
        }
        List<LocutorPrograma> aaa = LocutorPrograma.listAll(LocutorPrograma.class);
        return programas;
    }
}
