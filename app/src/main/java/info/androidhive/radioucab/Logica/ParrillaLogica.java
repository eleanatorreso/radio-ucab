package info.androidhive.radioucab.Logica;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import info.androidhive.radioucab.Conexiones.conexionGETAPIJSONArray;
import info.androidhive.radioucab.Conexiones.conexionGETAPIString;
import info.androidhive.radioucab.Model.Actualizacion;
import info.androidhive.radioucab.Model.Parrilla;
import info.androidhive.radioucab.Model.Programa;
import info.androidhive.radioucab.R;

public class ParrillaLogica implements RespuestaAsyncTask{

    private static ParrillaLogica instancia;
    private ProgramaLogica programaLogica = new ProgramaLogica();
    private final ManejoFecha manejoFecha = new ManejoFecha();
    private final ActualizacionLogica actualizacionLogica = new ActualizacionLogica();
    private List<String> horas;
    private List<String> programas;
    private Date ultimaActWS;
    private static final ManejoFecha tiempoActual = new ManejoFecha();
    private Context contexto;
    private static int tipo;
    public RespuestaProgramaAsyncTask delegate = null;
    private final ManejoActivity manejoActivity = ManejoActivity.getInstancia();
    private Programa programaActual;

    public List<String> getHoras() {
        return horas;
    }

    public void setHoras(List<String> horas) {
        this.horas = horas;
    }

    public List<String> getProgramas() {
        return programas;
    }

    public void setProgramas(List<String> programas) {
        this.programas = programas;
    }

    public boolean comprobarActualizacionParrilla() {
        List<Actualizacion> listaActualizaciones = Actualizacion.listAll(Actualizacion.class);
        if (listaActualizaciones != null && !listaActualizaciones.isEmpty()) {
            Actualizacion actualizacion = listaActualizaciones.get(0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            if (sdf.format(actualizacion.getActParrilla()).equals(sdf.format(new Date()))) {
                return true;
            }
        }
        return false;
    }

    public void procesarResultados(Date ultimaActWS, JSONArray resultados){
        actualizacionLogica.almacenarUltimaActualizacion(4, ultimaActWS);
        Parrilla.deleteAll(Parrilla.class);
        for (int i = 0; i < resultados.length(); i++) {
            try {
                JSONObject objeto = resultados.getJSONObject(i);
                final Parrilla parrilla = new Parrilla(objeto.getString("hora_inicio") + " - " + objeto.getString("hora_fin"),
                        objeto.getString("nombre"), objeto.getInt("id"));
                parrilla.save();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mostrarNombrePrograma();
    }

    public void mostrarNombrePrograma(){
        final String nombrePrograma = calculoPrograma();
        if (tipo == 0) {
            manejoActivity.mostrarNombreInformacion(nombrePrograma);
        }
        else {
            delegate.procesoExitoso(programaActual);
        }
    }

    private String calculoPrograma(){
        List<Parrilla> parrillas = Parrilla.listAll(Parrilla.class);
        final Date horaActual = manejoFecha.convertirStringHora(manejoFecha.getHoraActual());
        if (parrillas != null && parrillas.size() > 0) {
            for (Parrilla parrilla : parrillas) {
                final String[] split = parrilla.getHorario().split("-");
                final Date horaInicio = manejoFecha.convertirStringHora(split[0].trim());
                Date horaFin = manejoFecha.convertirStringHora(split[1].trim());
                if (horaFin.equals(manejoFecha.convertirStringHora(manejoFecha.getHoraTope())))
                    horaFin = manejoFecha.convertirStringHora(manejoFecha.getUltimoMinutoDia());
                if ((horaActual.after(horaInicio) && horaActual.before(horaFin)) || (horaActual.equals(horaInicio) || horaActual.equals(horaFin))) {
                    programaActual = programaLogica.getDatosPrograma(parrilla.getIdPrograma());
                    return contexto.getResources().getString(R.string.dialogo_mensaje_informacion) + " " + parrilla.getNombrePrograma();
                }
            }
        }
        return contexto.getResources().getString(R.string.campo_error_sin_programa_actual);
    }

    public void getProgramaActual(Context contexto, int tipo) {
        this.tipo = tipo;
        this.contexto = contexto;
        String respuesta = "";
        if (actualizacionLogica.comprobarActualizacionParrilla()) {
            respuesta = calculoPrograma();
            if (tipo == 0) {
                manejoActivity.mostrarNombreInformacion(respuesta);
            }
            else {
                delegate.procesoExitoso(programaActual);
            }
        }
        else {
            comprobarUltimaActualizacion();
        }
    }

    private void comprobarUltimaActualizacion() {
        final conexionGETAPIString conexionString = new conexionGETAPIString();
        conexionString.contexto = contexto;
        conexionString.delegate = this;
        conexionString.execute("Api/Programa/GetDiaActual");
    }

    private void cargarParrilla() {
        final conexionGETAPIJSONArray conexion = new conexionGETAPIJSONArray();
        conexion.contexto = contexto;
        conexion.delegate = this;
        TimeZone zonaHoraria = TimeZone.getDefault();
        conexion.execute("Api/Programa/GetContenido?GMT=" + zonaHoraria.getDisplayName(false, TimeZone.SHORT));
    }

    private void ultimaActualizacion(String resultado) {
        try {
            resultado = resultado.replace("\"", "");
            ultimaActWS = tiempoActual.convertirStringSimple(resultado);
            cargarParrilla();
        } catch (Exception e) {
            Log.e("Parrilla: ultima act", e.getMessage());
        }
    }

    @Override
    public void procesoExitoso(JSONArray resultados) {
        procesarResultados(ultimaActWS, resultados);
    }

    @Override
    public void procesoExitoso(JSONObject resultado) {

    }

    @Override
    public void procesoExitoso(int codigo, int tipo) {

    }

    @Override
    public void procesoExitoso(String respuesta) {
        ultimaActualizacion(respuesta);
    }

    @Override
    public void procesoNoExitoso() {
        manejoActivity.mostrarNombreInformacion(contexto.getString(R.string.campo_error_sin_programa_actual));
        delegate.procesoNoExitoso();
    }
}
