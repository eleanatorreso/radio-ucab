package info.androidhive.radioucab.Logica;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.radioucab.Controlador.ProgramaDetalleFragment;
import info.androidhive.radioucab.Model.HorarioPrograma;
import info.androidhive.radioucab.Model.Locutor;
import info.androidhive.radioucab.Model.Programa;
import info.androidhive.radioucab.Model.ProgramaFavorito;

public class ProgramaLogica {

    private ManejoActivity manejoActivity = ManejoActivity.getInstancia();

    public List<HorarioPrograma> procesarHorarios (JSONArray resultadoConsulta, Programa programa) {
        List<HorarioPrograma> horarios = new ArrayList<HorarioPrograma>();
        JSONObject objeto;
        HorarioPrograma horarioNuevo = new HorarioPrograma();
        for (int horario = 0; horario < resultadoConsulta.length(); horario ++) {
            try {
                objeto = resultadoConsulta.getJSONObject(horario);
                //int myId, int dia_semana, Time horario_inicio, Time horario_fin, Programa programa
                horarioNuevo = new HorarioPrograma(objeto.getInt("id"), objeto.getInt("dia_semana"),
                        objeto.getString("hora_inicio"),objeto.getString("hora_final"), programa);
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
        JSONObject objeto = null;
        for (int programa = 0; programa < resultadoConsulta.length(); programa++) {
            try {
                objeto = resultadoConsulta.getJSONObject(programa);
                JSONArray locutores = objeto.getJSONArray("locutores");
                JSONArray horarios = objeto.getJSONArray("horarios");
                programaNuevo = new Programa(objeto.getInt("id"), objeto.getString("nombre"), objeto.getString("descripcion"),objeto.getInt("tipo"));
                programaNuevo.save();
                procesarHorarios(horarios, programaNuevo);
                List<Locutor> locutoresPrograma = procesarLocutores(locutores);
                listaProgramaLocutor.procesarLocutoresPrograma(locutoresPrograma, programaNuevo);
                programas.add(programaNuevo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return programas;
    }

    public Programa getProgramaActual() {
        return null;
    }

    public void almacenarProgramasFavoritos(JSONArray programas){
        ProgramaFavorito.deleteAll(ProgramaFavorito.class);
        JSONObject objeto = null;
        for (int programa = 0; programa < programas.length(); programa++) {
            try {
                objeto = programas.getJSONObject(programa);
                ProgramaFavorito nuevo = new ProgramaFavorito(objeto.getInt("id_programa"), objeto.getString("nombre_programa"));
                nuevo.save();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void ingresarDetallePrograma(String tituloPrograma){
        List<Programa> programaSeleccionado = Programa.find(Programa.class, "titulo =?", tituloPrograma);
        if (programaSeleccionado != null && programaSeleccionado.size() > 0) {
            ProgramaDetalleFragment detalle = (ProgramaDetalleFragment) manejoActivity.cambiarFragment("ProgramaDetalle", true);
            detalle.programa = programaSeleccionado.get(0);
        }
    }

    public boolean comprobarProgramaFavorito(String titulo){
        boolean esFavorito = false;
        List<ProgramaFavorito> programasFavoritos = ProgramaFavorito.find(ProgramaFavorito.class, "nombre_programa = ?", titulo);
        if (programasFavoritos != null && programasFavoritos.size() > 0) {
            esFavorito = true;
        }
        return esFavorito;
    }

}
