package info.androidhive.radioucab.Logica;


import android.app.Activity;
import android.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.radioucab.Conexiones.conexionPUTAPI;
import info.androidhive.radioucab.Controlador.ProgramaDetalleFragment;
import info.androidhive.radioucab.Model.HorarioPrograma;
import info.androidhive.radioucab.Model.Locutor;
import info.androidhive.radioucab.Model.LocutorPrograma;
import info.androidhive.radioucab.Model.Parrilla;
import info.androidhive.radioucab.Model.Programa;
import info.androidhive.radioucab.Model.ProgramaFavorito;
import info.androidhive.radioucab.R;

public class ProgramaLogica implements RespuestaAsyncTask {

    private final ManejoActivity manejoActivity = ManejoActivity.getInstancia();
    private final ManejoUsuarioActual manejoUsuarioActual = ManejoUsuarioActual.getInstancia();
    private final ManejoToast manejoToast = ManejoToast.getInstancia();
    private static Programa programaPendiente;
    private static boolean almacenamientoProgramaFavorito = false;
    private Activity activity;
    private ProgramaDetalleFragment fragment;

    public static boolean isAlmacenamientoProgramaFavorito() {
        return almacenamientoProgramaFavorito;
    }

    public static void setAlmacenamientoProgramaFavorito(boolean almacenamientoProgramaFavorito) {
        ProgramaLogica.almacenamientoProgramaFavorito = almacenamientoProgramaFavorito;
    }

    public List<HorarioPrograma> procesarHorarios(JSONArray resultadoConsulta, Programa programa) {
        List<HorarioPrograma> horarios = new ArrayList<HorarioPrograma>();
        JSONObject objeto;
        HorarioPrograma horarioNuevo = new HorarioPrograma();
        for (int horario = 0; horario < resultadoConsulta.length(); horario++) {
            try {
                objeto = resultadoConsulta.getJSONObject(horario);
                //int myId, int dia_semana, Time horario_inicio, Time horario_fin, Programa programa
                horarioNuevo = new HorarioPrograma(objeto.getInt("id"), objeto.getInt("dia_semana"),
                        objeto.getString("hora_inicio"), objeto.getString("hora_final"), programa);
                horarioNuevo.save();
                horarios.add(horarioNuevo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return horarios;
    }

    public List<Locutor> procesarLocutores(JSONArray resultadoConsulta) {
        List<Locutor> locutores = new ArrayList<Locutor>();
        JSONObject objeto;
        Locutor locutorNuevo = new Locutor();
        for (int horario = 0; horario < resultadoConsulta.length(); horario++) {
            try {
                objeto = resultadoConsulta.getJSONObject(horario);
                locutorNuevo = new Locutor(objeto.getInt("id"), objeto.getString("nombreCompleto"),
                        objeto.getString("usuarioTwitter"), objeto.getString("usuarioFacebook"), objeto.getString("usuarioInstagram"),
                        objeto.getString("usuarioNombreFacebook"));
                locutorNuevo.save();
                locutores.add(locutorNuevo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return locutores;
    }

    public List<Programa> procesarResultados(JSONArray resultadoConsulta) {
        Programa programaNuevo = new Programa();
        LocutorProgramaLogica listaProgramaLocutor = new LocutorProgramaLogica();
        List<Programa> programas = new ArrayList<Programa>();
        JSONObject objeto = null;
        for (int programa = 0; programa < resultadoConsulta.length(); programa++) {
            try {
                objeto = resultadoConsulta.getJSONObject(programa);
                JSONArray locutores = objeto.getJSONArray("locutores");
                JSONArray horarios = objeto.getJSONArray("horarios");
                programaNuevo = new Programa(objeto.getInt("id"), objeto.getString("nombre"), objeto.getString("descripcion"), objeto.getInt("tipo"));
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

    public Parrilla getDatosPrograma(int idPrograma){
        final List<Parrilla> programas = Parrilla.find(Parrilla.class, "id_programa = ?", idPrograma + "");
        if (programas != null && programas.size() > 0) {
            final Parrilla programa = programas.get(0);
            return programa;
        }
        return null;
    }

    public void almacenarProgramasFavoritos(JSONArray programas) {
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

    public Programa buscarProgramaTitulo(String tituloPrograma) {
        final List<Programa> programaSeleccionado = Programa.find(Programa.class, "titulo =?", tituloPrograma);
        if (programaSeleccionado != null && programaSeleccionado.size() > 0) {
            return programaSeleccionado.get(0);
        }
        return null;
    }

    public void ingresarDetallePrograma(String tituloPrograma) {
        final Programa programa = buscarProgramaTitulo(tituloPrograma);
        if (programa != null) {
            final ProgramaDetalleFragment detalle = (ProgramaDetalleFragment) manejoActivity.cambiarFragment("ProgramaDetalle", true, false);
            detalle.programa = programa;
        }
    }

    public boolean comprobarProgramaFavorito(String titulo) {
        boolean esFavorito = false;
        final List<ProgramaFavorito> programasFavoritos = ProgramaFavorito.find(ProgramaFavorito.class, "nombre_programa = ?", titulo);
        if (programasFavoritos != null && programasFavoritos.size() > 0) {
            esFavorito = true;
        }
        return esFavorito;
    }

    public void almacenarProgramaFavorito(String tituloPrograma, Activity activity, Fragment fragment) {
        final Programa programa = buscarProgramaTitulo(tituloPrograma);
        if (programa != null) {
            almacenamientoProgramaFavorito = true;
            programaPendiente = programa;
            this.fragment = (ProgramaDetalleFragment)fragment;
            final conexionPUTAPI conexion = new conexionPUTAPI();
            this.activity = activity;
            conexion.contexto = activity;
            conexion.delegate = this;
            JSONObject objeto = new JSONObject();
            try {
                objeto.put("idPrograma", programa.getMyId());
                objeto.put("guid", manejoUsuarioActual.getGuid());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            conexion.objeto = objeto;
            conexion.execute("Api/Programa/PutProgramaFavorito");
        }
    }

    public void actualizarProgramaFavorito(){
        if (programaPendiente != null){
            final List<ProgramaFavorito> programasFavoritos = ProgramaFavorito.find(ProgramaFavorito.class, "nombre_programa = ?",
                    programaPendiente.getTitulo());
            if (programasFavoritos != null && programasFavoritos.size() > 0) {
                final ProgramaFavorito programaFavorito = programasFavoritos.get(0);
                programaFavorito.delete();
            }
            else {
                final ProgramaFavorito programaFavorito = new ProgramaFavorito(programaPendiente.getMyId(), programaPendiente.getTitulo());
                programaFavorito.save();
            }
            fragment.comprobarColoImagenFavorito();
            programaPendiente = null;
        }
    }

    @Override
    public void procesoExitoso(JSONArray resultados) {

    }

    @Override
    public void procesoExitoso(JSONObject resultado) {

    }

    @Override
    public void procesoExitoso(int codigo, int tipo) {
        if (codigo == 204) {
            actualizarProgramaFavorito();
        }
        else {
            manejoToast.crearToast(activity, activity.getResources().getString(R.string.campo_toast_error_actualizar_programas_favoritos));
        }
        almacenamientoProgramaFavorito = false;
    }

    @Override
    public void procesoExitoso(String respuesta) {
    }

    @Override
    public void procesoNoExitoso() {
        almacenamientoProgramaFavorito = false;
        manejoToast.crearToast(activity, activity.getResources().getString(R.string.campo_toast_error_actualizar_programas_favoritos));
    }
}
